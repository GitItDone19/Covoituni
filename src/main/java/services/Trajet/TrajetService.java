package services.Trajet;

import entities.Trajet.Trajet;
import utils.MyConnection;
import services.IService;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Service gérant les opérations CRUD pour les trajets
 * Cette classe assure l'interaction avec la base de données pour les trajets
 * Implémente l'interface IService pour standardiser les opérations CRUD
 */
public class TrajetService implements IService<Trajet> {
    
    private Connection conn;  // Connexion à la base de données

    /**
     * Constructeur qui initialise la connexion à la base de données
     * Utilise le singleton MyConnection pour assurer une connexion unique
     */
    public TrajetService() {
        conn = MyConnection.getInstance().getConnection();
    }

    /**
     * Crée un nouveau trajet dans la base de données
     * @param trajet Le trajet à créer avec ses informations
     * @throws SQLException En cas d'erreur lors de l'insertion
     */
    @Override
    public void create(Trajet trajet) throws SQLException {
        String sql = "INSERT INTO trajet (titre, departure_point, arrival_point, departure_date, available_seats, price) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            // Préparation des données
            pst.setString(1, trajet.getTitre());
            pst.setString(2, trajet.getDeparturePoint());
            pst.setString(3, trajet.getArrivalPoint());
            pst.setTimestamp(4, trajet.getDepartureDate());
            pst.setInt(5, trajet.getAvailableSeats());
            pst.setDouble(6, trajet.getPrice());
            
            pst.executeUpdate();
        }
    }

    /**
     * Met à jour un trajet existant dans la base de données
     * @param trajet Le trajet avec les nouvelles informations
     * @throws SQLException En cas d'erreur lors de la mise à jour
     */
    @Override
    public void update(Trajet trajet) throws SQLException {
        String sql = "UPDATE trajet SET titre=?, departure_point=?, arrival_point=?, " +
                    "departure_date=?, available_seats=?, price=? WHERE id=?";
        
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            // Mise à jour des données
            pst.setString(1, trajet.getTitre());
            pst.setString(2, trajet.getDeparturePoint());
            pst.setString(3, trajet.getArrivalPoint());
            pst.setTimestamp(4, trajet.getDepartureDate());
            pst.setInt(5, trajet.getAvailableSeats());
            pst.setDouble(6, trajet.getPrice());
            pst.setInt(7, trajet.getId());
            
            pst.executeUpdate();
        }
    }

    /**
     * Récupère tous les trajets de la base de données
     * @return Liste de tous les trajets disponibles
     * @throws SQLException En cas d'erreur lors de la lecture
     */
    @Override
    public List<Trajet> readAll() throws SQLException {
        List<Trajet> trajets = new ArrayList<>();
        String sql = "SELECT * FROM trajet";
        
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                // Construction de l'objet Trajet pour chaque entrée
                Trajet trajet = new Trajet(
                    rs.getInt("id"),
                    rs.getString("titre"),
                    rs.getString("departure_point"),
                    rs.getString("arrival_point"),
                    rs.getTimestamp("departure_date"),
                    rs.getInt("available_seats"),
                    rs.getDouble("price"),
                    rs.getTimestamp("created_at")
                );
                trajets.add(trajet);
            }
        }
        return trajets;
    }

    /**
     * Supprime un trajet de la base de données
     * @param trajet Le trajet à supprimer
     * @throws SQLException En cas d'erreur lors de la suppression
     */
    @Override
    public void delete(Trajet trajet) throws SQLException {
        String sql = "DELETE FROM trajet WHERE id=?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, trajet.getId());
            pst.executeUpdate();
        }
    }

    /**
     * Supprime un trajet de la base de données
     * @param id Identifiant du trajet à supprimer
     * @throws SQLException En cas d'erreur lors de la suppression
     */
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM trajet WHERE id=?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }

    /**
     * Récupère un trajet spécifique par son ID
     * @param id Identifiant du trajet recherché
     * @return Le trajet trouvé ou null si non trouvé
     * @throws SQLException En cas d'erreur lors de la lecture
     */
    public Trajet readById(int id) throws SQLException {
        String sql = "SELECT * FROM trajet WHERE id = ?";
        
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    // Construction et retour du trajet trouvé
                    return new Trajet(
                        rs.getInt("id"),
                        rs.getString("titre"),
                        rs.getString("departure_point"),
                        rs.getString("arrival_point"),
                        rs.getTimestamp("departure_date"),
                        rs.getInt("available_seats"),
                        rs.getDouble("price"),
                        rs.getTimestamp("created_at")
                    );
                }
            }
        }
        return null;
    }

    public List<Integer> getTrajetIds() throws SQLException {
        List<Integer> trajetIds = new ArrayList<>();
        String sql = "SELECT id FROM trajet";

        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                trajetIds.add(rs.getInt("id"));
            }
        }
        System.out.println("Liste des IDs trajets: " + trajetIds);  // Vérification
        return trajetIds;
    }
}
