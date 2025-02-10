package entities;

import java.sql.Date;

public class Annonce {
    private int id;
    private String titre;
    private String description;
    private Date datePublication;
    private int driverId;
    private int carId;
    private Date departureDate;
    private String departurePoint;
    private String arrivalPoint;
    private String status;

    public Annonce() {
    }

    public Annonce(int id, String titre, String description, Date datePublication, int driverId, int carId, Date departureDate, String departurePoint, String arrivalPoint, String status) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.datePublication = datePublication;
        this.driverId = driverId;
        this.carId = carId;
        this.departureDate = departureDate;
        this.departurePoint = departurePoint;
        this.arrivalPoint = arrivalPoint;
        this.status = status;
    }

    public Annonce(String titre, String description, Date datePublication, int driverId, int carId, Date departureDate, String departurePoint, String arrivalPoint, String status) {
        this.titre = titre;
        this.description = description;
        this.datePublication = datePublication;
        this.driverId = driverId;
        this.carId = carId;
        this.departureDate = departureDate;
        this.departurePoint = departurePoint;
        this.arrivalPoint = arrivalPoint;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDatePublication() {
        return datePublication;
    }

    public void setDatePublication(Date datePublication) {
        this.datePublication = datePublication;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public String getDeparturePoint() {
        return departurePoint;
    }

    public void setDeparturePoint(String departurePoint) {
        this.departurePoint = departurePoint;
    }

    public String getArrivalPoint() {
        return arrivalPoint;
    }

    public void setArrivalPoint(String arrivalPoint) {
        this.arrivalPoint = arrivalPoint;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Annonce{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", datePublication=" + datePublication +
                ", driverId=" + driverId +
                ", carId=" + carId +
                ", departureDate=" + departureDate +
                ", departurePoint='" + departurePoint + '\'' +
                ", arrivalPoint='" + arrivalPoint + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
