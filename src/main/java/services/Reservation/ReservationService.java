package services.Reservation;

import utils.DatabaseConnection;
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
        conn = DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Crée une nouvelle réservation dans la base de données
     * @param annonceId ID de l'annonce concernée
     * @param userId ID de l'utilisateur qui fait la réservation
     * @param status Statut de la réservation
     * @return La réservation créée
     * @throws SQLException En cas d'erreur SQL
     */
    public Reservation createReservation(int annonceId, int userId, String status) throws SQLException {
        // Conversion des int en Long
        Long longAnnonceId = Long.valueOf(annonceId);
        Long longUserId = Long.valueOf(userId);
        Long newId = getNextId(); // Assurez-vous que cette méthode retourne un Long

        Reservation reservation = new Reservation(
            newId,
            longAnnonceId,
            longUserId,
            new Timestamp(System.currentTimeMillis()),
            status
        );

        String sql = "INSERT INTO reservation (annonce_id, user_id, date_reservation, status) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setLong(1, longAnnonceId);
            pst.setLong(2, longUserId);
            pst.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            pst.setString(4, status);
            
            pst.executeUpdate();
        }
        return reservation;
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
                    reservation.setId(Long.valueOf(rs.getInt("id")));
                    reservation.setAnnonceId(Long.valueOf(rs.getInt("annonce_id")));
                    reservation.setUserId(Long.valueOf(rs.getInt("user_id")));
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
    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM reservation WHERE id = ?";
        
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setLong(1, id);
            pst.executeUpdate();
        }
    }

    /**
     * Met à jour le statut d'une réservation
     * @param id ID de la réservation
     * @param status Nouveau statut
     * @throws SQLException En cas d'erreur lors de la mise à jour
     */
    public void updateStatus(Long id, String status) throws SQLException {
        String sql = "UPDATE reservation SET status = ? WHERE id = ?";
        
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, status);
            pst.setLong(2, id);
            pst.executeUpdate();
        }
    }

    private Long getNextId() {
        // Si vous utilisez une requête SQL pour obtenir le prochain ID
        String query = "SELECT MAX(id) FROM reservation";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getLong(1) + 1;
            }
            return 1L; // Si aucune réservation n'existe encore
        } catch (SQLException e) {
            // Gérer l'exception
            throw new RuntimeException("Erreur lors de la génération du prochain ID", e);
        }
    }
} 