package entities;

import java.util.Date;

public class Avis {
    private int id;
    private int rating; // Rating from 1 to 5 stars
    private User passager; // The passenger who made the avis
    private User conducteur; // The driver (as a User) who receives the avis
    private Date date; // Date of the avis

    public Avis(int id, int rating, User passager, User conducteur, Date date) {
        this.id = id;
        this.rating = rating;
        this.passager = passager;
        this.conducteur = conducteur;
        this.date = date;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        this.rating = rating;
    }

    public User getPassager() {
        return passager;
    }

    public void setPassager(User passager) {
        this.passager = passager;
    }

    public User getConducteur() {
        return conducteur;
    }

    public void setConducteur(User conducteur) {
        this.conducteur = conducteur;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    // Add helper method to display stars
    public String getStarRating() {
        return "★".repeat(rating) + "☆".repeat(5 - rating);
    }
} 