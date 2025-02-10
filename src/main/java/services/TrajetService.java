package services;

import entities.Trajet;
import utils.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrajetService implements IService<Trajet> {

    private Connection cnx;

    public TrajetService() {
        cnx = MyConnection.getInstance().getConnection();
    }

    @Override
    public void create(Trajet trajet) throws SQLException {
        String query = "INSERT INTO trajet (annonce_id, available_seats, price, created_at) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = cnx.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, trajet.getAnnonceId());
        ps.setInt(2, trajet.getAvailableSeats());
        ps.setDouble(3, trajet.getPrice());
        ps.setDate(4, trajet.getCreatedAt());
        ps.executeUpdate();

        // Récupérer l'ID généré automatiquement
        ResultSet generatedKeys = ps.getGeneratedKeys();
        if (generatedKeys.next()) {
            trajet.setId(generatedKeys.getInt(1));
        }
    }

    @Override
    public void update(Trajet trajet) throws SQLException {
        String query = "UPDATE trajet SET available_seats = ?, price = ? WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setInt(1, trajet.getAvailableSeats());
        ps.setDouble(2, trajet.getPrice());
        ps.setInt(3, trajet.getId());
        int rowsUpdated = ps.executeUpdate();

        if (rowsUpdated == 0) {
            System.out.println("Aucun trajet mis à jour. Vérifiez l'ID.");
        }
    }

    @Override
    public void delete(Trajet trajet) throws SQLException {
        String query = "DELETE FROM trajet WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setInt(1, trajet.getId());
        int rowsDeleted = ps.executeUpdate();

        if (rowsDeleted == 0) {
            System.out.println("Aucun trajet supprimé. Vérifiez l'ID.");
        }
    }

    @Override
    public List<Trajet> readAll() throws SQLException {
        List<Trajet> trajets = new ArrayList<>();
        String query = "SELECT * FROM trajet";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            Trajet trajet = new Trajet(
                    rs.getInt("id"),
                    rs.getInt("annonce_id"),
                    rs.getInt("available_seats"),
                    rs.getDouble("price"),
                    rs.getDate("created_at")
            );
            trajets.add(trajet);
        }
        return trajets;
    }
}
