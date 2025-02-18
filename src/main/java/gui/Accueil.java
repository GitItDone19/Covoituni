package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Accueil {

    @FXML
    private void handleVoituresButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherVoitures.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Gestion des Voitures");
            stage.setScene(new Scene(root));
            stage.show();
            
            // Fermer la fenêtre d'accueil
            ((Stage) ((javafx.scene.Node) (loader.getRoot())).getScene().getWindow()).close();
        } catch (IOException e) {
            showError("Erreur", "Impossible d'ouvrir la fenêtre de gestion des voitures");
        }
    }

    @FXML
    private void handleCategoriesButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherCategories.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Gestion des Catégories");
            stage.setScene(new Scene(root));
            stage.show();
            
            // Fermer la fenêtre d'accueil
            ((Stage) ((javafx.scene.Node) (loader.getRoot())).getScene().getWindow()).close();
        } catch (IOException e) {
            showError("Erreur", "Impossible d'ouvrir la fenêtre de gestion des catégories");
        }
    }

    private void showError(String title, String content) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 