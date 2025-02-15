package gui;

import entities.Reclamation;
import Services.ReclamationService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;

public class AjouterReclamationController {

    @FXML private TextArea descriptionArea;
    
    private int userId;
    private final ReclamationService reclamationService = new ReclamationService();
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    @FXML
    private void handleSubmit() {
        if (descriptionArea.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez décrire votre problème");
            return;
        }

        try {
            Reclamation reclamation = new Reclamation(
                descriptionArea.getText(),
                "EN_ATTENTE",  // Default status
                Date.valueOf(LocalDate.now()),
                userId
            );
            
            reclamationService.create(reclamation);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Réclamation envoyée avec succès");
            handleBack();
            
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", e.getMessage());
        }
    }
    
    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserReclamations.fxml"));
            Parent root = loader.load();
            
            UserReclamationsController controller = loader.getController();
            // We'll need to pass the user back through a service or session manager
            
            descriptionArea.getScene().setRoot(root);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur de navigation: " + e.getMessage());
        }
    }
    
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 