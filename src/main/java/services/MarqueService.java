package services;

import entities.Marque;
import utils.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MarqueService implements IService<Marque> {

    private Connection cnx;

    public MarqueService() {
        cnx = MyConnection.getInstance().getConnection();
    }

    @Override
    public int create(Marque marque) throws SQLException {
        String query = "INSERT INTO marque (logo, nom) VALUES (?, ?)";
        PreparedStatement ps = cnx.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, marque.getLogo());
        ps.setString(2, marque.getNom());

        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            int generatedId = rs.getInt(1);
            marque.setId(generatedId);
            return generatedId;
        } else {
            throw new SQLException("Failed to retrieve generated ID.");
        }
    }

    @Override
    public void update(Marque marque) throws SQLException {
        String query = "UPDATE marque SET logo = ?, nom = ? WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setString(1, marque.getLogo());
        ps.setString(2, marque.getNom());
        ps.setInt(3, marque.getId());
        ps.executeUpdate();
    }

    @Override
    public void delete(Marque marque) throws SQLException {
        String query = "DELETE FROM marque WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setInt(1, marque.getId());
        ps.executeUpdate();
    }

    @Override
    public List<Marque> readAll() throws SQLException {
        List<Marque> marques = new ArrayList<>();
        String query = "SELECT * FROM marque";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            int id = rs.getInt("id");
            String logo = rs.getString("logo");
            String nom = rs.getString("nom");

            Marque marque = new Marque(id, logo, nom);
            marques.add(marque);
        }
        return marques;
    }
}
