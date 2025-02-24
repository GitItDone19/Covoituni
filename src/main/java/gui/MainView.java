package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainView implements Initializable {

    @FXML
    private StackPane contentArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Load voitures view by default
        loadVoituresView();
    }

    @FXML
    private void handleVoituresMenuItem() {
        loadVoituresView();
    }

    @FXML
    private void handleCategoriesMenuItem() {
        loadCategoriesView();
    }

    private void loadVoituresView() {
        try {
            Parent view = FXMLLoader.load(getClass().getResource("/AfficherVoitures.fxml"));
            contentArea.getChildren().clear();
            contentArea.getChildren().add(view);
        } catch (IOException e) {
            showError("Erreur", "Impossible de charger la vue des voitures");
        }
    }

    private void loadCategoriesView() {
        try {
            Parent view = FXMLLoader.load(getClass().getResource("/AfficherCategories.fxml"));
            contentArea.getChildren().clear();
            contentArea.getChildren().add(view);
        } catch (IOException e) {
            showError("Erreur", "Impossible de charger la vue des catégories");
        }
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 