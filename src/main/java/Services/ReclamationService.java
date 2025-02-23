package Services;

import entities.Reclamation;
import entities.User;
import entities.Role;
import utils.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class ReclamationService {
    private Connection connection;

    public ReclamationService() {
        this.connection = MyConnection.getInstance().getCnx();
    }

    public void create(Reclamation reclamation) throws SQLException {
        String sql = "INSERT INTO reclamation (subject, description, user_id, state, date) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setString(1, reclamation.getSubject());
        pst.setString(2, reclamation.getDescription());
        pst.setInt(3, reclamation.getUser().getId());
        pst.setString(4, reclamation.getState());
        pst.setDate(5, new java.sql.Date(reclamation.getDate().getTime())); // Convert Date to java.sql.Date
        pst.executeUpdate();
    }

    public void update(Reclamation reclamation) throws SQLException {
        String sql = "UPDATE reclamation SET subject=?, description=?, state=?, reply=? WHERE id=?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setString(1, reclamation.getSubject());
            pst.setString(2, reclamation.getDescription());
            pst.setString(3, reclamation.getState());
            pst.setString(4, reclamation.getReply());
            pst.setInt(5, reclamation.getId());
            pst.executeUpdate();
        }
    }

    public ArrayList<Reclamation> readAll() throws SQLException {
        ArrayList<Reclamation> reclamations = new ArrayList<>();
        String sql = "SELECT r.*, u.id as user_id, u.nom, u.prenom, u.email, u.mdp, " +
                     "u.role_code, ro.display_name as role_display, r.reply " +
                     "FROM reclamation r " +
                     "JOIN utilisateur u ON r.user_id = u.id " +
                     "LEFT JOIN role ro ON u.role_code = ro.code";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {
            // Create Role object
            Role role = new Role(
                0, // ID is not needed here
                rs.getString("role_code"),
                rs.getString("role_display")
            );

            // Create User object with Role
            User user = new User(
                rs.getInt("user_id"),
                rs.getString("nom"),
                rs.getString("prenom"),
                "",  // Empty string for tel instead of null
                rs.getString("email"),
                rs.getString("mdp"),
                role,  // Pass the Role object instead of role_code string
                ""    // Empty string for verification code instead of null
            );

            Reclamation reclamation = new Reclamation(
                rs.getInt("id"),
                rs.getString("subject"),
                rs.getString("description"),
                user,
                rs.getString("state"),
                rs.getDate("date"),
                rs.getString("reply")
            );
            reclamations.add(reclamation);
        }
        return reclamations;
    }

    public void delete(Reclamation reclamation) throws SQLException {
        String sql = "DELETE FROM reclamation WHERE id=?";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setInt(1, reclamation.getId());
        pst.executeUpdate();
    }

    // Additional methods for update and other functionalities can be added here
} 