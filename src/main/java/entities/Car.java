package entities;

import java.util.Date;

public class Car {
    private int id;
    private String plaqueImatriculation;
    private int marqueId;
    private int modelId;
    private String type;
    private Date dateImatriculation;
    private String couleur;

    public Car(int id, String plaqueImatriculation, int marqueId, int modelId, String type, Date dateImatriculation, String couleur) {
        this.id = id;
        this.plaqueImatriculation = plaqueImatriculation;
        this.marqueId = marqueId;
        this.modelId = modelId;
        this.type = type;
        this.dateImatriculation = dateImatriculation;
        this.couleur = couleur;
    }

    public Car(String plaqueImatriculation, int marqueId, int modelId, String type, Date dateImatriculation, String couleur) {
        this.plaqueImatriculation = plaqueImatriculation;
        this.marqueId = marqueId;
        this.modelId = modelId;
        this.type = type;
        this.dateImatriculation = dateImatriculation;
        this.couleur = couleur;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getPlaqueImatriculation() { return plaqueImatriculation; }
    public void setPlaqueImatriculation(String plaqueImatriculation) { this.plaqueImatriculation = plaqueImatriculation; }

    public int getMarqueId() { return marqueId; }
    public void setMarqueId(int marqueId) { this.marqueId = marqueId; }

    public int getModelId() { return modelId; }
    public void setModelId(int modelId) { this.modelId = modelId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Date getDateImatriculation() { return dateImatriculation; }
    public void setDateImatriculation(Date dateImatriculation) { this.dateImatriculation = dateImatriculation; }

    public String getCouleur() { return couleur; }
    public void setCouleur(String couleur) { this.couleur = couleur; }

    @Override
    public String toString() {
        return "Car{id=" + id +
                ", plaqueImatriculation='" + plaqueImatriculation +
                ", marqueId=" + marqueId +
                ", modelId=" + modelId +
                ", type='" + type +
                ", dateImatriculation=" + dateImatriculation +
                ", couleur='" + couleur +  +
                '}';
    }
}
