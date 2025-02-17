package User;

import Services.IService;
import main.MyConnection;
import entities.User.Role;
import entities.User.User;

import java.sql.*;
import java.util.ArrayList;

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
        
        PreparedStatement pst = connection.prepareStatement(sql);
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
            users.add(user);
        }
        return users;
    }

    public ArrayList<User> getDriversByRating(double minRating) throws SQLException {
        ArrayList<User> drivers = new ArrayList<>();
        String sql = "SELECT u.*, r.id as role_id, r.display_name FROM utilisateur u " +
                    "JOIN role r ON u.role_code = r.code " +
                    "WHERE u.role_code = ? AND u.rating >= ?";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setString(1, Role.DRIVER_CODE);
        pst.setDouble(2, minRating);
        
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            Role role = new Role(
                rs.getInt("role_id"),
                rs.getString("role_code"),
                rs.getString("display_name")
            );
            
            User driver = new User(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getString("prenom"),
                rs.getString("tel"),
                rs.getString("email"),
                rs.getString("mdp"),
                role,
                rs.getString("verificationcode")
            );
            driver.setRating(rs.getDouble("rating"));
            driver.setTripsCount(rs.getInt("trips_count"));
            drivers.add(driver);
        }
        return drivers;
    }
    
    public void updateUserRating(int userId, double newRating) throws SQLException {
        String sql = "UPDATE utilisateur SET rating = ? WHERE id = ?";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setDouble(1, newRating);
        pst.setInt(2, userId);
        pst.executeUpdate();
    }
    
    public void incrementTripsCount(int userId) throws SQLException {
        String sql = "UPDATE utilisateur SET trips_count = trips_count + 1 WHERE id = ?";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setInt(1, userId);
        pst.executeUpdate();
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
}
