package entities.Reservation;

import java.sql.Timestamp;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import entities.Annonce.Annonce;

/**
 * Classe représentant une réservation dans le système de covoiturage.
 * Cette classe gère les informations relatives aux réservations des trajets.
 */
@Entity
public class Reservation {
    @Id
    private Long id;
    private Long annonceId;
    private Long userId;
    private Timestamp dateReservation;
    private String status; // PENDING, CONFIRMED, CANCELLED
    
    @ManyToOne
    private Annonce annonce;

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
    public Reservation(Long id, Long annonceId, Long userId, Timestamp dateReservation, String status) {
        this.id = id;
        this.annonceId = annonceId;
        this.userId = userId;
        this.dateReservation = dateReservation;
        this.status = status;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Long getAnnonceId() {
        return annonceId;
    }

    public Long getUserId() {
        return userId;
    }

    public Timestamp getDateReservation() {
        return dateReservation;
    }

    public String getStatus() {
        return status;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setAnnonceId(Long annonceId) {
        this.annonceId = annonceId;
    }

    public void setUserId(Long userId) {
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