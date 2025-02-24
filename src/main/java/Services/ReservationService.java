package Services;

import Services.IService;
import entities.Reservation;
import entities.Annonce;
import utils.MyConnection;
import Services.AnnonceService;
import Services.ReservationService;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReservationService implements IService<Reservation> {
    private Connection cnx;

    public ReservationService() {
        cnx = MyConnection.getInstance().getCnx();
    }

    @Override
    public void create(Reservation reservation) throws SQLException {
        String sql = "INSERT INTO reservation (annonce_id, user_id, status, comment, date_reservation) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setLong(1, reservation.getAnnonceId());
            pst.setInt(2, reservation.getPassengerId());
            pst.setString(3, reservation.getStatus());
            pst.setString(4, reservation.getComment());
            pst.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            pst.executeUpdate();
        }
    }

    @Override
    public void update(Reservation reservation) throws SQLException {
        String sql = "UPDATE reservation SET status = ?, comment = ? WHERE id = ?";
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setString(1, reservation.getStatus());
            pst.setString(2, reservation.getComment());
            pst.setLong(3, reservation.getId());
            pst.executeUpdate();
        }
    }

    @Override
    public void delete(Reservation reservation) throws SQLException {
        String sql = "DELETE FROM reservation WHERE id = ?";
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setLong(1, reservation.getId());
            pst.executeUpdate();
        }
    }

    @Override
    public List<Reservation> readAll() throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservation ORDER BY date_reservation DESC";
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Reservation r = new Reservation();
                r.setId(rs.getLong("id"));
                r.setAnnonceId(rs.getLong("annonce_id"));
                r.setPassengerId(rs.getInt("user_id"));
                r.setStatus(rs.getString("status"));
                r.setComment(rs.getString("comment"));
                r.setDateReservation(rs.getTimestamp("date_reservation").toLocalDateTime());
                reservations.add(r);
            }
        }
        return reservations;
    }

    // Méthodes supplémentaires spécifiques aux réservations
    public List<Reservation> readByAnnonceId(long annonceId) throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservation WHERE annonce_id = ? ORDER BY date_reservation DESC";
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setLong(1, annonceId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Reservation r = new Reservation();
                    r.setId(rs.getLong("id"));
                    r.setAnnonceId(rs.getLong("annonce_id"));
                    r.setPassengerId(rs.getInt("user_id"));
                    r.setStatus(rs.getString("status"));
                    r.setComment(rs.getString("comment"));
                    r.setDateReservation(rs.getTimestamp("date_reservation").toLocalDateTime());
                    reservations.add(r);
                }
            }
        }
        return reservations;
    }

    public List<Reservation> readByPassengerId(int passengerId) throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservation WHERE user_id = ? ORDER BY date_reservation DESC";
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setInt(1, passengerId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Reservation r = new Reservation();
                    r.setId(rs.getLong("id"));
                    r.setAnnonceId(rs.getLong("annonce_id"));
                    r.setPassengerId(rs.getInt("user_id"));
                    r.setStatus(rs.getString("status"));
                    r.setComment(rs.getString("comment"));
                    r.setDateReservation(rs.getTimestamp("date_reservation").toLocalDateTime());
                    reservations.add(r);
                }
            }
        }
        return reservations;
    }

    public List<Reservation> readByDriverId(int driverId) throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT r.* FROM reservation r " +
                     "JOIN annonce a ON r.annonce_id = a.id " +
                     "WHERE a.driver_id = ? " +
                     "ORDER BY r.date_reservation DESC";
        
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setInt(1, driverId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Reservation r = new Reservation();
                    r.setId(rs.getLong("id"));
                    r.setAnnonceId(rs.getLong("annonce_id"));
                    r.setPassengerId(rs.getInt("user_id"));
                    r.setStatus(rs.getString("status"));
                    r.setComment(rs.getString("comment"));
                    r.setDateReservation(rs.getTimestamp("date_reservation").toLocalDateTime());
                    reservations.add(r);
                }
            }
        }
        return reservations;
    }

    public void updateReservationAndSeats(Reservation reservation, String newStatus) throws SQLException {
        // Mettre à jour le statut de la réservation
        reservation.setStatus(newStatus);
        update(reservation);
        
        // Si la réservation est annulée ou refusée, remettre une place disponible
        if ("CANCELLED".equals(newStatus) || "REFUSED".equals(newStatus)) {
            AnnonceService annonceService = new AnnonceService();
            Annonce annonce = annonceService.readById(reservation.getAnnonceId());
            
            if (annonce != null) {
                // Augmenter le nombre de places disponibles
                int newSeats = annonce.getAvailableSeats() + 1;
                annonce.setAvailableSeats(newSeats);
                annonceService.update(annonce);
                
                // Si l'annonce était fermée et qu'il y a maintenant des places, la rouvrir
                if ("CLOSED".equals(annonce.getStatus()) && newSeats > 0) {
                    annonceService.updateStatus(annonce, "OPEN");
                }
            }
        }
    }

    public void cancelReservation(Reservation reservation) throws SQLException {
        String query = "UPDATE reservation SET status = 'CANCELLED' WHERE id = ?";
        try (PreparedStatement pstmt = cnx.prepareStatement(query)) {
            pstmt.setLong(1, reservation.getId());
            pstmt.executeUpdate();
            
            // Mettre à jour le nombre de places disponibles dans l'annonce
            AnnonceService annonceService = new AnnonceService();
            Annonce annonce = annonceService.readById(reservation.getAnnonceId());
            if (annonce != null) {
                annonce.setAvailableSeats(annonce.getAvailableSeats() + 1);
                if ("CLOSED".equals(annonce.getStatus())) {
                    annonce.setStatus("OPEN");
                }
                annonceService.update(annonce);
            }
        }
    }
} 