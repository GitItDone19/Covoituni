package gui.Users;

import entities.Avis;
import entities.User;
import Services.AvisService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.sql.SQLException;
import java.util.Date;

public class AjouterAvisController {
    @FXML private TextField tfRating; // Text field for rating
    @FXML private Button btnSubmit; // Submit button
    @FXML private Button btnRetour; // Return button
    private User currentUser; // The current user (passager)
    private User conducteur; // The selected conducteur
    private AvisService avisService;

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public void setConducteur(User conducteur) {
        this.conducteur = conducteur;
    }

    @FXML
    private void initialize() {
        avisService = new AvisService(); // Initialize the service
    }

    @FXML
    private void handleSubmit() {
        int rating;
        try {
            rating = Integer.parseInt(tfRating.getText().trim());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Validation", "Please enter a valid rating (1-5).");
            return;
        }

        if (rating < 1 || rating > 5) {
            showAlert(Alert.AlertType.WARNING, "Validation", "Rating must be between 1 and 5.");
            return;
        }

        Avis avis = new Avis(0, rating, currentUser, conducteur, new Date());
        try {
            avisService.create(avis);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Avis submitted successfully!");
            tfRating.clear(); // Clear the input field
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error while submitting avis: " + e.getMessage());
        }
    }

    @FXML
    private void handleRetour() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Users/DashboardUser.fxml"));
            Parent root = loader.load();
            DashboardUserController controller = loader.getController();
            controller.setCurrentUser(currentUser); // Pass the current user
            Stage stage = (Stage) btnRetour.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error returning to dashboard: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 