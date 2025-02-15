package services;

import entities.Reclamation;
import utils.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReclamationService implements IService<Reclamation> {
    private Connection cnx;

    public ReclamationService() {
        cnx = MyConnection.getInstance().getConnection();
    }

    @Override
    public void create(Reclamation reclamation) throws SQLException {
        String query = "INSERT INTO reclamation (description, status, dateReclamation, userId) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setString(1, reclamation.getDescription());
        ps.setString(2, reclamation.getStatus());
        ps.setDate(3, reclamation.getDateReclamation());
        ps.setInt(4, reclamation.getUserId());
        ps.executeUpdate();
    }

    @Override
    public void update(Reclamation reclamation) throws SQLException {
        String query = "UPDATE reclamation SET description=?, status=?, dateReclamation=?, userId=? WHERE id=?";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setString(1, reclamation.getDescription());
        ps.setString(2, reclamation.getStatus());
        ps.setDate(3, reclamation.getDateReclamation());
        ps.setInt(4, reclamation.getUserId());
        ps.setInt(5, reclamation.getId());
        ps.executeUpdate();
    }

    @Override
    public void delete(Reclamation reclamation) throws SQLException {
        String query = "DELETE FROM reclamation WHERE id=?";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setInt(1, reclamation.getId());
        ps.executeUpdate();
    }

    @Override
    public List<Reclamation> readAll() throws SQLException {
        List<Reclamation> reclamations = new ArrayList<>();
        String query = "SELECT * FROM reclamation";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            Reclamation r = new Reclamation(
                rs.getInt("id"),
                rs.getString("description"),
                rs.getString("status"),
                rs.getDate("dateReclamation"),
                rs.getInt("userId")
            );
            reclamations.add(r);
        }
        return reclamations;
    }
} 