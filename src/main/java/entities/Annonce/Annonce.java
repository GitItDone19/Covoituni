package entities.Annonce;

import java.sql.Timestamp;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import entities.Reservation.Reservation;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/**
 * Classe représentant une annonce de covoiturage
 * Cette classe gère les informations d'une annonce publiée par un conducteur
 */
@Entity
public class Annonce {
    // Attributs de base
    @Id
    private Long id;                    // Identifiant unique de l'annonce
    private String titre;              // Titre de l'annonce
    private String description;        // Description détaillée de l'annonce
    private Timestamp datePublication; // Date et heure de publication
    private int driverId;             // ID du conducteur qui publie l'annonce
    private int carId;                // ID du véhicule utilisé
    private String status;            // Statut de l'annonce (OPEN/CLOSED)
    private Integer trajetId;          // ID du trajet associé à l'annonce (peut être null)

    @OneToMany(mappedBy = "annonce", cascade = CascadeType.REMOVE)
    private List<Reservation> reservations;

    /**
     * Constructeur par défaut
     */
    public Annonce() {
        this.status = "OPEN";
        this.datePublication = new Timestamp(System.currentTimeMillis());
    }

    /**
     * Constructeur avec tous les paramètres
     * @param id Identifiant unique
     * @param titre Titre de l'annonce
     * @param description Description détaillée
     * @param datePublication Date de publication
     * @param driverId ID du conducteur
     * @param carId ID du véhicule
     * @param status Statut de l'annonce
     * @param trajetId ID du trajet
     */
    public Annonce(Long id, String titre, String description, Timestamp datePublication, 
                  int driverId, int carId, String status, Integer trajetId) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.datePublication = datePublication;
        this.driverId = driverId;
        this.carId = carId;
        this.status = status;
        this.trajetId = trajetId;
    }

    // Getters et Setters
    /**
     * Récupère l'ID de l'annonce
     * @return ID de l'annonce
     */
    public Long getId() {
        return id;
    }

    /**
     * Définit l'ID de l'annonce
     * @param id Nouvel ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Récupère le titre de l'annonce
     * @return Titre de l'annonce
     */
    public String getTitre() {
        return titre;
    }

    /**
     * Définit le titre de l'annonce
     * @param titre Nouveau titre
     */
    public void setTitre(String titre) {
        this.titre = titre;
    }

    /**
     * Récupère la description de l'annonce
     * @return Description de l'annonce
     */
    public String getDescription() {
        return description;
    }

    /**
     * Définit la description de l'annonce
     * @param description Nouvelle description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Récupère la date de publication
     * @return Date de publication
     */
    public Timestamp getDatePublication() {
        return datePublication;
    }

    /**
     * Définit la date de publication
     * @param datePublication Nouvelle date de publication
     */
    public void setDatePublication(Timestamp datePublication) {
        this.datePublication = datePublication;
    }

    /**
     * Récupère l'ID du conducteur
     * @return ID du conducteur
     */
    public int getDriverId() {
        return driverId;
    }

    /**
     * Définit l'ID du conducteur
     * @param driverId Nouvel ID du conducteur
     */
    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    /**
     * Récupère l'ID du véhicule
     * @return ID du véhicule
     */
    public int getCarId() {
        return carId;
    }

    /**
     * Définit l'ID du véhicule
     * @param carId Nouvel ID du véhicule
     */
    public void setCarId(int carId) {
        this.carId = carId;
    }

    /**
     * Récupère le statut de l'annonce
     * @return Statut actuel (OPEN/CLOSED)
     */
    public String getStatus() {
        return status;
    }

    /**
     * Définit le statut de l'annonce
     * @param status Nouveau statut
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Récupère l'ID du trajet associé
     * @return ID du trajet
     */
    public Integer getTrajetId() {
        return trajetId;
    }

    /**
     * Définit l'ID du trajet associé
     * @param trajetId Nouvel ID du trajet
     */
    public void setTrajetId(Integer trajetId) {
        this.trajetId = trajetId;
    }

    /**
     * Retourne une représentation textuelle de l'annonce
     * @return String contenant toutes les informations de l'annonce
     */
    @Override
    public String toString() {
        return "Annonce{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", datePublication=" + datePublication +
                ", driverId=" + driverId +
                ", carId=" + carId +
                ", status='" + status + '\'' +
                ", trajetId=" + trajetId +
                '}';
    }
}
