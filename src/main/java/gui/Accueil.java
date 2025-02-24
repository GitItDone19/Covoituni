package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Modality;

import java.io.IOException;

public class Accueil {
    private Stage voituresStage;
    private Stage categoriesStage;

    @FXML
    private void handleVoituresButton() {
        try {
            if (voituresStage == null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherVoitures.fxml"));
                Parent root = loader.load();
                voituresStage = new Stage();
                voituresStage.setTitle("Gestion des Voitures");
                voituresStage.setScene(new Scene(root));

                // When voitures window is closed, reset the reference
                voituresStage.setOnCloseRequest(e -> voituresStage = null);
            }

            // Bring voitures window to front
            voituresStage.show();
            voituresStage.toFront();

            // Hide categories window if it exists
            if (categoriesStage != null) {
                categoriesStage.hide();
            }

        } catch (IOException e) {
            showError("Erreur", "Impossible d'ouvrir la fenêtre de gestion des voitures");
        }
    }

    @FXML
    private void handleCategoriesButton() {
        try {
            if (categoriesStage == null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherCategories.fxml"));
                Parent root = loader.load();
                categoriesStage = new Stage();
                categoriesStage.setTitle("Gestion des Catégories");
                categoriesStage.setScene(new Scene(root));

                // When categories window is closed, reset the reference
                categoriesStage.setOnCloseRequest(e -> categoriesStage = null);
            }

            // Bring categories window to front
            categoriesStage.show();
            categoriesStage.toFront();

            // Hide voitures window if it exists
            if (voituresStage != null) {
                voituresStage.hide();
            }

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