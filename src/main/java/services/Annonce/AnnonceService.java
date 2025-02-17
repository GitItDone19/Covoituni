package services.Annonce;

import entities.Annonce.Annonce;
import utils.DatabaseConnection;
import services.IService;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Service de gestion des annonces
 * Gère les opérations CRUD pour les annonces dans la base de données
 */
public class AnnonceService implements IService<Annonce> {
    private Connection conn;

    public AnnonceService() {
        conn = DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Crée une nouvelle annonce
     * @param annonce L'annonce à créer
     */
    @Override
    public void create(Annonce annonce) throws SQLException {
        String sql = "INSERT INTO annonce (titre, description, date_publication, driver_id, car_id, status, trajet_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, annonce.getTitre());
            pst.setString(2, annonce.getDescription());
            pst.setTimestamp(3, annonce.getDatePublication());
            pst.setInt(4, annonce.getDriverId());
            pst.setInt(5, annonce.getCarId());
            pst.setString(6, annonce.getStatus());
            pst.setInt(7, annonce.getTrajetId()); // Ajout du trajet_id
            pst.executeUpdate();
        }
    }

    public List<Annonce> readAll() throws SQLException {
        List<Annonce> annonces = new ArrayList<>();
        String sql = "SELECT * FROM annonce ORDER BY date_publication DESC";
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Annonce annonce = new Annonce();
                annonce.setId(rs.getInt("id"));
                annonce.setTitre(rs.getString("titre"));
                annonce.setDescription(rs.getString("description"));
                annonce.setDatePublication(rs.getTimestamp("date_publication"));
                annonce.setDriverId(rs.getInt("driver_id"));
                annonce.setCarId(rs.getInt("car_id"));
                annonce.setStatus(rs.getString("status"));
                annonce.setTrajetId(rs.getInt("trajet_id"));
                annonces.add(annonce);
            }
        }
        return annonces;
    }

    public void update(Annonce annonce) throws SQLException {
        String sql = "UPDATE annonce SET titre=?, description=?, driver_id=?, car_id=?, status=?, trajet_id=? WHERE id=?";

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, annonce.getTitre());
            pst.setString(2, annonce.getDescription());
            pst.setInt(3, annonce.getDriverId());
            pst.setInt(4, annonce.getCarId());
            pst.setString(5, annonce.getStatus());
            pst.setInt(6, annonce.getTrajetId()); // Vérification
            pst.setInt(7, annonce.getId());
            pst.executeUpdate();
        }
    }

    @Override
    public void delete(Annonce annonce) throws SQLException {
        // D'abord, supprimer toutes les réservations associées
        String deleteReservations = "DELETE FROM reservation WHERE annonce_id = ?";
        try (PreparedStatement pst = conn.prepareStatement(deleteReservations)) {
            pst.setInt(1, annonce.getId());
            pst.executeUpdate();
        }

        // Ensuite, supprimer l'annonce
        String deleteAnnonce = "DELETE FROM annonce WHERE id = ?";
        try (PreparedStatement pst = conn.prepareStatement(deleteAnnonce)) {
            pst.setInt(1, annonce.getId());
            pst.executeUpdate();
        }
    }

    // Méthode utilitaire pour la suppression par ID
    public void delete(int id) throws SQLException {
        // D'abord, supprimer toutes les réservations associées
        String deleteReservations = "DELETE FROM reservation WHERE annonce_id = ?";
        try (PreparedStatement pst = conn.prepareStatement(deleteReservations)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        }

        // Ensuite, supprimer l'annonce
        String deleteAnnonce = "DELETE FROM annonce WHERE id = ?";
        try (PreparedStatement pst = conn.prepareStatement(deleteAnnonce)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }

    /**
     * Lit une annonce par son ID
     * @param id ID de l'annonce à lire
     * @return L'annonce trouvée
     * @throws SQLException En cas d'erreur SQL
     */
    public Annonce readById(Long id) throws SQLException {
        String sql = "SELECT * FROM annonce WHERE id = ?";
        
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setLong(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Annonce annonce = new Annonce();
                    annonce.setId(rs.getInt("id"));
                    annonce.setTitre(rs.getString("titre"));
                    annonce.setDescription(rs.getString("description"));
                    annonce.setDatePublication(rs.getTimestamp("date_publication"));
                    annonce.setDriverId(rs.getInt("driver_id"));
                    annonce.setCarId(rs.getInt("car_id"));
                    annonce.setStatus(rs.getString("status"));
                    annonce.setTrajetId(rs.getInt("trajet_id"));
                    return annonce;
                }
            }
        }
        return null;
    }
}
