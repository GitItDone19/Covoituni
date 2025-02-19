package gui.Users;

import User.ServiceUser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;

public class ForgotPasswordController {
    
    @FXML private TextField tfEmail;
    private ServiceUser serviceUser;
    
    public void initialize() {
        serviceUser = new ServiceUser();
    }
    
    @FXML
    private void handleResetPassword() {
        String email = tfEmail.getText().trim();
        
        if (email.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation", 
                "Veuillez entrer votre email");
            return;
        }
        
        try {
            // Check if email exists
            if (serviceUser.emailExists(email)) {
                // TODO: Implement password reset logic
                // For now, just show a success message
                showAlert(Alert.AlertType.INFORMATION, "Succès", 
                    "Un email de réinitialisation a été envoyé à " + email);
                handleBackToLogin();
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", 
                    "Aucun compte n'est associé à cet email");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Erreur lors de la réinitialisation: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleBackToLogin() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Users/LoginUser.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) tfEmail.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Erreur de navigation: " + e.getMessage());
        }
    }
    
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 