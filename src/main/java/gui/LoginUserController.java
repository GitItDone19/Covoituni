package gui;

import entities.User;
import Services.ServiceUser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.application.Platform;

public class LoginUserController implements Initializable {
    
    @FXML private TextField tfEmail;
    @FXML private PasswordField tfPassword;
    @FXML private Button btnLogin;
    
    private ServiceUser serviceUser = new ServiceUser();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize any other necessary components
    }
    
    @FXML
    private void handleLogin() {
        String email = tfEmail.getText();
        String password = tfPassword.getText();
        
        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs");
            return;
        }
        
        try {
            User user = serviceUser.findByEmail(email);
            if (user != null && user.getMdp().equals(password)) {
                if ("admin".equals(user.getRoleCode())) {
                    loadAdminDashboard();
                } else {
                    loadUserDashboard(user);
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Email ou mot de passe incorrect");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Erreur de connexion: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/RegisterUser.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) tfEmail.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur de navigation: " + e.getMessage());
        }
    }
    
    private void loadAdminDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminDashboard.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) tfEmail.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Erreur de navigation: " + e.getMessage());
        }
    }
    
    private void loadUserDashboard(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DashboardUser.fxml"));
            Parent root = loader.load();
            DashboardUserController controller = loader.getController();
            controller.setCurrentUser(user);
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