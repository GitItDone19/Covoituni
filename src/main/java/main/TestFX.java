package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TestFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            // Vérifier si le fichier FXML existe
            var fxmlUrl = getClass().getResource("/.fxml");
            if (fxmlUrl == null) {
                System.err.println("Erreur : Impossible de trouver le fichier AjouterAvis.fxml");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            
            // Vérifier si le fichier CSS existe
            var cssUrl = getClass().getResource("/styles.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            } else {
                System.err.println("Attention : Impossible de trouver le fichier styles.css");
            }

            primaryStage.setTitle("Gestion des Avis et Réclamations");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("Erreur de chargement : " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erreur inattendue : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
