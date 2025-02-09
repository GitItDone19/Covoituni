//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package Services;

import entities.Annonce;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import utils.MyConnection;

public class AnnonceService implements IService<Annonce> {
    private Connection cnx = MyConnection.getInstance().getConnection();

    public AnnonceService() {
    }

    public void create(Annonce annonce) throws SQLException {
        String query = "INSERT INTO annonce (titre, description, date_publication, driver_id, car_id, departure_date, departure_point, arrival_point, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = this.cnx.prepareStatement(query);
        ps.setString(1, annonce.getTitre());
        ps.setString(2, annonce.getDescription());
        ps.setDate(3, annonce.getDatePublication());
        ps.setInt(4, annonce.getDriverId());
        ps.setInt(5, annonce.getCarId());
        ps.setDate(6, annonce.getDepartureDate());
        ps.setString(7, annonce.getDeparturePoint());
        ps.setString(8, annonce.getArrivalPoint());
        ps.setString(9, annonce.getStatus());
        ps.executeUpdate();
    }

    public void update(Annonce annonce) throws SQLException {
        String query = "UPDATE annonce SET titre = ?, description = ?, status = ? WHERE id = ?";
        PreparedStatement ps = this.cnx.prepareStatement(query);
        ps.setString(1, annonce.getTitre());
        ps.setString(2, annonce.getDescription());
        ps.setString(3, annonce.getStatus());
        ps.setInt(4, annonce.getId());
        ps.executeUpdate();
    }

    public void delete(Annonce annonce) throws SQLException {
        String query = "DELETE FROM annonce WHERE id = ?";
        PreparedStatement ps = this.cnx.prepareStatement(query);
        ps.setInt(1, annonce.getId());
        ps.executeUpdate();
    }

    public List<Annonce> readAll() throws SQLException {
        List<Annonce> annonces = new ArrayList();
        String query = "SELECT * FROM annonce";
        Statement st = this.cnx.createStatement();
        ResultSet rs = st.executeQuery(query);

        while(rs.next()) {
            Annonce annonce = new Annonce(rs.getInt("id"), rs.getString("titre"), rs.getString("description"), rs.getDate("date_publication"), rs.getInt("driver_id"), rs.getInt("car_id"), rs.getDate("departure_date"), rs.getString("departure_point"), rs.getString("arrival_point"), rs.getString("status"));
            annonces.add(annonce);
        }

        return annonces;
    }
}
