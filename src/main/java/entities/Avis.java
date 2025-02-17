package entities;

import java.util.Date;

public class Avis {

    private int numAvis;
    private String commentaire;
    private int note;
    private String reponseAvis;
    private Date dateAvis;
    private String username;

    public Avis(String commentaire, int note, String username) {
        this.commentaire = commentaire;
        this.note = note;
        this.reponseAvis = "En attente de modération";
        this.dateAvis = new Date();
        this.username = username;
    }

    // Constructeur complet pour la récupération depuis la base de données
    public Avis(int numAvis, String commentaire, int note, String reponseAvis, Date dateAvis, String username) {
        this.numAvis = numAvis;
        this.commentaire = commentaire;
        this.note = note;
        this.reponseAvis = reponseAvis;
        this.dateAvis = dateAvis;
        this.username = username;
    }

    // Getters et Setters
    public int getNumAvis() {
        return numAvis;
    }

    public void setNumAvis(int numAvis) {
        this.numAvis = numAvis;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }

    public String getReponseAvis() {
        return reponseAvis;
    }

    public void setReponseAvis(String reponseAvis) {
        this.reponseAvis = reponseAvis;
    }

    public Date getDateAvis() {
        return dateAvis;
    }

    public void setDateAvis(Date dateAvis) {
        this.dateAvis = dateAvis;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Avis{" +
                "numAvis=" + numAvis +
                ", commentaire='" + commentaire + '\'' +
                ", note=" + note +
                ", reponseAvis='" + reponseAvis + '\'' +
                ", dateAvis=" + dateAvis +
                ", username='" + username + '\'' +
                '}';
    }
} 