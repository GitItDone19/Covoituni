package gui.Users;

import entities.Reclamation;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;
import javafx.scene.control.Alert;
import entities.User;

public class DetailReclamationController {
    @FXML private Label lblSubject; // Label for the subject
    @FXML private TextArea taDescription; // Text area for the description
    @FXML private Label lblStatus; // Label for the status
    @FXML private Label lblReply; // Label for the reply

    private Reclamation reclamation; // The reclamation being viewed
    private User currentUser;

    public void setReclamation(Reclamation reclamation) {
        this.reclamation = reclamation;
        displayReclamationDetails();
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    private void displayReclamationDetails() {
        lblSubject.setText(reclamation.getSubject());
        taDescription.setText(reclamation.getDescription());
        lblStatus.setText(reclamation.getState());

        // Display the reply if it exists
        if (reclamation.getReply() != null && !reclamation.getReply().isEmpty()) {
            lblReply.setText("Réponse: " + reclamation.getReply());
        } else {
            lblReply.setText("Aucune réponse disponible.");
        }
    }

    @FXML
    private void handleRetour() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Users/DashboardUser.fxml"));
            Parent root = loader.load();
            
            DashboardUserController controller = loader.getController();
            controller.setCurrentUser(currentUser);
            
            Stage stage = (Stage) lblSubject.getScene().getWindow();
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