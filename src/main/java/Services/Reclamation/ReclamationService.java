package Services.Reclamation;

import Services.IService;
import entities.Reclamation;
import entities.User;
import entities.Role;
import utils.MyConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReclamationService implements IService<Reclamation> {
    private Connection cnx;

    public ReclamationService() {
        cnx = MyConnection.getInstance().getCnx();
    }

    @Override
    public void create(Reclamation reclamation) throws SQLException {
        String req = "INSERT INTO reclamation (description, status, date, user_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setString(1, reclamation.getDescription());
            ps.setString(2, reclamation.getStatus());
            java.util.Date utilDate = reclamation.getDate();
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            ps.setDate(3, sqlDate);
            ps.setInt(4, reclamation.getUser().getId());
            ps.executeUpdate();
        }
    }

    @Override
    public List<Reclamation> readAll() throws SQLException {
        List<Reclamation> reclamations = new ArrayList<>();
        String req = "SELECT r.*, " +
                    "u.id as user_id, u.nom, u.prenom, u.tel, u.email, u.mdp, u.role_code, " +
                    "u.verificationcode, u.rating, u.trips_count, u.username, " +
                    "role.id as role_id, role.code as role_code, role.display_name " +
                    "FROM reclamation r " +
                    "LEFT JOIN utilisateur u ON r.user_id = u.id " +
                    "LEFT JOIN role ON u.role_code = role.code " +
                    "ORDER BY r.date DESC";
        
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(req)) {
            while (rs.next()) {
                Role role = new Role(
                    rs.getInt("role_id"),
                    rs.getString("role_code"),
                    rs.getString("display_name")
                );

                User user = new User(
                    rs.getInt("user_id"),
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

                Reclamation r = new Reclamation();
                r.setId(rs.getInt("id"));
                r.setDescription(rs.getString("description"));
                r.setStatus(rs.getString("status"));
                r.setDate(rs.getDate("date"));
                r.setUser(user);
                r.setAdminReply(rs.getString("admin_reply"));
                
                reclamations.add(r);
            }
        }
        return reclamations;
    }

    public List<Reclamation> getReclamationsByStatus(String status) throws SQLException {
        List<Reclamation> reclamations = new ArrayList<>();
        String query = "SELECT r.*, " +
                      "u.id as user_id, u.nom, u.prenom, u.tel, u.email, u.mdp, u.role_code, " +
                      "u.verificationcode, u.rating, u.trips_count, u.username, " +
                      "role.id as role_id, role.code as role_code, role.display_name " +
                      "FROM reclamation r " +
                      "LEFT JOIN utilisateur u ON r.user_id = u.id " +
                      "LEFT JOIN role ON u.role_code = role.code " +
                      "WHERE r.status = ? " +
                      "ORDER BY r.date DESC";
        
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Role role = new Role(
                    rs.getInt("role_id"),
                    rs.getString("role_code"),
                    rs.getString("display_name")
                );

                User user = new User(
                    rs.getInt("user_id"),
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

                Reclamation r = new Reclamation();
                r.setId(rs.getInt("id"));
                r.setDescription(rs.getString("description"));
                r.setStatus(rs.getString("status"));
                r.setDate(rs.getDate("date"));
                r.setUser(user);
                r.setAdminReply(rs.getString("admin_reply"));
                
                reclamations.add(r);
            }
        }
        return reclamations;
    }

    @Override
    public void update(Reclamation reclamation) throws SQLException {
        String query = "UPDATE reclamation SET description = ?, status = ? WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setString(1, reclamation.getDescription());
            ps.setString(2, "EN_COURS");
            ps.setInt(3, reclamation.getId());
            
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                reclamation.setStatus("EN_COURS");
            }
        }
    }

    @Override
    public void delete(Reclamation reclamation) throws SQLException {
        String query = "DELETE FROM reclamation WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setInt(1, reclamation.getId());
            ps.executeUpdate();
        }
    }

    public void reply(Reclamation reclamation, String response) throws SQLException {
        String query = "UPDATE reclamation SET admin_reply = ?, status = ? WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setString(1, response);
            ps.setString(2, "RESOLUE");
            ps.setInt(3, reclamation.getId());
            ps.executeUpdate();
            
            reclamation.setAdminReply(response);
            reclamation.setStatus("RESOLUE");
        }
    }
} 