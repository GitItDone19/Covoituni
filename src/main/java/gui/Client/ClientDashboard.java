package gui.Client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import java.io.IOException;

public class ClientDashboard {

    @FXML
    private void showReclamations(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/User/AfficherReclamations.fxml"));
            ((Button)event.getSource()).getScene().setRoot(root);
        } catch (IOException e) {
            showError("Erreur lors du chargement des réclamations: " + e.getMessage());
        }
    }

    @FXML
    private void addReclamation(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/User/AjouterReclamation.fxml"));
            ((Button)event.getSource()).getScene().setRoot(root);
        } catch (IOException e) {
            showError("Erreur lors du chargement du formulaire: " + e.getMessage());
        }
    }

    @FXML
    private void showAvis(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/User/AjouterAvis.fxml"));
            ((Button)event.getSource()).getScene().setRoot(root);
        } catch (IOException e) {
            showError("Erreur lors du chargement des avis: " + e.getMessage());
        }
    }

    @FXML
    private void logout(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Login.fxml"));
            ((Button)event.getSource()).getScene().setRoot(root);
        } catch (IOException e) {
            showError("Erreur lors de la déconnexion: " + e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
} 