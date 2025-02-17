package entities.Trajet;

import java.sql.Timestamp;

/**
 * Classe représentant un trajet dans le système de covoiturage
 * Cette classe gère les informations détaillées d'un trajet proposé
 * incluant les points de départ/arrivée, horaires et tarifs
 */
public class Trajet {
    // Attributs de base
    private int id;                    // Identifiant unique du trajet
    private String titre;              // Titre descriptif du trajet
    private String departurePoint;     // Point de départ du trajet
    private String arrivalPoint;       // Point d'arrivée du trajet
    private Timestamp departureDate;   // Date et heure de départ prévues
    private int availableSeats;        // Nombre de places disponibles dans le véhicule
    private double price;              // Prix par passager en DT
    private Timestamp createdAt;       // Date de création du trajet dans le système

    /**
     * Constructeur par défaut
     * Initialise un nouveau trajet sans paramètres
     */
    public Trajet() {
    }

    /**
     * Constructeur avec tous les paramètres
     * Crée un nouveau trajet avec toutes les informations nécessaires
     * 
     * @param id Identifiant unique du trajet
     * @param titre Titre descriptif du trajet
     * @param departurePoint Lieu de départ
     * @param arrivalPoint Lieu d'arrivée
     * @param departureDate Date et heure de départ
     * @param availableSeats Nombre de places disponibles
     * @param price Prix par passager
     * @param createdAt Date de création de l'entrée
     */
    public Trajet(int id, String titre, String departurePoint, String arrivalPoint, 
                 Timestamp departureDate, int availableSeats, double price, Timestamp createdAt) {
        this.id = id;
        this.titre = titre;
        this.departurePoint = departurePoint;
        this.arrivalPoint = arrivalPoint;
        this.departureDate = departureDate;
        this.availableSeats = availableSeats;
        this.price = price;
        this.createdAt = createdAt;
    }

    // Getters et Setters
    /**
     * Récupère l'identifiant du trajet
     * @return ID unique du trajet
     */
    public int getId() {
        return id;
    }

    /**
     * Définit l'identifiant du trajet
     * @param id Nouvel identifiant
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Récupère le titre du trajet
     * @return Titre descriptif du trajet
     */
    public String getTitre() {
        return titre;
    }

    /**
     * Définit le titre du trajet
     * @param titre Nouveau titre
     */
    public void setTitre(String titre) {
        this.titre = titre;
    }

    /**
     * Récupère le point de départ
     * @return Lieu de départ du trajet
     */
    public String getDeparturePoint() {
        return departurePoint;
    }

    /**
     * Définit le point de départ
     * @param departurePoint Nouveau lieu de départ
     */
    public void setDeparturePoint(String departurePoint) {
        this.departurePoint = departurePoint;
    }

    /**
     * Récupère le point d'arrivée
     * @return Lieu d'arrivée du trajet
     */
    public String getArrivalPoint() {
        return arrivalPoint;
    }

    /**
     * Définit le point d'arrivée
     * @param arrivalPoint Nouveau lieu d'arrivée
     */
    public void setArrivalPoint(String arrivalPoint) {
        this.arrivalPoint = arrivalPoint;
    }

    /**
     * Récupère la date et l'heure de départ
     * @return Date et heure prévues pour le départ
     */
    public Timestamp getDepartureDate() {
        return departureDate;
    }

    /**
     * Définit la date et l'heure de départ
     * @param departureDate Nouvelle date et heure de départ
     */
    public void setDepartureDate(Timestamp departureDate) {
        this.departureDate = departureDate;
    }

    /**
     * Récupère le nombre de places disponibles
     * @return Nombre de places restantes
     */
    public int getAvailableSeats() {
        return availableSeats;
    }

    /**
     * Définit le nombre de places disponibles
     * @param availableSeats Nouveau nombre de places
     */
    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    /**
     * Récupère le prix du trajet
     * @return Prix par passager en DT
     */
    public double getPrice() {
        return price;
    }

    /**
     * Définit le prix du trajet
     * @param price Nouveau prix par passager
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Récupère la date de création
     * @return Date de création du trajet dans le système
     */
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    /**
     * Définit la date de création
     * @param createdAt Nouvelle date de création
     */
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Retourne une représentation textuelle du trajet
     * Inclut toutes les informations importantes du trajet
     * @return String formatée avec les détails du trajet
     */
    @Override
    public String toString() {
        return "Trajet{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", departurePoint='" + departurePoint + '\'' +
                ", arrivalPoint='" + arrivalPoint + '\'' +
                ", departureDate=" + departureDate +
                ", availableSeats=" + availableSeats +
                ", price=" + price +
                ", createdAt=" + createdAt +
                '}';
    }
}
