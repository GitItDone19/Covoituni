package service;

import entities.Notification;
import utils.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationService {

    private Connection cnx;

    public NotificationService() {
        cnx = MyConnection.getInstance().getConnection();
    }

    // Ajouter une notification
    public void create(Notification notification) throws SQLException {
        String query = "INSERT INTO notifications (type, user_id, message, timeStamp, is_read) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = cnx.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, notification.getType());
        ps.setInt(2, notification.getUserId());
        ps.setString(3, notification.getMessage());
        ps.setTimestamp(4, notification.getTimeStamp());
        ps.setBoolean(5, notification.isRead());

        ps.executeUpdate();

        // Récupérer l'ID généré automatiquement
        ResultSet generatedKeys = ps.getGeneratedKeys();
        if (generatedKeys.next()) {
            notification.setId(generatedKeys.getInt(1));
        }
    }

    // Lire toutes les notifications
    public List<Notification> readAll() throws SQLException {
        List<Notification> notifications = new ArrayList<>();
        String query = "SELECT * FROM notifications";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            Notification notification = new Notification(
                    rs.getInt("id"),
                    rs.getInt("type"),
                    rs.getInt("user_id"),
                    rs.getString("message"),
                    rs.getTimestamp("timeStamp"),
                    rs.getBoolean("is_read")
            );
            notifications.add(notification);
        }
        return notifications;
    }

    // Mettre à jour une notification
    public void update(Notification notification) throws SQLException {
        String query = "UPDATE notifications SET message = ?, is_read = ? WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setString(1, notification.getMessage());
        ps.setBoolean(2, notification.isRead());
        ps.setInt(3, notification.getId());

        int rowsUpdated = ps.executeUpdate();
        if (rowsUpdated == 0) {
            System.out.println("⚠ Aucune notification mise à jour. Vérifiez l'ID.");
        }
    }

    // Supprimer une notification
    public void delete(Notification notification) throws SQLException {
        String query = "DELETE FROM notifications WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setInt(1, notification.getId());

        int rowsDeleted = ps.executeUpdate();
        if (rowsDeleted == 0) {
            System.out.println("⚠ Aucune notification supprimée. Vérifiez l'ID.");
        }
    }
}
