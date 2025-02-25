package main.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private StackPane contentArea;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Charger la liste des annonces par défaut
        loadView("/fxml/ListeAnnonces.fxml");
    }

    @FXML
    private void handleAnnonceClick() {
        loadView("/fxml/ListeAnnonces.fxml");
    }

    @FXML
    private void handleTrajetClick() {
        loadView("/fxml/ListeTrajet.fxml");
    }

    @FXML
    private void handleAjoutAnnonceClick() {
        loadView("/fxml/AjoutAnnonce.fxml");
    }

    @FXML
    private void handleAjoutTrajetClick() {
        loadView("/fxml/AjoutTrajet.fxml");
    }

    @FXML
    private void handleEvenementsClick() {
        loadView("/fxml/ListeEvenements.fxml");
    }

    @FXML
    private void handleReservationsChauffeurClick() {
        loadView("/fxml/ReservationsChauffeur.fxml");
    }

    @FXML
    private void handleReservationsPassagerClick() {
        loadView("/fxml/ReservationsPassager.fxml");
    }

    @FXML
    private void handleHistoriqueClick() {
        loadView("/fxml/Historique.fxml");
    }

    @FXML
    private void showEvenements() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ListeEvenements.fxml"));
            Parent root = loader.load();
            contentArea.getChildren().clear();
            contentArea.getChildren().add(root);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                     "Erreur lors du chargement de la liste des événements");
        }
    }

    private void loadView(String fxml) {
        try {
            URL fxmlUrl = getClass().getResource(fxml);
            if (fxmlUrl == null) {
                showNotImplementedAlert();
                return;
            }
            Parent view = FXMLLoader.load(fxmlUrl);
            contentArea.getChildren().clear();
            contentArea.getChildren().add(view);
        } catch (IOException e) {
            showErrorAlert("Erreur lors du chargement de la vue: " + e.getMessage());
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showNotImplementedAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setContentText("Cette fonctionnalité n'est pas encore implémentée.");
        alert.showAndWait();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 