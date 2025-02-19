package Services.Avis;

import Services.IService;
import entities.Avis;
import entities.User;
import entities.Role;
import utils.MyConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AvisService implements IService<Avis> {

    private Connection cnx;

    public AvisService() {
        cnx = MyConnection.getInstance().getCnx();
    }

    @Override
    public void create(Avis avis) throws SQLException {
        String query = "INSERT INTO avis(commentaire, rating, date_avis, user_id) VALUES(?, ?, ?, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setString(1, avis.getCommentaire());
            ps.setInt(2, avis.getRating());
            ps.setTimestamp(3, new Timestamp(avis.getDateAvis().getTime()));
            ps.setInt(4, avis.getUser().getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void update(Avis avis) throws SQLException {
        String query = "UPDATE avis SET commentaire = ?, rating = ?, reponse_avis = ?, date_avis = ? WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setString(1, avis.getCommentaire());
            ps.setInt(2, avis.getRating());
            ps.setString(3, avis.getReponseAvis());
            ps.setTimestamp(4, new Timestamp(avis.getDateAvis().getTime()));
            ps.setInt(5, avis.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Avis avis) throws SQLException {
        String query = "DELETE FROM avis WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setInt(1, avis.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public List<Avis> readAll() throws SQLException {
        List<Avis> avisList = new ArrayList<>();
        String query = "SELECT a.*, u.*, r.id as role_id, r.code as role_code, r.display_name " +
                      "FROM avis a " +
                      "JOIN utilisateur u ON a.user_id = u.id " +
                      "JOIN role r ON u.role_code = r.code " +
                      "ORDER BY a.date_avis DESC";
        
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            
            while (rs.next()) {
                Role role = new Role(
                    rs.getInt("role_id"),
                    rs.getString("role_code"),
                    rs.getString("display_name")
                );

                User user = new User(
                    rs.getInt("user_id"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("tel"),
                    rs.getString("email"),
                    rs.getString("mdp"),
                    role,
                    rs.getString("verificationcode")
                );
                user.setRating(rs.getDouble("rating"));
                user.setTripsCount(rs.getInt("trips_count"));

                Avis avis = new Avis();
                avis.setId(rs.getInt("id"));
                avis.setCommentaire(rs.getString("commentaire"));
                avis.setRating(rs.getInt("rating"));
                avis.setDateAvis(rs.getTimestamp("date_avis"));
                avis.setUser(user);
                
                avisList.add(avis);
            }
        }
        return avisList;
    }

    public void reply(Avis avis, String response) throws SQLException {
        String query = "UPDATE avis SET reponse_avis = ? WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setString(1, response);
            ps.setInt(2, avis.getId());
            ps.executeUpdate();
        }
    }
} 