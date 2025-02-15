package Services;

import entities.Reclamation;
import utils.MyConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReclamationService implements IReclamationService {
    private Connection cnx;

    public ReclamationService() {
        cnx = MyConnection.getInstance().getConnection();
    }

    @Override
    public void create(Reclamation reclamation) throws SQLException {
        String query = "INSERT INTO reclamation(description, status, date_reclamation, user_id) " +
                "VALUES(?, ?, ?, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setString(1, reclamation.getDescription());
            ps.setString(2, reclamation.getStatus());
            ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            ps.setInt(4, reclamation.getUserId());
            ps.executeUpdate();
        }
    }

    @Override
    public void update(Reclamation reclamation) throws SQLException {
        String query = "UPDATE reclamation SET status = ?, admin_reply = ? WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setString(1, reclamation.getStatus());
            ps.setString(2, reclamation.getAdminReply());
            ps.setInt(3, reclamation.getId());
            ps.executeUpdate();
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

    @Override
    public List<Reclamation> readAll() throws SQLException {
        List<Reclamation> reclamations = new ArrayList<>();
        String query = "SELECT r.*, u.email as username " +
                      "FROM reclamation r " +
                      "LEFT JOIN users u ON r.user_id = u.id " +
                      "ORDER BY r.date_reclamation DESC";
        
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            
            while (rs.next()) {
                Reclamation r = new Reclamation(
                    rs.getInt("id"),
                    rs.getString("description"),
                    rs.getString("status"),
                    rs.getDate("date_reclamation"),
                    rs.getInt("user_id")
                );
                r.setAdminReply(rs.getString("admin_reply"));
                r.setUsername(rs.getString("username"));
                reclamations.add(r);
            }
        }
        return reclamations;
    }

    @Override
    public List<Reclamation> getReclamationsByStatus(String status) throws SQLException {
        List<Reclamation> reclamations = new ArrayList<>();
        String query = "SELECT r.*, u.email as username " +
                      "FROM reclamation r " +
                      "LEFT JOIN users u ON r.user_id = u.id " +
                      "WHERE r.status = ? " +
                      "ORDER BY r.date_reclamation DESC";
        
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Reclamation r = new Reclamation(
                    rs.getInt("id"),
                    rs.getString("description"),
                    rs.getString("status"),
                    rs.getDate("date_reclamation"),
                    rs.getInt("user_id")
                );
                r.setAdminReply(rs.getString("admin_reply"));
                r.setUsername(rs.getString("username"));
                reclamations.add(r);
            }
        }
        return reclamations;
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