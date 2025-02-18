package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import services.MainViewController;
import javafx.scene.layout.BorderPane;
import java.io.IOException;

/**
 * Classe principale de l'application JavaFX
 * Gère le démarrage et la configuration initiale de l'application
 */
public class MainFX extends Application {
    private Stage primaryStage;
    private MainViewController mainController;
    private BorderPane mainContent;  // Changé de VBox à BorderPane

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
            mainContent = mainController.getMainContent();  // Utilisation de BorderPane
            
            // Configuration de la scène
            Scene scene = new Scene(root, 1200, 800);
            
            // Chargement du CSS
            String cssPath = getClass().getResource("/styles/bolt-style.css").toExternalForm();
            System.out.println("Chargement du CSS: " + cssPath); // Debug
            scene.getStylesheets().clear(); // Nettoyer les styles existants
            scene.getStylesheets().add(cssPath);
            
            // Configuration de la fenêtre
            primaryStage.setTitle("Application de covoiturage");
            primaryStage.setScene(scene);
            primaryStage.show();
            
            // Affichage initial des annonces
            showView("Annonce/AnnoncesView.fxml");
            
        } catch (IOException e) {
            System.err.println("Erreur de chargement : " + e.getMessage());
            e.printStackTrace();
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
            
            // Appliquer le CSS à la nouvelle vue
            String cssPath = getClass().getResource("/styles/bolt-style.css").toExternalForm();
            view.getStylesheets().clear();
            view.getStylesheets().add(cssPath);
            
            System.out.println("Vue chargée avec succès");
            
            mainContent.setCenter(view);  // Utilisation de setCenter au lieu de getChildren().setAll()
            
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de " + fxmlPath + ":");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
