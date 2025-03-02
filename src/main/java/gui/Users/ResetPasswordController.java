package gui.Users;

import Services.ServiceUser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.io.IOException;

public class ResetPasswordController {

    @FXML private PasswordField tfNewPassword;
    @FXML private PasswordField tfConfirmPassword;

    private String token;
    private ServiceUser serviceuser;

    public void setToken(String token) {
        this.token = token;
    }

    public void initialize() {
        serviceuser = new ServiceUser();
    }

    @FXML
    private void handleReset() {
        String newPassword = tfNewPassword.getText().trim();
        String confirmPassword = tfConfirmPassword.getText().trim();

        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Passwords do not match!");
            return;
        }


        if (newPassword.length() < 6) {
            showAlert(Alert.AlertType.ERROR, "Weak Password", "Password must be at least 6 characters long.");
            return;
        }

        if (serviceuser.isTokenValid(token)) {
            if (serviceuser.resetPassword(token, newPassword)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Your password has been reset successfully!");
                goToLogin();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Something went wrong. Try again.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Invalid Token", "The reset link is invalid or has expired.");
        }
    }

    @FXML
    private void goToLogin() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Users/LoginUser.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) tfNewPassword.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Error navigating to login: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
