package gui;

import entities.Reclamation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import Services.ReclamationService;

import java.io.IOException;
import java.sql.SQLException;

public class ReclamationDetails {
    @FXML
    private Label idLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private ComboBox<String> statusComboBox;
    @FXML
    private TextArea replyTextArea;
    
    private Reclamation reclamation;
    private final ReclamationService reclamationService = new ReclamationService();
    
    @FXML
    public void initialize() {
        // Initialize status options (only EN_COURS and RESOLUE)
        statusComboBox.getItems().addAll("EN_COURS", "RESOLUE");
    }
    
    public void setReclamation(Reclamation reclamation) {
        this.reclamation = reclamation;
        
        // Populate fields
        idLabel.setText(String.valueOf(reclamation.getId()));
        dateLabel.setText(reclamation.getDateReclamation().toString());
        descriptionLabel.setText(reclamation.getDescription());
        statusComboBox.setValue(reclamation.getStatus());
        replyTextArea.setText(reclamation.getAdminReply());
    }
    
    @FXML
    private void handleSave() {
        if (statusComboBox.getValue() == null || replyTextArea.getText().trim().isEmpty()) {
            showError("Veuillez remplir tous les champs");
            return;
        }
        
        reclamation.setStatus(statusComboBox.getValue());
        reclamation.setAdminReply(replyTextArea.getText().trim());
        
        try {
            reclamationService.update(reclamation);
            showInfo("Réponse enregistrée avec succès");
            handleCancel(); // Return to dashboard
        } catch (SQLException e) {
            showError(e.getMessage());
        }
    }
    
    @FXML
    private void handleCancel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminDashboard.fxml"));
            Parent root = loader.load();
            replyTextArea.getScene().setRoot(root);
        } catch (IOException e) {
            showError("Erreur lors du retour au tableau de bord: " + e.getMessage());
        }
    }
    
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setContentText(message);
        alert.showAndWait();
    }
} 