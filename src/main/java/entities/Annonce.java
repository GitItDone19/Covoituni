package entities;

import java.time.LocalDateTime;

public class Annonce {
    private long id;
    private String titre;
    private String description;
    private LocalDateTime datePublication;
    private int driverId;
    private int carId;
    private String status;
    private Integer trajetId;
    private int availableSeats;
    private LocalDateTime dateTermination;
    private int eventId;

    public Annonce() {
        this.datePublication = LocalDateTime.now();
        this.status = "OPEN";
    }

    public Annonce(String titre, String description, int driverId, int carId, int availableSeats) {
        this();
        this.titre = titre;
        this.description = description;
        this.driverId = driverId;
        this.carId = carId;
        this.availableSeats = availableSeats;
    }

    // Getters
    public long getId() { return id; }
    public String getTitre() { return titre; }
    public String getDescription() { return description; }
    public LocalDateTime getDatePublication() { return datePublication; }
    public int getDriverId() { return driverId; }
    public int getCarId() { return carId; }
    public String getStatus() { return status; }
    public Integer getTrajetId() { return trajetId; }
    public int getAvailableSeats() { return availableSeats; }
    public LocalDateTime getDateTermination() { return dateTermination; }
    public int getEventId() { return eventId; }

    // Setters
    public void setId(long id) { this.id = id; }
    public void setTitre(String titre) { this.titre = titre; }
    public void setDescription(String description) { this.description = description; }
    public void setDatePublication(LocalDateTime datePublication) { this.datePublication = datePublication; }
    public void setDriverId(int driverId) { this.driverId = driverId; }
    public void setCarId(int carId) { this.carId = carId; }
    public void setStatus(String status) { this.status = status; }
    public void setTrajetId(Integer trajetId) { this.trajetId = trajetId; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }
    public void setDateTermination(LocalDateTime dateTermination) { this.dateTermination = dateTermination; }
    public void setEventId(int eventId) { this.eventId = eventId; }

    @Override
    public String toString() {
        return "Annonce{" +
                "titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", places disponibles=" + availableSeats +
                ", status='" + status + '\'' +
                '}';
    }
} 