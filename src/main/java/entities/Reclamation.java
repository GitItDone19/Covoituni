package entities;

import java.util.Date;

public class Reclamation {
    
    private static int dernierNumRec = 0; // Variable statique pour garder la trace du dernier numRec utilisé
    private int numRec; // Identifiant de la réclamation
    private String titre; 
    private String sujet; 
    private String reponseRec; 
    private Date dateReclamation; 
    private String username; 

    // Constructeur
    public Reclamation(String titre, String sujet, String username) {
        this.numRec = ++dernierNumRec; // Auto-incrémentation de numRec
        this.titre = titre;
        this.sujet = sujet;
        this.reponseRec = "En cours de traitement";
        this.dateReclamation = new Date();
        this.username = username;
    }

  
    public int getNumRec() {
        return numRec;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getSujet() {
        return sujet;
    }

    public void setSujet(String sujet) {
        this.sujet = sujet;
    }

    public String getReponseRec() {
        return reponseRec;
    }

    public void setReponseRec(String reponseRec) {
        this.reponseRec = reponseRec;
    }

    public Date getDateReclamation() {
        return dateReclamation;
    }

    public void setDateReclamation(Date dateReclamation) {
        this.dateReclamation = dateReclamation;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    
    @Override
    public String toString() {
        return "Reclamation{" +
                "numRec=" + numRec +
                ", titre='" + titre + '\'' +
                ", sujet='" + sujet + '\'' +
                ", reponseRec='" + reponseRec + '\'' +
                ", dateReclamation=" + dateReclamation +
                ", username='" + username + '\'' +
                '}';
    }

 
    public static void main(String[] args) {
      
        Reclamation rec1 = new Reclamation("Probleme de temps", "Le conducteur arrive en retard", "Rouge007");
        System.out.println(rec1);


        Reclamation rec2 = new Reclamation("Probleme sanitaire", "Le conducteur fume dans la voiture",  "Cheikh408");
        System.out.println(rec2);
    }
}
