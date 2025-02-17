package Services.Reclamation;

import entities.Reclamation;
import entities.User;
import utils.MyConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReclamationService implements IReclamationService {
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
            ps.setInt(4, reclamation.getUserId());
            ps.executeUpdate();
        }
    }

    @Override
    public List<Reclamation> readAll() throws SQLException {
        List<Reclamation> reclamations = new ArrayList<>();
        String req = "SELECT r.*, u.id as user_id, u.nom, u.prenom " +
                     "FROM reclamation r " +
                     "LEFT JOIN user u ON r.user_id = u.id " +
                     "ORDER BY r.date DESC";
        
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(req)) {
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("user_id"));
                user.setNom(rs.getString("nom"));
                user.setPrenom(rs.getString("prenom"));

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
        String query = "SELECT r.*, u.id as user_id, u.nom, u.prenom " +
                      "FROM reclamation r " +
                      "LEFT JOIN user u ON r.user_id = u.id " +
                      "WHERE r.status = ? " +
                      "ORDER BY r.date DESC";
        
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("user_id"));
                user.setNom(rs.getString("nom"));
                user.setPrenom(rs.getString("prenom"));

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
            ps.setString(2, "EN_COURS"); // Reset status to EN_COURS when user modifies
            ps.setInt(3, reclamation.getId());
            
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                // Update the object status if database update was successful
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
            ps.setString(2, "RESOLUE");  // Update status when admin replies
            ps.setInt(3, reclamation.getId());
            ps.executeUpdate();
            
            // Update the reclamation object to reflect changes
            reclamation.setAdminReply(response);
            reclamation.setStatus("RESOLUE");
        }
    }

} 