package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.MainViewController;
import java.io.IOException;

/**
 * Classe principale de l'application JavaFX
 * Gère le démarrage et la configuration initiale de l'application
 */
public class MainFX extends Application {
    private Stage primaryStage;
    private VBox mainContent;  // Référence au conteneur principal
    private MainViewController mainController;

    /**
     * Point d'entrée de l'application JavaFX
     * Configure et affiche la fenêtre principale
     * @param primaryStage La fenêtre principale
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        
        try {
            // Chargement du FXML principal
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainView.fxml"));
            Parent root = loader.load();
            
            // Récupération du contrôleur et du conteneur principal
            mainController = loader.getController();
            mainController.setMainFX(this);
            
            // Configuration de la scène
            Scene scene = new Scene(root, 1200, 800);
            scene.getStylesheets().add(getClass().getResource("/styles/bolt-style.css").toExternalForm());
            
            // Configuration de la fenêtre
            primaryStage.setTitle("Application de covoiturage");
            primaryStage.setScene(scene);
            primaryStage.show();
            
            // Affichage initial des annonces
            showView("Annonce/AnnoncesView.fxml");
            
        } catch (IOException e) {
            System.err.println("Erreur de chargement du fichier FXML : " + e.getMessage());
        }
    }

    /**
     * Charge et affiche une vue dans la zone de contenu principale
     * @param fxmlPath Chemin vers le fichier FXML à charger
     */
    public void showView(String fxmlPath) {
        try {
            System.out.println("Tentative de chargement de : " + fxmlPath);
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + fxmlPath));
            if (loader.getLocation() == null) {
                throw new IOException("Fichier FXML non trouvé : " + fxmlPath);
            }
            
            Parent view = loader.load();
            System.out.println("Vue chargée avec succès");
            
            VBox mainContent = mainController.getMainContent();
            if (mainContent != null) {
                mainContent.getChildren().clear();
                mainContent.getChildren().add(view);
                System.out.println("Vue ajoutée au conteneur principal");
            } else {
                System.err.println("mainContent est null!");
            }
            
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de " + fxmlPath + ":");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
