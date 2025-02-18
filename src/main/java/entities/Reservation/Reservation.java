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
    
    @ManyToOne
    private Annonce annonce;
    
    private Long userId;
    private Timestamp dateReservation;
    private String status; // "PENDING", "CONFIRMED", "CANCELLED"
    private String comment;
    private Long annonceId;
    
    public Reservation() {
        this.dateReservation = new Timestamp(System.currentTimeMillis());
        this.status = "PENDING";
    }
    
    public Reservation(Annonce annonce, String passager, Long userId) {
        this();
        this.annonce = annonce;
        this.userId = userId;
    }
    
    // Getters
    public Long getId() {
        return id;
    }
    
    public Annonce getAnnonce() {
        return annonce;
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
    
    public String getComment() {
        return comment;
    }
    
    // Setters
    public void setId(Long id) {
        this.id = id;
    }
    
    public void setAnnonce(Annonce annonce) {
        this.annonce = annonce;
        if (annonce != null) {
            this.annonceId = Long.valueOf(annonce.getId());
        }
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
    
    public void setComment(String comment) {
        this.comment = comment;
    }
    
    public void setAnnonceId(Long annonceId) {
        this.annonceId = annonceId;
    }
    
    /**
     * Méthode toString pour l'affichage des informations de la réservation
     * @return String représentant la réservation
     */
    @Override
    public String toString() {
        if (annonce != null) {
            return "Réservation du " + dateReservation + 
                   " (Statut: " + status + ")";
        }
        return "Réservation " + id + " (Statut: " + status + ")";
    }
} 