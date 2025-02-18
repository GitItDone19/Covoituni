package repositories;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import entities.Reservation.Reservation;
import entities.Annonce.Annonce;
import utils.MyConnection;

public class ReservationRepository {
    private Connection connection;
    private AnnonceRepository annonceRepository;

    public ReservationRepository() {
        this.connection = MyConnection.getInstance().getCnx();
        this.annonceRepository = new AnnonceRepository();
    }

    public Reservation save(Reservation reservation) {
        String sql;
        if (reservation.getId() == null) {
            sql = "INSERT INTO reservation (annonce_id, user_id, date_reservation, status, comment) " +
                  "VALUES (?, ?, ?, ?, ?)";
        } else {
            sql = "UPDATE reservation SET annonce_id=?, user_id=?, date_reservation=?, status=?, comment=? " +
                  "WHERE id=?";
        }

        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setLong(1, reservation.getAnnonceId());
            pstmt.setLong(2, reservation.getUserId());
            pstmt.setTimestamp(3, reservation.getDateReservation());
            pstmt.setString(4, reservation.getStatus());
            pstmt.setString(5, reservation.getComment());
            
            if (reservation.getId() != null) {
                pstmt.setLong(6, reservation.getId());
            }

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La création de la réservation a échoué");
            }

            if (reservation.getId() == null) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        reservation.setId(generatedKeys.getLong(1));
                    } else {
                        throw new SQLException("La création a échoué, aucun ID obtenu.");
                    }
                }
            }
            
            return reservation;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Reservation findById(Long id) {
        String sql = "SELECT * FROM reservation WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToReservation(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    private Reservation mapResultSetToReservation(ResultSet rs) throws SQLException {
        Reservation reservation = new Reservation();
        reservation.setId(rs.getLong("id"));
        reservation.setAnnonceId(rs.getLong("annonce_id"));
        reservation.setUserId(rs.getLong("user_id"));
        reservation.setDateReservation(rs.getTimestamp("date_reservation"));
        reservation.setStatus(rs.getString("status"));
        reservation.setComment(rs.getString("comment"));
        return reservation;
    }

    public List<Reservation> findAll() {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservation ORDER BY date_reservation DESC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                reservations.add(mapResultSetToReservation(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return reservations;
    }

    public List<Reservation> findByUserId(Long userId) {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservation WHERE user_id = ? ORDER BY date_reservation DESC";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                reservations.add(mapResultSetToReservation(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return reservations;
    }

    public List<Reservation> findByAnnonceId(Long annonceId) {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservation WHERE annonce_id = ? ORDER BY date_reservation DESC";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, annonceId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                reservations.add(mapResultSetToReservation(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return reservations;
    }

    public boolean delete(Long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
} 