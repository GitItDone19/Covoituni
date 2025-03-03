package services;

import entities.Car;
import entities.Categorie;
import utils.DataSource;


import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarService implements IService<Car> {
    private Connection connection;
    private CategorieService categorieService;

    public CarService() {
        connection = DataSource.getInstance().getConnection();
        categorieService = new CategorieService();
    }

    @Override
    public void create(Car car) throws SQLException {
        String query = "INSERT INTO car (plaqueImatriculation, description, dateImatriculation, couleur, marque, modele, categorie_id) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, car.getPlaqueImatriculation());
        ps.setString(2, car.getDescription());
        ps.setDate(3, new java.sql.Date(car.getDateImatriculation().getTime()));
        ps.setString(4, car.getCouleur());
        ps.setString(5, car.getMarque());
        ps.setString(6, car.getModele());
        ps.setInt(7, car.getCategorieId());
        ps.executeUpdate();
        
        ResultSet generatedKeys = ps.getGeneratedKeys();
        if (generatedKeys.next()) {
            car.setId(generatedKeys.getInt(1));
        }
    }

    @Override
    public void update(Car car) throws SQLException {
        String query = "UPDATE car SET plaqueImatriculation = ?, description = ?, dateImatriculation = ?, " +
                      "couleur = ?, marque = ?, modele = ?, categorie_id = ? WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, car.getPlaqueImatriculation());
        ps.setString(2, car.getDescription());
        ps.setDate(3, new java.sql.Date(car.getDateImatriculation().getTime()));
        ps.setString(4, car.getCouleur());
        ps.setString(5, car.getMarque());
        ps.setString(6, car.getModele());
        ps.setInt(7, car.getCategorieId());
        ps.setInt(8, car.getId());
        ps.executeUpdate();
    }

    @Override
    public void delete(Car car) throws SQLException {
        String query = "DELETE FROM car WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, car.getId());
        ps.executeUpdate();
    }

    @Override
    public List<Car> readAll() throws SQLException {
        List<Car> cars = new ArrayList<>();
        String query = "SELECT c.*, cat.nom as categorie_nom FROM car c " +
                      "LEFT JOIN categorie cat ON c.categorie_id = cat.id";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query);

        while (rs.next()) {
            Car car = new Car(
                rs.getInt("id"),
                rs.getString("plaqueImatriculation"),
                rs.getString("description"),
                rs.getDate("dateImatriculation"),
                rs.getString("couleur"),
                rs.getString("marque"),
                rs.getString("modele"),
                rs.getInt("categorie_id")
            );
            cars.add(car);
        }
        return cars;
    }

    public Car findById(int id) throws SQLException {
        String query = "SELECT c.*, cat.nom as categorie_nom FROM car c " +
                      "LEFT JOIN categorie cat ON c.categorie_id = cat.id " +
                      "WHERE c.id = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return new Car(
                rs.getInt("id"),
                rs.getString("plaqueImatriculation"),
                rs.getString("description"),
                rs.getDate("dateImatriculation"),
                rs.getString("couleur"),
                rs.getString("marque"),
                rs.getString("modele"),
                rs.getInt("categorie_id")
            );
        }
        return null;
    }
    // New method to search cars by immatriculation
    public List<Car> searchByImmatriculation(String immatriculation) throws SQLException {
        String query = "SELECT * FROM car WHERE plaqueImatriculation LIKE ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, "%" + immatriculation + "%");

        ResultSet rs = stmt.executeQuery();
        List<Car> cars = new ArrayList<>();
        while (rs.next()) {
            cars.add(new Car(
                    rs.getString("plaqueImatriculation"),
                    rs.getString("description"),
                    rs.getDate("dateImatriculation"),
                    rs.getString("couleur"),
                    rs.getString("marque"),
                    rs.getString("modele"),
                    rs.getInt("categorie_id")
            ));
        }
        return cars;
    }
    public Map<String, Integer> getCarCountByYear() throws SQLException {
        String query = "SELECT YEAR(dateImatriculation) AS year, COUNT(*) AS count FROM car GROUP BY year";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        Map<String, Integer> carStats = new HashMap<>();
        while (rs.next()) {
            carStats.put(rs.getString("year"), rs.getInt("count"));
        }
        return carStats;
    }

}
