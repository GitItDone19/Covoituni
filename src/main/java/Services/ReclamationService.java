package Services;

import entities.Reclamation;
import utils.MyConnection;
import java.sql.*;
import java.util.ArrayList;

public class ReclamationService implements IServices<Reclamation> {
    private Connection connection;

    public ReclamationService() {
        this.connection = MyConnection.getInstance().getCnx();
    }

    @Override
    public void create(Reclamation reclamation) throws SQLException {
        String sql = "INSERT INTO reclamation (description, status, date_reclamation, user_id) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, reclamation.getDescription());
            ps.setString(2, reclamation.getStatus());
            ps.setDate(3, reclamation.getDateReclamation());
            ps.setInt(4, reclamation.getUserId());
            ps.executeUpdate();
        }
    }

    @Override
    public void update(Reclamation reclamation) throws SQLException {
        String sql = "UPDATE reclamation SET description=?, status=?, date_reclamation=?, user_id=? WHERE id=?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, reclamation.getDescription());
            ps.setString(2, reclamation.getStatus());
            ps.setDate(3, reclamation.getDateReclamation());
            ps.setInt(4, reclamation.getUserId());
            ps.setInt(5, reclamation.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Reclamation reclamation) throws SQLException {
        String sql = "DELETE FROM reclamation WHERE id=?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, reclamation.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public ArrayList<Reclamation> readAll() throws SQLException {
        ArrayList<Reclamation> reclamations = new ArrayList<>();
        String sql = "SELECT * FROM reclamation ORDER BY date_reclamation DESC";
        
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                Reclamation reclamation = new Reclamation(
                    rs.getInt("id"),
                    rs.getString("description"),
                    rs.getString("status"),
                    rs.getDate("date_reclamation"),
                    rs.getInt("user_id")
                );
                reclamations.add(reclamation);
            }
        }
        return reclamations;
    }

    public ArrayList<Reclamation> findByUserId(int userId) throws SQLException {
        ArrayList<Reclamation> reclamations = new ArrayList<>();
        String sql = "SELECT * FROM reclamation WHERE user_id=? ORDER BY date_reclamation DESC";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Reclamation reclamation = new Reclamation(
                        rs.getInt("id"),
                        rs.getString("description"),
                        rs.getString("status"),
                        rs.getDate("date_reclamation"),
                        rs.getInt("user_id")
                    );
                    reclamations.add(reclamation);
                }
            }
        }
        return reclamations;
    }

    public void updateStatus(int reclamationId, String newStatus) throws SQLException {
        String sql = "UPDATE reclamation SET status=? WHERE id=?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, reclamationId);
            ps.executeUpdate();
        }
    }
} 