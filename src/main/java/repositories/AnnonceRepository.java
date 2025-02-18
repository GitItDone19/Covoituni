package repositories;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import entities.Annonce.Annonce;
import utils.MyConnection;

public class AnnonceRepository {
    private Connection connection;

    public AnnonceRepository() {
        this.connection = MyConnection.getInstance().getCnx();
    }

    public Annonce findById(Long id) {
        String sql = "SELECT * FROM annonce WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToAnnonce(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    private Annonce mapResultSetToAnnonce(ResultSet rs) throws SQLException {
        Annonce annonce = new Annonce();
        annonce.setId(rs.getLong("id"));
        annonce.setTitre(rs.getString("titre"));
        annonce.setDescription(rs.getString("description"));
        annonce.setDatePublication(rs.getTimestamp("date_publication"));
        annonce.setDriverId(rs.getInt("driver_id"));
        annonce.setCarId(rs.getInt("car_id"));
        annonce.setStatus(rs.getString("status"));
        
        int trajetId = rs.getInt("trajet_id");
        if (!rs.wasNull()) {
            annonce.setTrajetId(trajetId);
        }
        
        return annonce;
    }

    public List<Annonce> findAll() {
        List<Annonce> annonces = new ArrayList<>();
        String sql = "SELECT * FROM annonce ORDER BY date_publication DESC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                annonces.add(mapResultSetToAnnonce(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return annonces;
    }

    public Annonce save(Annonce annonce) {
        String sql;
        if (annonce.getId() == null) {
            sql = "INSERT INTO annonce (titre, description, driver_id, car_id, status, trajet_id) " +
                  "VALUES (?, ?, ?, ?, ?, ?)";
        } else {
            sql = "UPDATE annonce SET titre=?, description=?, driver_id=?, car_id=?, status=?, trajet_id=? " +
                  "WHERE id=?";
        }

        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, annonce.getTitre());
            pstmt.setString(2, annonce.getDescription());
            pstmt.setInt(3, annonce.getDriverId());
            pstmt.setInt(4, annonce.getCarId());
            pstmt.setString(5, annonce.getStatus());
            
            if (annonce.getTrajetId() != null) {
                pstmt.setInt(6, annonce.getTrajetId());
            } else {
                pstmt.setNull(6, Types.INTEGER);
            }
            
            if (annonce.getId() != null) {
                pstmt.setLong(7, annonce.getId());
            }

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La création de l'annonce a échoué");
            }

            if (annonce.getId() == null && pstmt.getGeneratedKeys().next()) {
                annonce.setId(pstmt.getGeneratedKeys().getLong(1));
            }
            
            return annonce;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM annonce WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
} 