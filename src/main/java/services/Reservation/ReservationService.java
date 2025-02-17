package services.Reservation;

import utils.MyConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import entities.Reservation.Reservation;

/**
 * Service gérant les opérations CRUD pour les réservations
 * Cette classe gère toutes les interactions avec la base de données pour les réservations
 */
public class ReservationService {
    private Connection conn;  // Connexion à la base de données

    /**
     * Constructeur initialisant la connexion à la base de données
     */
    public ReservationService() {
        conn = MyConnection.getInstance().getConnection();
    }

    /**
     * Crée une nouvelle réservation dans la base de données
     * @param annonceId ID de l'annonce concernée
     * @param userId ID de l'utilisateur qui fait la réservation
     * @throws SQLException En cas d'erreur SQL
     */
    public void create(int annonceId, int userId) throws SQLException {
        String sql = "INSERT INTO reservation (annonce_id, user_id, date_reservation, status) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, annonceId);
            pst.setInt(2, userId);
            pst.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            pst.setString(4, "PENDING"); // Statut initial
            
            pst.executeUpdate();
        }
    }

    /**
     * Récupère toutes les réservations d'un utilisateur
     * @param userId ID de l'utilisateur
     * @return Liste des réservations de l'utilisateur
     * @throws SQLException En cas d'erreur SQL
     */
    public List<Reservation> getReservationsByUserId(int userId) throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservation WHERE user_id = ? " +
                     "ORDER BY FIELD(status, 'PENDING', 'CANCELLED') ASC, " + // PENDING en premier, puis CANCELLED
                     "date_reservation DESC";
        
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, userId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Reservation reservation = new Reservation();
                    reservation.setId(rs.getInt("id"));
                    reservation.setAnnonceId(rs.getInt("annonce_id"));
                    reservation.setUserId(rs.getInt("user_id"));
                    reservation.setDateReservation(rs.getTimestamp("date_reservation"));
                    reservation.setStatus(rs.getString("status"));
                    reservations.add(reservation);
                }
            }
        }
        return reservations;
    }

    /**
     * Supprime une réservation de la base de données
     * @param id ID de la réservation à supprimer
     * @throws SQLException En cas d'erreur lors de la suppression
     */
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM reservation WHERE id = ?";
        
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }

    /**
     * Met à jour le statut d'une réservation
     * @param id ID de la réservation
     * @param status Nouveau statut
     * @throws SQLException En cas d'erreur lors de la mise à jour
     */
    public void updateStatus(int id, String status) throws SQLException {
        String sql = "UPDATE reservation SET status = ? WHERE id = ?";
        
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, status);
            pst.setInt(2, id);
            pst.executeUpdate();
        }
    }
} 