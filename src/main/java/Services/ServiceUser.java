package Services;

import utils.MyConnection;
import entities.User;
import java.sql.*;
import java.util.ArrayList;

public class ServiceUser implements IService<User> {
    private Connection connection;

    public ServiceUser() {
        this.connection = MyConnection.getInstance().getCnx();
    }

    @Override
    public void create(User user) throws SQLException {
        String sql = "INSERT INTO utilisateur (username, nom, prenom, tel, email, mdp, role_code, verificationcode, rating, trips_count) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setString(1, user.getUsername());
            pst.setString(2, user.getNom());
            pst.setString(3, user.getPrenom());
            pst.setString(4, user.getTel());
            pst.setString(5, user.getEmail());
            pst.setString(6, user.getMdp());
            pst.setString(7, user.getRoleCode());
            pst.setString(8, user.getVerificationcode());
            pst.setDouble(9, user.getRating());
            pst.setInt(10, user.getTripsCount());
            
            pst.executeUpdate();
        }
    }

    @Override
    public void update(User user) throws SQLException {
        String sql = "UPDATE utilisateur SET username=?, nom=?, prenom=?, tel=?, email=?, mdp=?, role_code=?, verificationcode=?, rating=?, trips_count=? WHERE id=?";
        
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setString(1, user.getUsername());
            pst.setString(2, user.getNom());
            pst.setString(3, user.getPrenom());
            pst.setString(4, user.getTel());
            pst.setString(5, user.getEmail());
            pst.setString(6, user.getMdp());
            pst.setString(7, user.getRoleCode());
            pst.setString(8, user.getVerificationcode());
            pst.setDouble(9, user.getRating());
            pst.setInt(10, user.getTripsCount());
            pst.setInt(11, user.getId());
            
            pst.executeUpdate();
        }
    }

    @Override
    public void delete(User user) throws SQLException {
        String sql = "DELETE FROM utilisateur WHERE id=?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, user.getId());
            pst.executeUpdate();
        }
    }

    @Override
    public ArrayList<User> readAll() throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        String sql = "SELECT * FROM utilisateur";
        
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setNom(rs.getString("nom"));
                user.setPrenom(rs.getString("prenom"));
                user.setTel(rs.getString("tel"));
                user.setEmail(rs.getString("email"));
                user.setMdp(rs.getString("mdp"));
                user.setRoleCode(rs.getString("role_code"));
                user.setVerificationcode(rs.getString("verificationcode"));
                user.setRating(rs.getDouble("rating"));
                user.setTripsCount(rs.getInt("trips_count"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
                users.add(user);
            }
        }
        return users;
    }

    public User findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM utilisateur WHERE email=?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setString(1, email);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setNom(rs.getString("nom"));
                    user.setPrenom(rs.getString("prenom"));
                    user.setTel(rs.getString("tel"));
                    user.setEmail(rs.getString("email"));
                    user.setMdp(rs.getString("mdp"));
                    user.setRoleCode(rs.getString("role_code"));
                    user.setVerificationcode(rs.getString("verificationcode"));
                    user.setRating(rs.getDouble("rating"));
                    user.setTripsCount(rs.getInt("trips_count"));
                    user.setCreatedAt(rs.getTimestamp("created_at"));
                    return user;
                }
            }
        }
        return null;
    }
}
