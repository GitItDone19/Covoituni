package gui.Admin;

import entities.Reclamation;
import Services.ReclamationService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;

public class ReplyReclamationController {
    @FXML private TextArea taReply; // Text area for the reply
    @FXML private Button btnSubmit; // Submit button
    @FXML private Button btnRetour; // Return button
    @FXML private RadioButton rbResolved; // Radio button for resolved status
    @FXML private RadioButton rbRejected; // Radio button for rejected status

    private Reclamation reclamation; // The reclamation being replied to
    private ReclamationService reclamationService;

    public void setReclamation(Reclamation reclamation) {
        this.reclamation = reclamation;
    }

    @FXML
    private void initialize() {
        reclamationService = new ReclamationService(); // Initialize the service
    }

    @FXML
    private void handleSubmit() {
        String replyText = taReply.getText().trim();

        if (replyText.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation", "Veuillez écrire une réponse.");
            return;
        }

        // Update the reclamation with the new reply
        reclamation.setReply(replyText); // Set the reply in the reclamation object
        try {
            reclamationService.update(reclamation); // Update the reclamation in the database
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Réponse soumise avec succès !");
            taReply.clear(); // Clear the text area after submission
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la mise à jour: " + e.getMessage());
        }
    }

    @FXML
    private void handleRetour() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/AdminDashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnRetour.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
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