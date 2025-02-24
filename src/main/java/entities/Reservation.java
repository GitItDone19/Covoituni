package entities;

import java.time.LocalDateTime;

public class Reservation {
    private long id;
    private long annonceId;
    private int passengerId;
    private String status;
    private String comment;
    private LocalDateTime dateReservation;

    public Reservation() {
        this.dateReservation = LocalDateTime.now();
        this.status = "PENDING";
    }

    // Getters et Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    
    public long getAnnonceId() { return annonceId; }
    public void setAnnonceId(long annonceId) { this.annonceId = annonceId; }
    
    public int getPassengerId() { return passengerId; }
    public void setPassengerId(int passengerId) { this.passengerId = passengerId; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    
    public LocalDateTime getDateReservation() { return dateReservation; }
    public void setDateReservation(LocalDateTime dateReservation) { this.dateReservation = dateReservation; }
} 