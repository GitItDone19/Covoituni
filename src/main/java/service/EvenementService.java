package service;

import entities.Evenement;
import utils.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EvenementService {
    private Connection cnx;

    public EvenementService() {
        cnx = MyConnection.getInstance().getConnection();
    }

    public void create(Evenement evenement) throws SQLException {
        String query = "INSERT INTO event (title, description, eventDate, location, " +
                      "categoryEvent, createdAt, updateAt) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        PreparedStatement ps = cnx.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, evenement.getTitle());
        ps.setString(2, evenement.getDescription());
        ps.setTimestamp(3, evenement.getEventDate());
        ps.setString(4, evenement.getLocation());
        ps.setString(5, evenement.getCategoryEvent());
        ps.setTimestamp(6, evenement.getCreatedAt());
        ps.setTimestamp(7, evenement.getUpdateAt());

        ps.executeUpdate();
        
        ResultSet generatedKeys = ps.getGeneratedKeys();
        if (generatedKeys.next()) {
            evenement.setId(generatedKeys.getInt(1));
        }
    }

    public List<Evenement> readAll() throws SQLException {
        List<Evenement> evenements = new ArrayList<>();
        String query = "SELECT * FROM event";
        
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            Evenement evenement = new Evenement();
            evenement.setId(rs.getInt("id"));
            evenement.setTitle(rs.getString("title"));
            evenement.setDescription(rs.getString("description"));
            evenement.setEventDate(rs.getTimestamp("eventDate"));
            evenement.setLocation(rs.getString("location"));
            evenement.setCategoryEvent(rs.getString("categoryEvent"));
            evenement.setCreatedAt(rs.getTimestamp("createdAt"));
            evenement.setUpdateAt(rs.getTimestamp("updateAt"));
            
            evenements.add(evenement);
        }
        return evenements;
    }

    public void update(Evenement evenement) throws SQLException {
        String query = "UPDATE event SET title = ?, description = ?, eventDate = ?, " +
                      "location = ?, categoryEvent = ?, updateAt = ? WHERE id = ?";
        
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setString(1, evenement.getTitle());
        ps.setString(2, evenement.getDescription());
        ps.setTimestamp(3, evenement.getEventDate());
        ps.setString(4, evenement.getLocation());
        ps.setString(5, evenement.getCategoryEvent());
        ps.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
        ps.setInt(7, evenement.getId());

        ps.executeUpdate();
    }

    public void delete(int id) throws SQLException {
        String query = "DELETE FROM event WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    // Rechercher des événements par ville de départ
    public List<Evenement> rechercherParVilleDepart(String villeDepart) throws SQLException {
        List<Evenement> evenements = new ArrayList<>();
        String query = "SELECT * FROM event WHERE location LIKE ?";
        
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setString(1, "%" + villeDepart + "%");
        
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Evenement evt = new Evenement();
            // Remplir l'objet evenement
            evt.setId(rs.getInt("id"));
            evt.setTitle(rs.getString("title"));
            evt.setDescription(rs.getString("description"));
            evt.setEventDate(rs.getTimestamp("eventDate"));
            evt.setLocation(rs.getString("location"));
            evt.setCategoryEvent(rs.getString("categoryEvent"));
            evt.setCreatedAt(rs.getTimestamp("createdAt"));
            evt.setUpdateAt(rs.getTimestamp("updateAt"));
            
            evenements.add(evt);
        }
        return evenements;
    }

    // Trouver les événements à venir
    public List<Evenement> getEvenementsAVenir() throws SQLException {
        List<Evenement> evenements = new ArrayList<>();
        String query = "SELECT * FROM event WHERE eventDate > NOW() AND categoryEvent = 'à venir' ORDER BY eventDate";
        
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(query);
        
        while (rs.next()) {
            Evenement evt = new Evenement();
            // Remplir l'objet evenement
            evt.setId(rs.getInt("id"));
            evt.setTitle(rs.getString("title"));
            evt.setDescription(rs.getString("description"));
            evt.setEventDate(rs.getTimestamp("eventDate"));
            evt.setLocation(rs.getString("location"));
            evt.setCategoryEvent(rs.getString("categoryEvent"));
            evt.setCreatedAt(rs.getTimestamp("createdAt"));
            evt.setUpdateAt(rs.getTimestamp("updateAt"));
            
            evenements.add(evt);
        }
        return evenements;
    }

    // Trouver les événements d'un organisateur
    public List<Evenement> getEvenementsParOrganisateur(int organisateurId) throws SQLException {
        List<Evenement> evenements = new ArrayList<>();
        String query = "SELECT * FROM event WHERE organisateur_id = ?";
        
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setInt(1, organisateurId);
        
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Evenement evt = new Evenement();
            // Remplir l'objet evenement
            evt.setId(rs.getInt("id"));
            evt.setTitle(rs.getString("title"));
            evt.setDescription(rs.getString("description"));
            evt.setEventDate(rs.getTimestamp("eventDate"));
            evt.setLocation(rs.getString("location"));
            evt.setCategoryEvent(rs.getString("categoryEvent"));
            evt.setCreatedAt(rs.getTimestamp("createdAt"));
            evt.setUpdateAt(rs.getTimestamp("updateAt"));
            
            evenements.add(evt);
        }
        return evenements;
    }

    public List<Evenement> rechercheAvancee(String villeDepart, String villeArrivee, 
            Timestamp dateMin, Timestamp dateMax, int placesMin) throws SQLException {
        String query = "SELECT * FROM event WHERE location = ? AND categoryEvent = ? " +
                      "AND eventDate BETWEEN ? AND ? AND places >= ? AND categoryEvent = 'à venir'";
        
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setString(1, villeDepart);
            pst.setString(2, villeArrivee);
            pst.setTimestamp(3, dateMin);
            pst.setTimestamp(4, dateMax);
            pst.setInt(5, placesMin);
            
            ResultSet rs = pst.executeQuery();
            return createEvenementListFromResultSet(rs);
        }
    }

    public Evenement findById(int id) throws SQLException {
        String query = "SELECT * FROM event WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Evenement evt = new Evenement();
                evt.setId(rs.getInt("id"));
                evt.setTitle(rs.getString("title"));
                evt.setDescription(rs.getString("description"));
                evt.setEventDate(rs.getTimestamp("eventDate"));
                evt.setLocation(rs.getString("location"));
                evt.setCategoryEvent(rs.getString("categoryEvent"));
                evt.setCreatedAt(rs.getTimestamp("createdAt"));
                evt.setUpdateAt(rs.getTimestamp("updateAt"));
                return evt;
            }
        }
        return null;
    }

    public void desactiverEvenement(int id) throws SQLException {
        String query = "UPDATE event SET categoryEvent = 'désactivé' WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private List<Evenement> createEvenementListFromResultSet(ResultSet rs) throws SQLException {
        List<Evenement> evenements = new ArrayList<>();
        while (rs.next()) {
            Evenement evt = new Evenement();
            evt.setId(rs.getInt("id"));
            evt.setTitle(rs.getString("title"));
            evt.setDescription(rs.getString("description"));
            evt.setEventDate(rs.getTimestamp("eventDate"));
            evt.setLocation(rs.getString("location"));
            evt.setCategoryEvent(rs.getString("categoryEvent"));
            evt.setCreatedAt(rs.getTimestamp("createdAt"));
            evt.setUpdateAt(rs.getTimestamp("updateAt"));
            evenements.add(evt);
        }
        return evenements;
    }
} 