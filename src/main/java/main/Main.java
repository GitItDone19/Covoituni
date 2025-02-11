package main;

import entities.Notification;
import service.NotificationService;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        NotificationService notificationService = new NotificationService();

        try {
            // *** Étape 1 : Ajouter 3 notifications ***
            Notification notif1 = new Notification(1, 101, "Votre trajet est confirmé.", new Timestamp(System.currentTimeMillis()), false);
            Notification notif2 = new Notification(2, 102, "Un nouveau trajet est disponible.", new Timestamp(System.currentTimeMillis()), false);
            Notification notif3 = new Notification(3, 103, "Votre réservation a été annulée.", new Timestamp(System.currentTimeMillis()), false);

            notificationService.create(notif1);
            notificationService.create(notif2);
            notificationService.create(notif3);

            System.out.println(" 3 notifications ajoutées avec succès.");

            // *** Étape 2 : Afficher toutes les notifications ***
            System.out.println("\n Liste des notifications AVANT modification:");
            List<Notification> notifications = notificationService.readAll();
            for (Notification n : notifications) {
                System.out.println(n);
            }

            // *** Étape 3 : Mise à jour d'une notification ***
            notif2.setMessage("Un trajet que vous suivez a été modifié.");
            notif2.setRead(true);
            notificationService.update(notif2);
            System.out.println("\n Notification 2 mise à jour avec succès.");

            // *** Étape 4 : Suppression d'une notification ***
            notificationService.delete(notif3);
            System.out.println(" Notification 3 supprimée avec succès.");

            // *** Étape 5 : Afficher toutes les notifications après modification ***
            System.out.println("\n Liste des notifications APRÈS modification:");
            notifications = notificationService.readAll();
            for (Notification n : notifications) {
                System.out.println(n);
            }

        } catch (SQLException e) {
            System.err.println(" Erreur SQL : " + e.getMessage());
        }
    }
}
