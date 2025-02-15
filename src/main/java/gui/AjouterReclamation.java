package gui;

import entities.Reclamation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import Services.ReclamationService;

import java.io.IOException;
import java.sql.SQLException;

public class AjouterReclamation {

    private final ReclamationService reclamationService = new ReclamationService();

    @FXML
    private TextArea descriptionArea;

    @FXML
    void ajouter(ActionEvent event) {
        if (descriptionArea.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Veuillez décrire votre problème");
            alert.showAndWait();
            return;
        }

        Reclamation r = new Reclamation(
            descriptionArea.getText(),
            "EN_COURS",
            1
        );

        try {
            reclamationService.create(r);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setContentText("Réclamation ajoutée avec succès");
            alert.showAndWait();
            clearFields();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void clearFields() {
        descriptionArea.clear();
    }

    @FXML
    void afficher(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherReclamations.fxml"));
            descriptionArea.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Erreur lors du chargement de la page: " + e.getMessage());
            alert.showAndWait();
        }
    }
} 