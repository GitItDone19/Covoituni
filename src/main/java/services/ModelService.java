package services;

import entities.Model;
import utils.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ModelService implements IService<Model> {

    private Connection cnx;

    public ModelService() {
        cnx = MyConnection.getInstance().getConnection();
    }

    @Override
    public int create(Model model) throws SQLException {
        String query = "INSERT INTO model (nom) VALUES (?)";
        PreparedStatement ps = cnx.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, model.getNom());

        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            int generatedId = rs.getInt(1);
            model.setId(generatedId);
            return generatedId;
        } else {
            throw new SQLException("Failed to retrieve generated ID.");
        }
    }

    @Override
    public void update(Model model) throws SQLException {
        String query = "UPDATE model SET nom = ? WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setString(1, model.getNom());
        ps.setInt(2, model.getId());
        ps.executeUpdate();
    }

    @Override
    public void delete(Model model) throws SQLException {
        String query = "DELETE FROM model WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setInt(1, model.getId());
        ps.executeUpdate();
    }

    @Override
    public List<Model> readAll() throws SQLException {
        List<Model> models = new ArrayList<>();
        String query = "SELECT * FROM model";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            int id = rs.getInt("id");
            String nom = rs.getString("nom");

            Model model = new Model(id, nom);
            models.add(model);
        }
        return models;
    }
}
