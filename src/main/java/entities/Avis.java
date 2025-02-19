package entities;

import java.util.Date;

public class Avis {
    private int id;
    private String commentaire;
    private int rating; // 1-5 stars
    private Date dateAvis;
    private User user;
    private String reponseAvis;

    // Default constructor
    public Avis() {
        this.dateAvis = new Date(); // Initialize date in default constructor
    }

    public Avis(String commentaire, int rating, User user) {
        this.commentaire = commentaire;
        this.rating = rating;
        this.user = user;
        this.dateAvis = new Date(); // Initialize date when creating new Avis
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Date getDateAvis() {
        return dateAvis;
    }

    public void setDateAvis(Date dateAvis) {
        this.dateAvis = dateAvis;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getReponseAvis() {
        return reponseAvis;
    }

    public void setReponseAvis(String reponseAvis) {
        this.reponseAvis = reponseAvis;
    }

    @Override
    public String toString() {
        return "Avis{" +
                "id=" + id +
                ", commentaire='" + commentaire + '\'' +
                ", rating=" + rating +
                ", dateAvis=" + dateAvis +
                ", user=" + user +
                ", reponseAvis='" + reponseAvis + '\'' +
                '}';
    }
} 