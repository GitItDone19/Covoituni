package entities;

import java.sql.Date;

public class Trajet {
    private int id;
    private int annonceId;
    private int availableSeats;
    private double price;
    private Date createdAt;

    public Trajet() {
    }

    public Trajet(int id, int annonceId, int availableSeats, double price, Date createdAt) {
        this.id = id;
        this.annonceId = annonceId;
        this.availableSeats = availableSeats;
        this.price = price;
        this.createdAt = createdAt;
    }

    public Trajet(int annonceId, int availableSeats, double price, Date createdAt) {
        this.annonceId = annonceId;
        this.availableSeats = availableSeats;
        this.price = price;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAnnonceId() {
        return annonceId;
    }

    public void setAnnonceId(int annonceId) {
        this.annonceId = annonceId;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Trajet{" +
                "id=" + id +
                ", annonceId=" + annonceId +
                ", availableSeats=" + availableSeats +
                ", price=" + price +
                ", createdAt=" + createdAt +
                '}';
    }
}
