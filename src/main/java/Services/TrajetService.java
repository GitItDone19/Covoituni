package Services;

import entities.Trajet;
import utils.MyConnection;
import java.sql.*;
import java.util.ArrayList;

public class TrajetService implements IService<Trajet> {
    private Connection connection;

    public TrajetService() {
        this.connection = MyConnection.getInstance().getCnx();
    }

    @Override
    public void create(Trajet trajet) throws SQLException {
        String sql = "INSERT INTO trajet (annonce_id, available_seats, price, created_at) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, trajet.getAnnonceId());
            ps.setInt(2, trajet.getAvailableSeats());
            ps.setDouble(3, trajet.getPrice());
            ps.setDate(4, trajet.getCreatedAt());
            ps.executeUpdate();
        }
    }

    @Override
    public void update(Trajet trajet) throws SQLException {
        String sql = "UPDATE trajet SET available_seats=?, price=? WHERE id=?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, trajet.getAvailableSeats());
            ps.setDouble(2, trajet.getPrice());
            ps.setInt(3, trajet.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Trajet trajet) throws SQLException {
        String sql = "DELETE FROM trajet WHERE id=?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, trajet.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public ArrayList<Trajet> readAll() throws SQLException {
        ArrayList<Trajet> trajets = new ArrayList<>();
        String sql = "SELECT * FROM trajet";
        
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
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
        }
        return trajets;
    }
}
