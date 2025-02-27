package main.entities;

import java.time.LocalDateTime;

public class Trajet {
    private int id;
    private String titre;
    private String departurePoint;
    private String arrivalPoint;
    private LocalDateTime departureDate;
    private double price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Trajet() {
    }

    public Trajet(String titre, String departurePoint, String arrivalPoint, LocalDateTime departureDate, double price) {
        this.titre = titre;
        this.departurePoint = departurePoint;
        this.arrivalPoint = arrivalPoint;
        this.departureDate = departureDate;
        this.price = price;
    }

    // Getters
    public int getId() { return id; }
    public String getTitre() { return titre; }
    public String getDeparturePoint() { return departurePoint; }
    public String getArrivalPoint() { return arrivalPoint; }
    public LocalDateTime getDepartureDate() { return departureDate; }
    public double getPrice() { return price; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setTitre(String titre) { this.titre = titre; }
    public void setDeparturePoint(String departurePoint) { this.departurePoint = departurePoint; }
    public void setArrivalPoint(String arrivalPoint) { this.arrivalPoint = arrivalPoint; }
    public void setDepartureDate(LocalDateTime departureDate) { this.departureDate = departureDate; }
    public void setPrice(double price) { this.price = price; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "Trajet{" +
                "titre='" + titre + '\'' +
                ", de='" + departurePoint + '\'' +
                ", à='" + arrivalPoint + '\'' +
                ", départ=" + departureDate +
                ", prix=" + price +
                '}';
    }
} 