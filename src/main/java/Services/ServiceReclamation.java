package Services;

import entities.Reclamation;
import entities.MyConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceReclamation implements IServices<Reclamation> {

    private Connection cnx;

    public ServiceReclamation() {
        cnx = MyConnection.getInstance().getCnx();
    }

    @Override
    public void create(Reclamation reclamation) throws SQLException {
        String query = "INSERT INTO reclamation(titre, sujet, username, reponseRec, dateReclamation) " +
                "VALUES(?, ?, ?, ?, ?)";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setString(1, reclamation.getTitre());
        ps.setString(2, reclamation.getSujet());
        ps.setString(3, reclamation.getUsername());
        ps.setString(4, reclamation.getReponseRec());
        ps.setTimestamp(5, new Timestamp(reclamation.getDateReclamation().getTime()));
        ps.executeUpdate();
    }

    @Override
    public void update(Reclamation reclamation) throws SQLException {
        String query = "UPDATE reclamation SET titre = ?, sujet = ?, reponseRec = ?, dateReclamation = ? WHERE numRec = ?";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setString(1, reclamation.getTitre());
        ps.setString(2, reclamation.getSujet());
        ps.setString(3, reclamation.getReponseRec());
        ps.setTimestamp(4, new Timestamp(reclamation.getDateReclamation().getTime()));
        ps.setInt(5, reclamation.getNumRec());
        ps.executeUpdate();
    }

    @Override
    public void delete(Reclamation reclamation) throws SQLException {
        String query = "DELETE FROM reclamation WHERE numRec = ?";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setInt(1, reclamation.getNumRec());
        ps.executeUpdate();
    }

    @Override
    public List<Reclamation> readAll() throws SQLException {
        List<Reclamation> reclamations = new ArrayList<>();
        String query = "SELECT * FROM reclamation";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            int numRec = rs.getInt("numRec");
            String titre = rs.getString("titre");
            String sujet = rs.getString("sujet");
            String reponseRec = rs.getString("reponseRec");
            Timestamp dateReclamationTimestamp = rs.getTimestamp("dateReclamation");
            String username = rs.getString("username");

            Reclamation reclamation = new Reclamation(titre, sujet, username);
            reclamation.setNumRec(numRec);
            reclamation.setReponseRec(reponseRec);
            reclamation.setDateReclamation(new Date(dateReclamationTimestamp.getTime()));
            reclamations.add(reclamation);
        }

        return reclamations;
    }

    // Méthode pour répondre à une réclamation
    public void reply(Reclamation reclamation, String response) throws SQLException {
        String query = "UPDATE reclamation SET reponseRec = ? WHERE numRec = ?";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setString(1, response);
        ps.setInt(2, reclamation.getNumRec());
        ps.executeUpdate();
    }
}


