package Services;

import User.ServiceRole;
import utils.MyConnection;
import entities.Role;
import entities.User;
import utils.PasswordUtil;

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
        String sql = "UPDATE utilisateur SET nom=?, prenom=?, tel=?, email=?, mdp=?, role_code=?, verificationcode=?, username=? WHERE id=?";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setString(1, user.getNom());
        pst.setString(2, user.getPrenom());
        pst.setString(3, user.getTel());
        pst.setString(4, user.getEmail());
        pst.setString(5, user.getMdp());
        pst.setString(6, user.getRoleCode());
        pst.setString(7, user.getVerificationCode());
        pst.setString(8, user.getUsername());
        pst.setInt(9, user.getId());
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


    
    public void updateUserRating(int userId, double newRating) throws SQLException {
        String sql = "UPDATE utilisateur SET rating = ? WHERE id = ?";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setDouble(1, newRating);
        pst.setInt(2, userId);
        pst.executeUpdate();
    }

    public List<User> getUsersByRole(String roleCode) throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT u.*, r.id as role_id, r.display_name FROM utilisateur u " +
                "JOIN role r ON u.role_code = r.code " +
                "WHERE u.role_code = ?";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, roleCode);
            ResultSet rs = pst.executeQuery();

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
                users.add(user);
            }
        }
        return users;
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


    public boolean emailExists(String email) {
        String query = "SELECT COUNT(*) FROM utilisateur WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Return true if email exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean isTokenValid(String token) {
        String query = "SELECT email FROM password_reset_tokens WHERE token = ? AND expiration > NOW()";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, token);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // If there is a result, the token is valid
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean resetPassword(String token, String newPassword) {
        String getEmailQuery = "SELECT email FROM password_reset_tokens WHERE token = ? AND expiration > NOW()";
        try (PreparedStatement stmt = connection.prepareStatement(getEmailQuery)) {
            stmt.setString(1, token);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String email = rs.getString("email");

                // Hash the password before storing (for security)
                String hashedPassword = PasswordUtil.hashPassword(newPassword);

                // Update password in the users table
                String updateQuery = "UPDATE utilisateur SET password = ? WHERE email = ?";
                try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                    updateStmt.setString(1, hashedPassword);
                    updateStmt.setString(2, email);
                    updateStmt.executeUpdate();
                }

                // Delete the used token
                deleteToken(token);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public void storeResetToken(String email, String token) throws SQLException {
        String query = "INSERT INTO password_reset_tokens (email, token, expiration) VALUES (?, ?, NOW() + INTERVAL 1 HOUR)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, token);
            stmt.executeUpdate();
            System.out.println("Token stored for: " + email);
        }
    }


    public void deleteToken(String token) {
        String query = "DELETE FROM password_reset_tokens WHERE token = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, token);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

}
