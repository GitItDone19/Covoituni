package gui.Users;

import Services.ServiceUser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.commons.mail.EmailException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

public class ForgotPasswordController {

    @FXML private TextField tfEmail;
    private ServiceUser serviceUser;

    public void initialize() {
        serviceUser = new ServiceUser();
    }

    @FXML
    private void handleResetPassword() throws SQLException {
        String email = tfEmail.getText().trim();

        if (email.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Email", "Please enter a valid email address.");
            return;
        }

        if (!serviceUser.emailExists(email)) {
            showAlert(Alert.AlertType.ERROR, "Email Not Found", "This email is not registered.");
            return;
        }

        // Generate a unique token
        String token = UUID.randomUUID().toString();

        // Store the token in the database with an expiration time
        serviceUser.storeResetToken(email, token);

        // Send the reset email
        try {
            sendResetEmail(email, token);
            showAlert(Alert.AlertType.INFORMATION, "Email Sent", "A password reset link has been sent to your email.");
        } catch (EmailException e) {
            showAlert(Alert.AlertType.ERROR, "Email Error", "Failed to send reset email. Please try again.");
            e.printStackTrace();
        }
    }

    private void sendResetEmail(String email, String token) throws EmailException {
        // Use a local URL for your JavaFX application
        String resetLink = "http://localhost:8000/reset-password?token=" + token;

        org.apache.commons.mail.HtmlEmail mail = new org.apache.commons.mail.HtmlEmail();
        mail.setHostName("smtp.gmail.com");
        mail.setSmtpPort(587);
        
        // Replace with your App Password (16 characters, no spaces)
        mail.setAuthentication("hammamahmoud06@gmail.com", "your-16-character-app-password");
        mail.setStartTLSRequired(true);
        
        mail.setFrom("hammamahmoud06@gmail.com", "Covoituni Password Reset");
        mail.setSubject("Password Reset Request");

        // Email Body
        String htmlMsg = "<html><body>"
                + "<p>Click the link below to reset your password:</p>"
                + "<a href='" + resetLink + "'>Reset Password</a>"
                + "<p>If you didn't request this, please ignore this email.</p>"
                + "</body></html>";

        mail.setHtmlMsg(htmlMsg);
        mail.addTo(email);
        mail.send();

        System.out.println("Reset email sent successfully to: " + email);
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
