package services;

import entities.Categorie;
import utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategorieService implements IService<Categorie> {
    private Connection connection;

    public CategorieService() {
        connection = DataSource.getInstance().getConnection();
    }

    @Override
    public void create(Categorie categorie) throws SQLException {
        String query = "INSERT INTO categorie (nom, description) VALUES (?, ?)";
        PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, categorie.getNom());
        ps.setString(2, categorie.getDescription());
        ps.executeUpdate();
        
        ResultSet generatedKeys = ps.getGeneratedKeys();
        if (generatedKeys.next()) {
            categorie.setId(generatedKeys.getInt(1));
        }
    }

    @Override
    public void update(Categorie categorie) throws SQLException {
        String query = "UPDATE categorie SET nom = ?, description = ? WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, categorie.getNom());
        ps.setString(2, categorie.getDescription());
        ps.setInt(3, categorie.getId());
        ps.executeUpdate();
    }

    @Override
    public void delete(Categorie categorie) throws SQLException {
        String query = "DELETE FROM categorie WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, categorie.getId());
        ps.executeUpdate();
    }

    @Override
    public List<Categorie> readAll() throws SQLException {
        List<Categorie> categories = new ArrayList<>();
        String query = "SELECT * FROM categorie";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query);

        while (rs.next()) {
            categories.add(new Categorie(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getString("description")
            ));
        }
        return categories;
    }

    public Categorie findById(int id) throws SQLException {
        String query = "SELECT * FROM categorie WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return new Categorie(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getString("description")
            );
        }
        return null;
    }
} 