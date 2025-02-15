package gui;

import entities.Role;
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
    
    private ServiceUser serviceUser;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        serviceUser = new ServiceUser();
    }
    
    @FXML
    private void handleLogin() {
        String email = tfEmail.getText();
        String password = tfPassword.getText();
        
        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation", "Veuillez remplir tous les champs");
            return;
        }
        
        try {
            boolean isAdmin = false;
            boolean loginSuccess = false;
            User loggedInUser = null;
            
            try {
                for (User user : serviceUser.afficherAll()) {
                    if (user.getEmail().equals(email) && user.getMdp().equals(password)) {
                        loginSuccess = true;
                        isAdmin = user.getRoleCode().equals(Role.ADMIN_CODE);
                        loggedInUser = user;
                        break;
                    }
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur de base de données: " + e.getMessage());
                return;
            }
            
            if (loginSuccess && loggedInUser != null) {
                if (isAdmin) {
                    loadGestionUsers();
                } else {
                    loadDashboard(loggedInUser);
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Email ou mot de passe incorrect");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur de connexion: " + e.getMessage());
            e.printStackTrace();
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
    
    private void loadGestionUsers() {
        try {
            URL fxmlUrl = getClass().getResource("/AdminDashboard.fxml");
            if (fxmlUrl == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Fichier AdminDashboard.fxml introuvable");
                return;
            }
            
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) tfEmail.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Erreur de navigation: " + e.getMessage() + "\nCause: " + e.getCause());
            e.printStackTrace();
        }
    }
    
    private void loadDashboard(User user) {
        try {
            if (user == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Utilisateur non trouvé");
                return;
            }
            
            URL fxmlUrl = getClass().getResource("/DashboardUser.fxml");
            if (fxmlUrl == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Fichier FXML introuvable");
                return;
            }
            
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();
            
            DashboardUserController controller = loader.getController();
            controller.setCurrentUser(user);
            
            Scene scene = new Scene(root);
            Stage stage = (Stage) tfEmail.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Covoituni - Tableau de bord");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Erreur de navigation: " + e.getMessage() + "\nCause: " + e.getCause());
            e.printStackTrace();
        }
    }
    
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 