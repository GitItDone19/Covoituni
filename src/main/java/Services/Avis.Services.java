package services;

import entities.Avis;
import entities.MyConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AvisService implements IService<Avis> {

    private Connection cnx;

    public AvisService() {
        cnx = MyConnection.getInstance().getConnection();
    }

    @Override
    public void create(Avis avis) throws SQLException {
        String query = "INSERT INTO avis(commentaire, note, username, reponseAvis, dateAvis) VALUES(?, ?, ?, ?, ?)";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setString(1, avis.getCommentaire());
        ps.setInt(2, avis.getNote());
        ps.setString(3, avis.getUsername());
        ps.setString(4, avis.getReponseAvis());
        ps.setTimestamp(5, new Timestamp(avis.getDateAvis().getTime()));
        ps.executeUpdate();
    }

    @Override
    public void update(Avis avis) throws SQLException {
        String query = "UPDATE avis SET commentaire = ?, note = ?, reponseAvis = ?, dateAvis = ? WHERE numAvis = ?";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setString(1, avis.getCommentaire());
        ps.setInt(2, avis.getNote());
        ps.setString(3, avis.getReponseAvis());
        ps.setTimestamp(4, new Timestamp(avis.getDateAvis().getTime()));
        ps.setInt(5, avis.getNumAvis());
        ps.executeUpdate();
    }

    @Override
    public void delete(Avis avis) throws SQLException {
        String query = "DELETE FROM avis WHERE numAvis = ?";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setInt(1, avis.getNumAvis());
        ps.executeUpdate();
    }

    @Override
    public List<Avis> readAll() throws SQLException {
        List<Avis> avisList = new ArrayList<>();
        String query = "SELECT * FROM avis";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            Avis avis = new Avis(
                rs.getString("commentaire"),
                rs.getInt("note"),
                rs.getString("username")
            );
            avis.setNumAvis(rs.getInt("numAvis"));
            avis.setReponseAvis(rs.getString("reponseAvis"));
            avis.setDateAvis(new Date(rs.getTimestamp("dateAvis").getTime()));
            avisList.add(avis);
        }

        return avisList;
    }

    @Override
    public void reply(Avis avis, String response) throws SQLException {
        String query = "UPDATE avis SET reponseAvis = ? WHERE numAvis = ?";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setString(1, response);
        ps.setInt(2, avis.getNumAvis());
        ps.executeUpdate();
    }
}