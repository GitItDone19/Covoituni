package Services;

import entities.Trajet;
import entities.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrajetService implements IService<Trajet> {

    private Connection connection;

    public TrajetService() {this.connection = MyConnection.getConnection().getCnx();
    }

    @Override
    public void ajouter(Trajet trajet) throws SQLException {
        String query = "INSERT INTO trajet (annonce_id, available_seats, price, created_at) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, trajet.getAnnonceId());
        ps.setInt(2, trajet.getAvailableSeats());
        ps.setDouble(3, trajet.getPrice());
        ps.setDate(4, trajet.getCreatedAt());
        ps.executeUpdate();
    }

    @Override
    public void update(Trajet trajet) throws SQLException {
        String query = "UPDATE trajet SET available_seats = ?, price = ? WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, trajet.getAvailableSeats());
        ps.setDouble(2, trajet.getPrice());
        ps.setInt(3, trajet.getId());
        ps.executeUpdate();
    }

    @Override
    public void delete(Trajet trajet) throws SQLException {
        String query = "DELETE FROM trajet WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, trajet.getId());
        ps.executeUpdate();
    }

    @Override
    public List<Trajet> afficherAll() throws SQLException {
        List<Trajet> trajets = new ArrayList<>();
        String query = "SELECT * FROM trajet";
        Statement st = connection.createStatement();
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
