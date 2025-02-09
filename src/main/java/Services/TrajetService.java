//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package Services;

import entities.Trajet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import utils.MyConnection;

public class TrajetService implements IService<Trajet> {
    private Connection cnx = MyConnection.getInstance().getConnection();

    public TrajetService() {
    }

    public void create(Trajet trajet) throws SQLException {
        String query = "INSERT INTO trajet (annonce_id, available_seats, price, created_at) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = this.cnx.prepareStatement(query);
        ps.setInt(1, trajet.getAnnonceId());
        ps.setInt(2, trajet.getAvailableSeats());
        ps.setDouble(3, trajet.getPrice());
        ps.setDate(4, trajet.getCreatedAt());
        ps.executeUpdate();
    }

    public void update(Trajet trajet) throws SQLException {
        String query = "UPDATE trajet SET available_seats = ?, price = ? WHERE id = ?";
        PreparedStatement ps = this.cnx.prepareStatement(query);
        ps.setInt(1, trajet.getAvailableSeats());
        ps.setDouble(2, trajet.getPrice());
        ps.setInt(3, trajet.getId());
        ps.executeUpdate();
    }

    public void delete(Trajet trajet) throws SQLException {
        String query = "DELETE FROM trajet WHERE id = ?";
        PreparedStatement ps = this.cnx.prepareStatement(query);
        ps.setInt(1, trajet.getId());
        ps.executeUpdate();
    }

    public List<Trajet> readAll() throws SQLException {
        List<Trajet> trajets = new ArrayList();
        String query = "SELECT * FROM trajet";
        Statement st = this.cnx.createStatement();
        ResultSet rs = st.executeQuery(query);

        while(rs.next()) {
            Trajet trajet = new Trajet(rs.getInt("id"), rs.getInt("annonce_id"), rs.getInt("available_seats"), rs.getDouble("price"), rs.getDate("created_at"));
            trajets.add(trajet);
        }

        return trajets;
    }
}
