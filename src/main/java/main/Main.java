package main;

import entities.Annonce;
import entities.Trajet;
import services.AnnonceService;
import services.TrajetService;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        AnnonceService annonceService = new AnnonceService();
        TrajetService trajetService = new TrajetService();

        try {
            // *** Étape 1 : Création de 3 annonces ***
            Annonce annonce1 = new Annonce(
                    "Trajet Tunis-Sousse", "Voyage confortable",
                    new Date(System.currentTimeMillis()),
                    1, 1, new Date(System.currentTimeMillis()),
                    "Tunis", "Sousse", "OPEN"
            );

            Annonce annonce2 = new Annonce(
                    "Trajet Tunis-Djerba", "Voyage agréable",
                    new Date(System.currentTimeMillis()),
                    1, 1, new Date(System.currentTimeMillis()),
                    "Tunis", "Djerba", "OPEN"
            );

            Annonce annonce3 = new Annonce(
                    "Trajet Tunis-Nabeul", "Voyage rapide",
                    new Date(System.currentTimeMillis()),
                    1, 1, new Date(System.currentTimeMillis()),
                    "Tunis", "Nabeul", "OPEN"
            );

            annonceService.create(annonce1);
            annonceService.create(annonce2);
            annonceService.create(annonce3);

            System.out.println(" 3 annonces ajoutées avec succès.");

            // *** Étape 2 : Création de 3 trajets liés aux annonces ***
            Trajet trajet1 = new Trajet(annonce1.getId(), 3, 10.5, new Date(System.currentTimeMillis()));
            Trajet trajet2 = new Trajet(annonce2.getId(), 4, 25.5, new Date(System.currentTimeMillis()));
            Trajet trajet3 = new Trajet(annonce3.getId(), 5, 15.5, new Date(System.currentTimeMillis()));

            trajetService.create(trajet1);
            trajetService.create(trajet2);
            trajetService.create(trajet3);

            System.out.println(" 3 trajets ajoutés avec succès.");

            // *** Affichage des annonces et trajets ***
            System.out.println("\n Liste des annonces AVANT modification:");
            for (Annonce a : annonceService.readAll()) {
                System.out.println(a);
            }

            System.out.println("\n Liste des trajets AVANT modification:");
            for (Trajet t : trajetService.readAll()) {
                System.out.println(t);
            }

            // *** Étape 3 : Mise à jour d'une annonce et d'un trajet ***
            annonce2.setDescription("Voyage premium avec sièges confortables.");
            annonce2.setStatus("CLOSED"); // Changer le statut de l'annonce
            annonceService.update(annonce2);
            System.out.println("\n Annonce 2 mise à jour avec succès.");

            trajet2.setAvailableSeats(2);
            trajet2.setPrice(22.5);
            trajetService.update(trajet2);
            System.out.println(" Trajet 2 mis à jour avec succès.");

            // *** Affichage après mise à jour ***
            System.out.println("\n Liste des annonces APRÈS modification:");
            for (Annonce a : annonceService.readAll()) {
                System.out.println(a);
            }

            System.out.println("\n Liste des trajets APRÈS modification:");
            for (Trajet t : trajetService.readAll()) {
                System.out.println(t);
            }

            // *** Étape 4 : Suppression d'une annonce et d'un trajet ***
            annonceService.delete(annonce3);
            System.out.println("\n Annonce 3 supprimée avec succès.");

            trajetService.delete(trajet3);
            System.out.println(" Trajet 3 supprimé avec succès.");

            // *** Affichage après suppression ***
            System.out.println("\n Liste finale des annonces:");
            for (Annonce a : annonceService.readAll()) {
                System.out.println(a);
            }

            System.out.println("\n Liste finale des trajets:");
            for (Trajet t : trajetService.readAll()) {
                System.out.println(t);
            }

        } catch (SQLException e) {
            System.err.println(" Erreur SQL : " + e.getMessage());
        }
    }
}
