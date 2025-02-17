package main;

import entities.Trajet.Trajet;
import services.Trajet.TrajetService;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import javafx.application.Application;

/**
 * Point d'entrée principal de l'application
 * Cette classe démarre l'application JavaFX et initialise les données de test
 */
public class Main {
    /**
     * Méthode principale qui lance l'application
     * Crée des données de test pour les trajets et démarre l'interface graphique
     * @param args Arguments de ligne de commande (non utilisés)
     */
    public static void main(String[] args) {
        try {
            // Initialisation du service de gestion des trajets
            TrajetService trajetService = new TrajetService();

            // Création des trajets de test
            // Premier trajet : Tunis-Sousse
            Trajet trajet1 = new Trajet();
            trajet1.setTitre("Tunis-Sousse Express");
            trajet1.setDeparturePoint("Tunis");
            trajet1.setArrivalPoint("Sousse");
            trajet1.setDepartureDate(Timestamp.valueOf(LocalDateTime.now().plusDays(1)));
            trajet1.setAvailableSeats(3);
            trajet1.setPrice(25.0);

            // Deuxième trajet : Tunis-Sfax
            Trajet trajet2 = new Trajet();
            trajet2.setTitre("Tunis-Sfax Direct");
            trajet2.setDeparturePoint("Tunis");
            trajet2.setArrivalPoint("Sfax");
            trajet2.setDepartureDate(Timestamp.valueOf(LocalDateTime.now().plusDays(2)));
            trajet2.setAvailableSeats(4);
            trajet2.setPrice(40.0);

            // Sauvegarde des trajets dans la base de données
            trajetService.create(trajet1);
            trajetService.create(trajet2);

            // Affichage des trajets pour vérification
            System.out.println("Liste des trajets :");
            trajetService.readAll().forEach(System.out::println);

            // Test de mise à jour d'un trajet
            Trajet trajetToUpdate = trajetService.readById(1);
            if (trajetToUpdate != null) {
                // Modification du prix du trajet
                trajetToUpdate.setPrice(30.0);
                trajetService.update(trajetToUpdate);
                
                // Affichage du trajet mis à jour
                System.out.println("\nTrajet mis à jour :");
                System.out.println(trajetService.readById(trajetToUpdate.getId()));
            }

        } catch (SQLException e) {
            // Gestion des erreurs de base de données
            System.err.println("Erreur : " + e.getMessage());
        }

        // Démarrage de l'interface graphique JavaFX
        Application.launch(MainFX.class, args);
    }
}
