package Services;

import Services.IService;
import entities.Trajet;
import utils.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrajetService implements IService<Trajet> {
    private Connection cnx;

    public TrajetService() {
        cnx = MyConnection.getInstance().getCnx();
    }

    @Override
    public void create(Trajet trajet) throws SQLException {
        String sql = "INSERT INTO trajet (titre, departure_point, arrival_point, departure_date, price) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setString(1, trajet.getTitre());
            pst.setString(2, trajet.getDeparturePoint());
            pst.setString(3, trajet.getArrivalPoint());
            pst.setTimestamp(4, Timestamp.valueOf(trajet.getDepartureDate()));
            pst.setDouble(5, trajet.getPrice());
            pst.executeUpdate();
        }
    }

    @Override
    public void update(Trajet trajet) throws SQLException {
        String sql = "UPDATE trajet SET titre=?, departure_point=?, arrival_point=?, departure_date=?, price=? WHERE id=?";
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setString(1, trajet.getTitre());
            pst.setString(2, trajet.getDeparturePoint());
            pst.setString(3, trajet.getArrivalPoint());
            pst.setTimestamp(4, Timestamp.valueOf(trajet.getDepartureDate()));
            pst.setDouble(5, trajet.getPrice());
            pst.setInt(6, trajet.getId());
            pst.executeUpdate();
        }
    }

    @Override
    public void delete(Trajet trajet) throws SQLException {
        String sql = "DELETE FROM trajet WHERE id=?";
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setInt(1, trajet.getId());
            pst.executeUpdate();
        }
    }

    @Override
    public List<Trajet> readAll() throws SQLException {
        List<Trajet> trajets = new ArrayList<>();
        String sql = "SELECT * FROM trajet";
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Trajet t = new Trajet();
                t.setId(rs.getInt("id"));
                t.setTitre(rs.getString("titre"));
                t.setDeparturePoint(rs.getString("departure_point"));
                t.setArrivalPoint(rs.getString("arrival_point"));
                t.setDepartureDate(rs.getTimestamp("departure_date").toLocalDateTime());
                t.setPrice(rs.getDouble("price"));
                trajets.add(t);
            }
        }
        return trajets;
    }
} 