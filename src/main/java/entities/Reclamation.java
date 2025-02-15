package entities;

import java.sql.Date;

public class Reclamation {
    private int id;
    private String description;
    private String status; // "EN_ATTENTE", "EN_COURS", "RESOLUE"
    private Date dateReclamation;
    private int userId; // L'utilisateur qui a fait la réclamation

    public Reclamation() {
    }

    public Reclamation(String description, String status, Date dateReclamation, int userId) {
        this.description = description;
        this.status = status;
        this.dateReclamation = dateReclamation;
        this.userId = userId;
    }

    public Reclamation(int id, String description, String status, Date dateReclamation, int userId) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.dateReclamation = dateReclamation;
        this.userId = userId;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDateReclamation() {
        return dateReclamation;
    }

    public void setDateReclamation(Date dateReclamation) {
        this.dateReclamation = dateReclamation;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Reclamation{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", dateReclamation=" + dateReclamation +
                ", userId=" + userId +
                '}';
    }
} 