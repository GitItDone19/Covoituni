package Services;

import entities.Avis;
import entities.User;
import utils.MyConnection;

import java.sql.*;
import java.util.ArrayList;

public class AvisService {
    private Connection connection;

    public AvisService() {
        this.connection = MyConnection.getInstance().getCnx();
    }

    public void create(Avis avis) throws SQLException {
        String sql = "INSERT INTO avis (rating, passager_id, conducteur_id, date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, avis.getRating());
            pst.setInt(2, avis.getPassager().getId());
            pst.setInt(3, avis.getConducteur().getId());
            pst.setDate(4, new java.sql.Date(avis.getDate().getTime()));
            pst.executeUpdate();
        }
    }

    public ArrayList<Avis> readAll() throws SQLException {
        ArrayList<Avis> avisList = new ArrayList<>();
        String sql = "SELECT a.*, up.nom AS passager_nom, up.prenom AS passager_prenom, " +
                     "uc.nom AS conducteur_nom, uc.prenom AS conducteur_prenom " +
                     "FROM avis a " +
                     "JOIN utilisateur up ON a.passager_id = up.id " +
                     "JOIN utilisateur uc ON a.conducteur_id = uc.id";
        
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                // Create User objects for passager and conducteur
                User passager = new User(
                    rs.getInt("passager_id"),
                    rs.getString("passager_nom"),
                    rs.getString("passager_prenom"),
                    "",
                    "",
                    "",
                    null,
                    ""
                );
                
                User conducteur = new User(
                    rs.getInt("conducteur_id"),
                    rs.getString("conducteur_nom"),
                    rs.getString("conducteur_prenom"),
                    "",
                    "",
                    "",
                    null,
                    ""
                );

                avisList.add(new Avis(
                    rs.getInt("id"),
                    rs.getInt("rating"),
                    passager,
                    conducteur,
                    rs.getDate("date")
                ));
            }
        }
        return avisList;
    }
} 