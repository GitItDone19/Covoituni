package Services;

import entities.Avis;
import utils.MyConnection;
import java.sql.*;
import java.util.ArrayList;

public class AvisService implements IServices<Avis> {
    private Connection connection;

    public AvisService() {
        this.connection = MyConnection.getInstance().getCnx();
    }

    @Override
    public void create(Avis avis) throws SQLException {
        String sql = "INSERT INTO avis (commentaire, note, username, reponse_avis, date_avis) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, avis.getCommentaire());
            ps.setInt(2, avis.getNote());
            ps.setString(3, avis.getUsername());
            ps.setString(4, avis.getReponseAvis());
            ps.setTimestamp(5, new Timestamp(avis.getDateAvis().getTime()));
            ps.executeUpdate();
            
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    avis.setNumAvis(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public void update(Avis avis) throws SQLException {
        String sql = "UPDATE avis SET commentaire=?, note=?, reponse_avis=?, date_avis=? WHERE num_avis=?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, avis.getCommentaire());
            ps.setInt(2, avis.getNote());
            ps.setString(3, avis.getReponseAvis());
            ps.setTimestamp(4, new Timestamp(avis.getDateAvis().getTime()));
            ps.setInt(5, avis.getNumAvis());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Avis avis) throws SQLException {
        String sql = "DELETE FROM avis WHERE num_avis=?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, avis.getNumAvis());
            ps.executeUpdate();
        }
    }

    @Override
    public ArrayList<Avis> readAll() throws SQLException {
        ArrayList<Avis> avisList = new ArrayList<>();
        String sql = "SELECT * FROM avis ORDER BY date_avis DESC";
        
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                Avis avis = new Avis(
                    rs.getString("commentaire"),
                    rs.getInt("note"),
                    rs.getString("username")
                );
                avis.setNumAvis(rs.getInt("num_avis"));
                avis.setReponseAvis(rs.getString("reponse_avis"));
                avis.setDateAvis(new java.util.Date(rs.getTimestamp("date_avis").getTime()));
                avisList.add(avis);
            }
        }
        return avisList;
    }

    public void reply(Avis avis, String response) throws SQLException {
        String sql = "UPDATE avis SET reponse_avis=? WHERE num_avis=?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, response);
            ps.setInt(2, avis.getNumAvis());
            ps.executeUpdate();
        }
    }
} 