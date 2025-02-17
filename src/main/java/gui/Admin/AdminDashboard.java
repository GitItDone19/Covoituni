package gui.Admin;

import entities.Reclamation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import Services.Reclamation.ReclamationService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.ResourceBundle;

public class AdminDashboard implements Initializable {
    
    @FXML
    private ListView<Reclamation> reclamationList;
    
    private final ReclamationService reclamationService = new ReclamationService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        updateList();
    }

    private void updateList() {
        try {
            reclamationList.getItems().clear();
            reclamationList.getItems().addAll(reclamationService.readAll());
        } catch (SQLException e) {
            showError("Erreur lors du chargement des réclamations: " + e.getMessage());
        }
    }

    private void showDetails(Reclamation reclamation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/ReclamationDetails.fxml"));
            Parent root = loader.load();
            
            ReclamationDetails controller = loader.getController();
            controller.setReclamation(reclamation);
            
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            
            reclamationList.getScene().setRoot(root);
        } catch (IOException e) {
            showError("Erreur lors de l'ouverture des détails: " + e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setContentText(message);
        alert.showAndWait();
    }
} 