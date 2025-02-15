package entities;

import java.sql.Date;

public class Reclamation {
    private int id;
    private String description;
    private String status; // "EN_ATTENTE", "EN_COURS", "RESOLUE"
    private Date dateReclamation;
    private int userId; // L'utilisateur qui a fait la réclamation
    private String adminReply;
    private String username; // Add this field

    // Default constructor
    public Reclamation() {
    }

    // Constructor for creating new reclamation
    public Reclamation(String description, String status, int userId) {
        this.description = description;
        this.status = status;
        this.userId = userId;
        this.dateReclamation = new Date(System.currentTimeMillis());
    }

    // Constructor for database retrieval
    public Reclamation(int id, String description, String status, Date dateReclamation, int userId) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.dateReclamation = dateReclamation;
        this.userId = userId;
    }

    // Getters and Setters
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

    public String getAdminReply() {
        return adminReply;
    }

    public void setAdminReply(String adminReply) {
        this.adminReply = adminReply;
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
                "id=" + id +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", dateReclamation=" + dateReclamation +
                ", userId=" + userId +
                ", adminReply='" + adminReply + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
} 