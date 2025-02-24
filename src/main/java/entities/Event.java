package entities;

public class Event {
    private int idEvent;
    private String nom;
    private String dateEvent;
    private String heureEvent;
    private String lieu;
    private String description;
    private int idType;
    private String status;

    public Event() {
    }

    public Event(int idEvent, String nom, String dateEvent, String heureEvent, String lieu, String description, int idType, String status) {
        this.idEvent = idEvent;
        this.nom = nom;
        this.dateEvent = dateEvent;
        this.heureEvent = heureEvent;
        this.lieu = lieu;
        this.description = description;
        this.idType = idType;
        this.status = status;
    }

    // Getters
    public int getIdEvent() { return idEvent; }
    public String getNom() { return nom; }
    public String getDateEvent() { return dateEvent; }
    public String getHeureEvent() { return heureEvent; }
    public String getLieu() { return lieu; }
    public String getDescription() { return description; }
    public int getIdType() { return idType; }
    public String getStatus() { return status; }

    // Setters
    public void setIdEvent(int idEvent) { this.idEvent = idEvent; }
    public void setNom(String nom) { this.nom = nom; }
    public void setDateEvent(String dateEvent) { this.dateEvent = dateEvent; }
    public void setHeureEvent(String heureEvent) { this.heureEvent = heureEvent; }
    public void setLieu(String lieu) { this.lieu = lieu; }
    public void setDescription(String description) { this.description = description; }
    public void setIdType(int idType) { this.idType = idType; }
    public void setStatus(String status) { this.status = status; }
} 