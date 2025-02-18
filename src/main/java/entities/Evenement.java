package entities;

import java.sql.Timestamp;

public class Evenement {
    private int id;
    private String title;
    private String description;
    private Timestamp eventDate;
    private String location;
    private String categoryEvent;
    private Timestamp createdAt;
    private Timestamp updateAt;

    // Constructeur par défaut
    public Evenement() {
    }

    // Constructeur avec paramètres
    public Evenement(String title, String description, Timestamp eventDate, 
                    String location, String categoryEvent) {
        this.title = title;
        this.description = description;
        this.eventDate = eventDate;
        this.location = location;
        this.categoryEvent = categoryEvent;
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.updateAt = this.createdAt;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Timestamp getEventDate() { return eventDate; }
    public void setEventDate(Timestamp eventDate) { this.eventDate = eventDate; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public String getCategoryEvent() { return categoryEvent; }
    public void setCategoryEvent(String categoryEvent) { this.categoryEvent = categoryEvent; }
    
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    
    public Timestamp getUpdateAt() { return updateAt; }
    public void setUpdateAt(Timestamp updateAt) { this.updateAt = updateAt; }

    @Override
    public String toString() {
        return "Evenement{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", eventDate=" + eventDate +
                ", location='" + location + '\'' +
                ", categoryEvent='" + categoryEvent + '\'' +
                ", createdAt=" + createdAt +
                ", updateAt=" + updateAt +
                '}';
    }
} 