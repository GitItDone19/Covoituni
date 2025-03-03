package User;

import Services.IService;
import utils.MyConnection;
import entities.Role;
import entities.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceUser implements IService<User> {
    private Connection connection;
    private ServiceRole serviceRole;

    public ServiceUser() {
        this.connection = MyConnection.getInstance().getCnx();
        this.serviceRole = new ServiceRole();
    }

    @Override
    public void create(User user) throws SQLException {
        String sql = "INSERT INTO utilisateur (nom, prenom, tel, email, mdp, role_code, verificationcode, rating, trips_count, username) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        pst.setString(1, user.getNom());
        pst.setString(2, user.getPrenom());
        pst.setString(3, user.getTel());
        pst.setString(4, user.getEmail());
        pst.setString(5, user.getMdp());
        pst.setString(6, user.getRoleCode());
        pst.setString(7, user.getVerificationCode());
        pst.setDouble(8, user.getRating());
        pst.setInt(9, user.getTripsCount());
        pst.setString(10, user.getUsername());

        pst.executeUpdate();
        
        // Get the generated ID and set it to the user object
        ResultSet generatedKeys = pst.getGeneratedKeys();
        if (generatedKeys.next()) {
            user.setId(generatedKeys.getInt(1));
        }
    }

    @Override
    public void update(User user) throws SQLException {
        String sql = "UPDATE utilisateur SET nom=?, prenom=?, tel=?, email=?, mdp=?, role_code=?, verificationcode=?, username=?, image_path=? WHERE id=?";

        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setString(1, user.getNom());
        pst.setString(2, user.getPrenom());
        pst.setString(3, user.getTel());
        pst.setString(4, user.getEmail());
        pst.setString(5, user.getMdp());
        pst.setString(6, user.getRoleCode());
        pst.setString(7, user.getVerificationCode());
        pst.setString(8, user.getUsername());
        pst.setString(9, user.getImagePath()); // ✅ Update image path
        pst.setInt(10, user.getId());

        pst.executeUpdate();
    }


    @Override
    public void delete(User user) throws SQLException {
        String sql = "DELETE FROM utilisateur WHERE id=?";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setInt(1, user.getId());  // Use the user object to get the ID
        pst.executeUpdate();
    }

    @Override
    public ArrayList<User> readAll() throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        String sql = "SELECT u.*, r.id as role_id, r.display_name FROM utilisateur u " +
                    "JOIN role r ON u.role_code = r.code";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(sql);
        
        while (rs.next()) {
            Role role = new Role(
                rs.getInt("role_id"),
                rs.getString("role_code"),
                rs.getString("display_name")
            );
            
            User user = new User(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getString("prenom"),
                rs.getString("tel"),
                rs.getString("email"),
                rs.getString("mdp"),
                role,
                rs.getString("verificationcode")
            );
            user.setRating(rs.getDouble("rating"));
            user.setTripsCount(rs.getInt("trips_count"));
            
            // Load the image path
            String imagePath = rs.getString("image_path");
            if (imagePath != null && !imagePath.isEmpty()) {
                user.setImagePath(imagePath);
            }
            
            users.add(user);
        }
        return users;
    }


    public boolean emailExists(String email) throws SQLException {
        String req = "SELECT COUNT(*) FROM utilisateur WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        }
    }


    public void updateUserImage(int userId, String imagePath) {
        String query = "UPDATE utilisateur SET image_path = ? WHERE id = ?";

        try (
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, imagePath);
            stmt.setInt(2, userId);

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating profile image: " + e.getMessage());
        }
    }

    public double calculateNewAverageRating(int userId, int newRating) throws SQLException {
        String query = "SELECT rating, trips_count FROM utilisateur WHERE id = ?";
        double newAverage = 0.0;

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                double currentRating = rs.getDouble("rating");
                int currentTripsCount = rs.getInt("trips_count");

                // Calculate new average
                newAverage = (currentRating * currentTripsCount + newRating) / (currentTripsCount + 1);
            }
        }
        return newAverage;
    }

    /**
     * Creates a user and returns the generated ID
     * @param user The user to create
     * @return The generated ID
     * @throws SQLException If an error occurs
     */
    public int createAndGetId(User user) throws SQLException {
        create(user);
        return user.getId();
    }
    
    /**
     * Finds a user by their email address
     * @param email The email address to search for
     * @return The user if found, null otherwise
     * @throws SQLException If an error occurs
     */
    public User findByEmail(String email) throws SQLException {
        String sql = "SELECT u.*, r.id as role_id, r.display_name FROM utilisateur u " +
                    "JOIN role r ON u.role_code = r.code " +
                    "WHERE u.email = ?";
        
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                Role role = new Role(
                    rs.getInt("role_id"),
                    rs.getString("role_code"),
                    rs.getString("display_name")
                );
                
                User user = new User(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("tel"),
                    rs.getString("email"),
                    rs.getString("mdp"),
                    role,
                    rs.getString("verificationcode")
                );
                user.setRating(rs.getDouble("rating"));
                user.setTripsCount(rs.getInt("trips_count"));
                user.setUsername(rs.getString("username"));
                
                // Load the image path
                String imagePath = rs.getString("image_path");
                if (imagePath != null && !imagePath.isEmpty()) {
                    user.setImagePath(imagePath);
                }
                
                return user;
            }
        }
        
        return null;
    }
}
