package entities;



import java.util.Date;

public class Reservation {
    private int id;
    private Date dateReservation;
    private String nomClient;            //TODO: id client ?
    private int nombrePersonnes;
    private String trajet;                  //TODO: n7iou trajet n3awthouha b annonce
    private String commentaires;



    // Constructeur
    public Reservation(int id, Date dateReservation, String nomClient, int nombrePersonnes, String trajet, String commentaires) {
        this.id = id;
        this.dateReservation = dateReservation;
        this.nomClient = nomClient;
        this.nombrePersonnes = nombrePersonnes;
        this.trajet = trajet;
        this.commentaires = commentaires;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(Date dateReservation) {
        this.dateReservation = dateReservation;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public int getNombrePersonnes() {
        return nombrePersonnes;
    }

    public void setNombrePersonnes(int nombrePersonnes) {
        this.nombrePersonnes = nombrePersonnes;
    }

    public String getTrajet() {
        return trajet;
    }

    public void setTrajet(String trajet) {
        this.trajet = trajet;
    }

    public String getCommentaires() {
        return commentaires;
    }

    public void setCommentaires(String commentaires) {
        this.commentaires = commentaires;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", dateReservation=" + dateReservation +
                ", nomClient='" + nomClient + '\'' +
                ", nombrePersonnes=" + nombrePersonnes +
                ", trajet='" + trajet + '\'' +
                ", commentaires='" + commentaires + '\'' +
                '}';
    }
}