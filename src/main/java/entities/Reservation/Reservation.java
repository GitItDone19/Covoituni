package entities.Reservation;

import java.sql.Timestamp;

/**
 * Classe représentant une réservation dans le système de covoiturage.
 * Cette classe gère les informations relatives aux réservations des trajets.
 */
public class Reservation {
    // Attributs de la classe
    private int id;                  // Identifiant unique de la réservation
    private int annonceId;          // ID de l'annonce associée
    private int userId;             // ID de l'utilisateur qui fait la réservation
    private Timestamp dateReservation; // Date et heure de la réservation
    private String status;          // Statut de la réservation (PENDING, CONFIRMED, CANCELLED)

    /**
     * Constructeur par défaut
     */
    public Reservation() {
    }

    /**
     * Constructeur avec paramètres
     * @param id Identifiant de la réservation
     * @param annonceId ID de l'annonce
     * @param userId ID de l'utilisateur
     * @param dateReservation Date de la réservation
     * @param status Statut de la réservation
     */
    public Reservation(int id, int annonceId, int userId, Timestamp dateReservation, String status) {
        this.id = id;
        this.annonceId = annonceId;
        this.userId = userId;
        this.dateReservation = dateReservation;
        this.status = status;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getAnnonceId() {
        return annonceId;
    }

    public int getUserId() {
        return userId;
    }

    public Timestamp getDateReservation() {
        return dateReservation;
    }

    public String getStatus() {
        return status;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setAnnonceId(int annonceId) {
        this.annonceId = annonceId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setDateReservation(Timestamp dateReservation) {
        this.dateReservation = dateReservation;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Méthode toString pour l'affichage des informations de la réservation
     * @return String représentant la réservation
     */
    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", annonceId=" + annonceId +
                ", userId=" + userId +
                ", dateReservation=" + dateReservation +
                ", status='" + status + '\'' +
                '}';
    }
} 