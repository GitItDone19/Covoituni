package Services;

import entities.Annonce;
import utils.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class AnnonceService implements IService<Annonce> {
    private Connection cnx;

    public AnnonceService() {
        cnx = MyConnection.getInstance().getCnx();
    }

    @Override
    public void create(Annonce annonce) throws SQLException {
        String sql = "INSERT INTO annonce (titre, description, driver_id, car_id, available_seats, status, event_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setString(1, annonce.getTitre());
            pst.setString(2, annonce.getDescription());
            pst.setInt(3, annonce.getDriverId());
            pst.setInt(4, annonce.getCarId());
            pst.setInt(5, annonce.getAvailableSeats());
            pst.setString(6, annonce.getStatus());
            pst.setInt(7, annonce.getEventId());
            pst.executeUpdate();
        }
    }

    @Override
    public void update(Annonce annonce) throws SQLException {
        String query = "UPDATE annonce SET titre = ?, description = ?, trajet_id = ?, " +
                      "available_seats = ?, event_id = ?, status = ? WHERE id = ?";
                      
        try (PreparedStatement pstmt = cnx.prepareStatement(query)) {
            pstmt.setString(1, annonce.getTitre());
            pstmt.setString(2, annonce.getDescription());
            pstmt.setObject(3, annonce.getTrajetId());
            pstmt.setInt(4, annonce.getAvailableSeats());
            pstmt.setInt(5, annonce.getEventId());
            pstmt.setString(6, annonce.getStatus());
            pstmt.setLong(7, annonce.getId());
            
            pstmt.executeUpdate();
        }
    }

    @Override
    public void delete(Annonce annonce) throws SQLException {
        String sql = "DELETE FROM annonce WHERE id=?";
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setLong(1, annonce.getId());
            pst.executeUpdate();
        }
    }

    @Override
    public List<Annonce> readAll() throws SQLException {
        List<Annonce> annonces = new ArrayList<>();
        String sql = "SELECT * FROM annonce WHERE status IN ('OPEN', 'CLOSED') ORDER BY date_publication DESC";
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Annonce a = new Annonce();
                a.setId(rs.getLong("id"));
                a.setTitre(rs.getString("titre"));
                a.setDescription(rs.getString("description"));
                a.setDriverId(rs.getInt("driver_id"));
                a.setCarId(rs.getInt("car_id"));
                a.setAvailableSeats(rs.getInt("available_seats"));
                a.setStatus(rs.getString("status"));
                a.setDatePublication(rs.getTimestamp("date_publication").toLocalDateTime());
                if (rs.getObject("trajet_id") != null) {
                    a.setTrajetId(rs.getInt("trajet_id"));
                }
                annonces.add(a);
            }
        }
        return annonces;
    }

    public void updateStatus(Annonce annonce, String status) throws SQLException {
        String query = "UPDATE annonce SET status = ? WHERE id = ?";
        try (PreparedStatement pstmt = cnx.prepareStatement(query)) {
            pstmt.setString(1, status);
            pstmt.setLong(2, annonce.getId());
            pstmt.executeUpdate();
            annonce.setStatus(status);
        }
    }

    public void closeAnnonce(Annonce annonce) throws SQLException {
        if (annonce.getAvailableSeats() <= 0) {
            updateStatus(annonce, "CLOSED");
        }
    }

    public List<Annonce> readHistorique() throws SQLException {
        List<Annonce> annonces = new ArrayList<>();
        String sql = "SELECT * FROM annonce WHERE status = 'TERMINATED' OR status = 'CLOSED' ORDER BY date_publication DESC";
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Annonce a = new Annonce();
                a.setId(rs.getLong("id"));
                a.setTitre(rs.getString("titre"));
                a.setDescription(rs.getString("description"));
                a.setDriverId(rs.getInt("driver_id"));
                a.setCarId(rs.getInt("car_id"));
                a.setAvailableSeats(rs.getInt("available_seats"));
                a.setStatus(rs.getString("status"));
                a.setDatePublication(rs.getTimestamp("date_publication").toLocalDateTime());
                if (rs.getObject("trajet_id") != null) {
                    a.setTrajetId(rs.getInt("trajet_id"));
                }
                if (rs.getTimestamp("date_termination") != null) {
                    a.setDateTermination(rs.getTimestamp("date_termination").toLocalDateTime());
                }
                annonces.add(a);
            }
        }
        return annonces;
    }

    public Annonce readById(long id) throws SQLException {
        String sql = "SELECT * FROM annonce WHERE id = ?";
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setLong(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Annonce a = new Annonce();
                    a.setId(rs.getLong("id"));
                    a.setTitre(rs.getString("titre"));
                    a.setDescription(rs.getString("description"));
                    a.setDriverId(rs.getInt("driver_id"));
                    a.setCarId(rs.getInt("car_id"));
                    a.setAvailableSeats(rs.getInt("available_seats"));
                    a.setStatus(rs.getString("status"));
                    a.setDatePublication(rs.getTimestamp("date_publication").toLocalDateTime());
                    if (rs.getObject("trajet_id") != null) {
                        a.setTrajetId(rs.getInt("trajet_id"));
                    }
                    return a;
                }
            }
        }
        return null;
    }

    public void reopenIfAvailable(Annonce annonce) throws SQLException {
        if ("CLOSED".equals(annonce.getStatus()) && annonce.getAvailableSeats() > 0) {
            updateStatus(annonce, "OPEN");
        }
    }

    public List<Annonce> readByEventId(int eventId) throws SQLException {
        List<Annonce> annonces = new ArrayList<>();
        String sql = "SELECT * FROM annonce WHERE event_id = ?";
        
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setInt(1, eventId);
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                Annonce a = new Annonce();
                a.setId(rs.getLong("id"));
                a.setTitre(rs.getString("titre"));
                a.setDescription(rs.getString("description"));
                a.setDriverId(rs.getInt("driver_id"));
                a.setCarId(rs.getInt("car_id"));
                a.setAvailableSeats(rs.getInt("available_seats"));
                a.setStatus(rs.getString("status"));
                a.setEventId(rs.getInt("event_id"));
                if (rs.getObject("trajet_id") != null) {
                    a.setTrajetId(rs.getInt("trajet_id"));
                }
                annonces.add(a);
            }
        }
        
        return annonces;
    }
} 