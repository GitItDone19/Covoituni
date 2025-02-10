package entities;

import Services.IServices;

import java.util.Date;

public class Reclamation  {
    private int numRec; // Sera rempli par la base de données (AUTO_INCREMENT)
    private String titre;
    private String sujet;
    private String reponseRec;
    private Date dateReclamation;
    private String username;

    // Constructeur
    public Reclamation(String titre, String sujet, String username) {
        this.titre = titre;
        this.sujet = sujet;
        this.username = username;
        this.reponseRec = "En cours de traitement"; // Valeur par défaut
        this.dateReclamation = new Date(); // Peut être remplacé par la date de la base
    }

    // Getters & Setters
    public int getNumRec() { return numRec; }
    public void setNumRec(int numRec) { this.numRec = numRec; }
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public String getSujet() { return sujet; }
    public void setSujet(String sujet) { this.sujet = sujet; }
    public String getReponseRec() { return reponseRec; }
    public void setReponseRec(String reponseRec) { this.reponseRec = reponseRec; }
    public Date getDateReclamation() { return dateReclamation; }
    public void setDateReclamation(Date dateReclamation) { this.dateReclamation = dateReclamation; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    @Override
    public String toString() {
        return "Reclamation [numRec=" + numRec + ", titre=" + titre + ", sujet=" + sujet
                + ", reponseRec=" + reponseRec + ", dateReclamation=" + dateReclamation
                + ", username=" + username + "]";
    }
}