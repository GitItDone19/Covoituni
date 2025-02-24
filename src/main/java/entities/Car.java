package entities;

import java.util.Date;

public class Car {
    private int id;
    private String plaqueImatriculation;
    private String description;
    private Date dateImatriculation;
    private String couleur;
    private String marque;
    private String modele;
    private int categorieId;

    // Constructor with id and category
    public Car(int id, String plaqueImatriculation, String description, Date dateImatriculation, 
               String couleur, String marque, String modele, int categorieId) {
        this.id = id;
        this.plaqueImatriculation = plaqueImatriculation;
        this.description = description;
        this.dateImatriculation = dateImatriculation;
        this.couleur = couleur;
        this.marque = marque;
        this.modele = modele;
        this.categorieId = categorieId;
    }

    // Constructor without id but with category (for new cars)
    public Car(String plaqueImatriculation, String description, Date dateImatriculation, 
               String couleur, String marque, String modele, int categorieId) {
        this.plaqueImatriculation = plaqueImatriculation;
        this.description = description;
        this.dateImatriculation = dateImatriculation;
        this.couleur = couleur;
        this.marque = marque;
        this.modele = modele;
        this.categorieId = categorieId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlaqueImatriculation() {
        return plaqueImatriculation;
    }

    public void setPlaqueImatriculation(String plaqueImatriculation) {
        this.plaqueImatriculation = plaqueImatriculation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateImatriculation() {
        return dateImatriculation;
    }

    public void setDateImatriculation(Date dateImatriculation) {
        this.dateImatriculation = dateImatriculation;
    }

    public String getCouleur() {
        return couleur;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public int getCategorieId() {
        return categorieId;
    }

    public void setCategorieId(int categorieId) {
        this.categorieId = categorieId;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", plaqueImatriculation='" + plaqueImatriculation + '\'' +
                ", description='" + description + '\'' +
                ", dateImatriculation=" + dateImatriculation +
                ", couleur='" + couleur + '\'' +
                ", marque='" + marque + '\'' +
                ", modele='" + modele + '\'' +
                ", categorieId=" + categorieId +
                '}';
    }
}
