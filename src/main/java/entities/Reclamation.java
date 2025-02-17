package entities;

import java.util.Date;

public class Reclamation {
    private int id;
    private String description;
    private String status; // "EN_ATTENTE", "EN_COURS", "RESOLUE"
    private java.sql.Date date;
    private User user;  // Changed from userId and username to User object
    private String adminReply;

    // Default constructor
    public Reclamation() {
        this.date = new java.sql.Date(System.currentTimeMillis());
    }

    // Constructor for creating new reclamation
    public Reclamation(String description, String status, User user) {
        this.description = description;
        this.status = status;
        this.user = user;
        this.date = new java.sql.Date(System.currentTimeMillis());
    }

    // Constructor for database retrieval
    public Reclamation(int id, String description, String status, Date date, User user) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.date = new java.sql.Date(date.getTime());
        this.user = user;
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

    public java.sql.Date getDate() {
        return date;
    }

    public void setDate(java.sql.Date date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Convenience methods to get user details
    public int getUserId() {
        return user != null ? user.getId() : 0;
    }

    public String getUsername() {
        return user != null ? user.getNom() + " " + user.getPrenom() : "";
    }

    public String getAdminReply() {
        return adminReply;
    }

    public void setAdminReply(String adminReply) {
        this.adminReply = adminReply;
    }

    @Override
    public String toString() {
        return "Reclamation{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", date=" + date +
                ", user=" + user +
                ", adminReply='" + adminReply + '\'' +
                '}';
    }
} 