package gui.Admin;

import entities.Reclamation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import Services.Reclamation.ReclamationService;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class ReclamationDetails {
    @FXML
    private Label idLabel;
    @FXML
    private Label usernameLabel;
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
        // Initialize status options
        statusComboBox.getItems().addAll("EN_COURS", "RESOLUE");
    }
    
    public void setReclamation(Reclamation reclamation) {
        this.reclamation = reclamation;
        
        idLabel.setText(String.valueOf(reclamation.getId()));
        usernameLabel.setText(reclamation.getUsername());
        
        // Format the date
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        dateLabel.setText(sdf.format(reclamation.getDate()));
        
        descriptionLabel.setText(reclamation.getDescription());
        statusComboBox.setValue(reclamation.getStatus());
        
        if (reclamation.getAdminReply() != null) {
            replyTextArea.setText(reclamation.getAdminReply());
        }
    }
    
    @FXML
    private void handleSave(ActionEvent event) {
        if (statusComboBox.getValue() == null || replyTextArea.getText().trim().isEmpty()) {
            showError("Veuillez remplir tous les champs");
            return;
        }
        
        reclamation.setStatus(statusComboBox.getValue());
        reclamation.setAdminReply(replyTextArea.getText().trim());
        
        try {
            reclamationService.update(reclamation);
            showSuccess("Réponse enregistrée avec succès");
            retourDashboard();
        } catch (SQLException e) {
            showError(e.getMessage());
        }
    }
    
    @FXML
    private void retourDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/DashboardReclamationAdmin.fxml"));
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
    
    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setContentText(message);
        alert.showAndWait();
    }
} 