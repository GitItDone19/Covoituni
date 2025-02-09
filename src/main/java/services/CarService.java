package services;

import entities.Car;
import utils.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarService implements IService<Car> {

    private Connection cnx;

    public CarService() {
        cnx = MyConnection.getInstance().getConnection();
    }

    @Override
    public int create(Car car) throws SQLException {
        String query = "INSERT INTO car (plaqueImatriculation, marque_id, model_id, type, dateImatriculation, couleur) " +
                       "VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = cnx.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, car.getPlaqueImatriculation());
        ps.setInt(2, car.getMarqueId());
        ps.setInt(3, car.getModelId());
        ps.setString(4, car.getType());
        ps.setDate(5, new java.sql.Date(car.getDateImatriculation().getTime()));
        ps.setString(6, car.getCouleur());

        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            int generatedId = rs.getInt(1);
            car.setId(generatedId);
            return generatedId;
        } else {
            throw new SQLException("Failed to retrieve generated ID.");
        }
    }

    @Override
    public void update(Car car) throws SQLException {
        String query = "UPDATE car SET plaqueImatriculation = ?, marque_id = ?, model_id = ?, type = ?, dateImatriculation = ?, couleur = ? " +
                       "WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setString(1, car.getPlaqueImatriculation());
        ps.setInt(2, car.getMarqueId());
        ps.setInt(3, car.getModelId());
        ps.setString(4, car.getType());
        ps.setDate(5, new java.sql.Date(car.getDateImatriculation().getTime()));
        ps.setString(6, car.getCouleur());
        ps.setInt(7, car.getId());
        ps.executeUpdate();
    }

    @Override
    public void delete(Car car) throws SQLException {
        String query = "DELETE FROM car WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setInt(1, car.getId());
        ps.executeUpdate();
    }

    @Override
    public List<Car> readAll() throws SQLException {
        List<Car> cars = new ArrayList<>();
        String query = "SELECT * FROM car";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            int id = rs.getInt("id");
            String plaqueImatriculation = rs.getString("plaqueImatriculation");
            int marqueId = rs.getInt("marque_id");
            int modelId = rs.getInt("model_id");
            String type = rs.getString("type");
            Date dateImatriculation = rs.getDate("dateImatriculation");
            String couleur = rs.getString("couleur");

            Car car = new Car(id, plaqueImatriculation, marqueId, modelId, type, dateImatriculation, couleur);
            cars.add(car);
        }
        return cars;
    }
}
