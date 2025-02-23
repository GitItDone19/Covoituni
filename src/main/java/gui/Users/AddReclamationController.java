package gui.Users;

import entities.User;
import entities.Reclamation;
import Services.ReclamationService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;

import java.util.Date;
import java.sql.SQLException;
import java.io.IOException;
import java.util.List;

public class AddReclamationController {
    @FXML private TextField tfSubject; // Subject of the reclamation
    @FXML private TextArea taDescription; // Description of the reclamation
    @FXML private Button btnSubmit; // Submit button
    @FXML private Button btnRetour; // Retour button
    @FXML private Button btnVoirReclamations; // Voir Réclamations button
    @FXML private Label lblUserName; // To display the current user's name

    private User currentUser;
    private ReclamationService reclamationService;

    public void setCurrentUser(User user) {
        this.currentUser = user;
        if (lblUserName != null) {
            lblUserName.setText("Réclamation pour: " + user.getNom() + " " + user.getPrenom());
        }
    }

    @FXML
    private void initialize() {
        reclamationService = new ReclamationService(); // Initialize the service
    }

    @FXML
    private void handleSubmit() {
        String subject = tfSubject.getText().trim();
        String description = taDescription.getText().trim();

        if (subject.isEmpty() || description.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation", "Please fill in all fields.");
            return;
        }

        // Create a new reclamation object
        Reclamation reclamation = new Reclamation(0, subject, description, currentUser, "pending", new Date(),null);

        try {
            // Save the reclamation using the service
            reclamationService.create(reclamation);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Reclamation submitted successfully!");

            // Clear the form
            tfSubject.clear();
            taDescription.clear();

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error while submitting reclamation: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRetour() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Users/DashboardUser.fxml"));
            Parent root = loader.load();

            DashboardUserController controller = loader.getController();
            controller.setCurrentUser(currentUser);

            Scene scene = new Scene(root);
            Stage stage = (Stage) btnRetour.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error returning to dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleVoirReclamations() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Users/ViewReclamations.fxml"));
            Parent root = loader.load();

            ViewReclamationsController controller = loader.getController();
            controller.setCurrentUser(currentUser); // Pass the current user

            Scene scene = new Scene(root);
            Stage stage = (Stage) btnVoirReclamations.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Mes Réclamations"); // Set the title for the new page
            stage.show();

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error loading reclamations page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleButtonHover(MouseEvent event) {
        Button button = (Button) event.getSource();
        if (button == btnSubmit) {
            button.setStyle("-fx-background-color: #075c39; -fx-text-fill: white; -fx-font-size: 14px; " +
                           "-fx-font-weight: bold; -fx-padding: 12 30; -fx-background-radius: 8; " +
                           "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 4, 0, 0, 2);");
        } else if (button == btnVoirReclamations) {
            button.setStyle("-fx-background-color: #0069d9; -fx-text-fill: white; -fx-font-size: 14px; " +
                           "-fx-font-weight: bold; -fx-padding: 12 30; -fx-background-radius: 8; " +
                           "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 4, 0, 0, 2);");
        } else if (button == btnRetour) {
            button.setStyle("-fx-background-color: #c0c0c0; -fx-text-fill: #505050; -fx-font-size: 14px; " +
                           "-fx-font-weight: bold; -fx-padding: 12 30; -fx-background-radius: 8; " +
                           "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 4, 0, 0, 2);");
        }
    }

    @FXML
    private void handleButtonExit(MouseEvent event) {
        Button button = (Button) event.getSource();
        if (button == btnSubmit) {
            button.setStyle("-fx-background-color: #098f5a; -fx-text-fill: white; -fx-font-size: 14px; " +
                           "-fx-font-weight: bold; -fx-padding: 12 30; -fx-background-radius: 8; " +
                           "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 2);");
        } else if (button == btnVoirReclamations) {
            button.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-size: 14px; " +
                           "-fx-font-weight: bold; -fx-padding: 12 30; -fx-background-radius: 8; " +
                           "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 2);");
        } else if (button == btnRetour) {
            button.setStyle("-fx-background-color: #dcdcdc; -fx-text-fill: #505050; -fx-font-size: 14px; " +
                           "-fx-font-weight: bold; -fx-padding: 12 30; -fx-background-radius: 8; " +
                           "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 2);");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}