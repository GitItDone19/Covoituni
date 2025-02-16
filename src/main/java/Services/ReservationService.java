package Services;


import Services.IServices;
import entities.Reservation;
import utils.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationService implements IServices<Reservation> {

    private Connection cnx;

    public ReservationService() {
        cnx = MyConnection.getInstance().getCnx();
    }

    @Override
    public void create(Reservation reservation) throws SQLException {
        String query = "INSERT INTO reservation (date_reservation, nom_client, nombre_personnes, trajet, commentaires) " +
                "VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setDate(1, new java.sql.Date(reservation.getDateReservation().getTime()));
        ps.setString(2, reservation.getNomClient());
        ps.setInt(3, reservation.getNombrePersonnes());
        ps.setString(4, reservation.getTrajet());
        ps.setString(5, reservation.getCommentaires());
        ps.executeUpdate();
    }

    @Override
    public void update(Reservation reservation) throws SQLException {
        String query = "UPDATE reservation SET date_reservation = ?, nom_client = ?, nombre_personnes = ?, trajet = ?, commentaires = ? WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setDate(1, new java.sql.Date(reservation.getDateReservation().getTime()));
        ps.setString(2, reservation.getNomClient());
        ps.setInt(3, reservation.getNombrePersonnes());
        ps.setString(4, reservation.getTrajet());
        ps.setString(5, reservation.getCommentaires());
        ps.setInt(6, reservation.getId());
        ps.executeUpdate();
    }

    @Override
    public void delete(Reservation reservation) throws SQLException {
        String query = "DELETE FROM reservation WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setInt(1, reservation.getId());
        ps.executeUpdate();
    }

    @Override
    public List<Reservation> readAll() throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT * FROM reservation";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            int id = rs.getInt("id");
            Date dateReservation = rs.getDate("date_reservation");
            String nomClient = rs.getString("nom_client");
            int nombrePersonnes = rs.getInt("nombre_personnes");
            String trajet = rs.getString("trajet");
            String commentaires = rs.getString("commentaires");

            Reservation reservation = new Reservation(id, dateReservation, nomClient, nombrePersonnes, trajet, commentaires);
            reservations.add(reservation);
        }

        return reservations;
    }
}