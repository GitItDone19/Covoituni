//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

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
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAnnonceId() {
        return this.annonceId;
    }

    public void setAnnonceId(int annonceId) {
        this.annonceId = annonceId;
    }

    public int getAvailableSeats() {
        return this.availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String toString() {
        return "Trajet{id=" + this.id + ", annonceId=" + this.annonceId + ", availableSeats=" + this.availableSeats + ", price=" + this.price + ", createdAt=" + this.createdAt + "}";
    }
}
