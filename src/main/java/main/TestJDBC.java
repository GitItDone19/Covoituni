package main;

import entities.Car;
import entities.Categorie;
import services.CarService;
import services.CategorieService;
import utils.DataSource;

import java.sql.SQLException;
import java.util.Date;

public class TestJDBC {

    public static void main(String[] args) {
        // Test de la connexion à la base de données
        DataSource.getInstance();

        CarService carService = new CarService();
        CategorieService categorieService = new CategorieService();

        try {
            // Créer une catégorie
            Categorie categorie = new Categorie("Test Catégorie", "Description de test");
            categorieService.create(categorie);
            System.out.println("Catégorie créée avec l'ID: " + categorie.getId());

            // Créer une voiture avec la catégorie
            Car car = new Car(
                "TEST123",
                "Voiture de test",
                new Date(),
                "Bleu",
                "Renault",
                "Clio",
                categorie.getId()
            );
            carService.create(car);
            System.out.println("Voiture créée avec l'ID: " + car.getId());

            // Afficher toutes les voitures
            System.out.println("\nListe des voitures :");
            carService.readAll().forEach(System.out::println);

            // Afficher toutes les catégories
            System.out.println("\nListe des catégories :");
            categorieService.readAll().forEach(System.out::println);

        } catch (SQLException e) {
            System.err.println("Erreur SQL : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
