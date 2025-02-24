package gui.Users;

import entities.Role;
import entities.User;
import User.ServiceUser;
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
import utils.PasswordUtil;

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
        // Clear previous styling
        tfEmail.setStyle("");
        tfPassword.setStyle("");
        
        StringBuilder errors = new StringBuilder();
        
        // Email validation
        String email = tfEmail.getText().trim();
        if (email.isEmpty()) {
            errors.append("L'email est requis\n");
            tfEmail.setStyle("-fx-border-color: red;");
        } else if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            errors.append("Format d'email invalide\n");
            tfEmail.setStyle("-fx-border-color: red;");
        }
        
        // Password validation
        String password = tfPassword.getText();
        if (password.isEmpty()) {
            errors.append("Le mot de passe est requis\n");
            tfPassword.setStyle("-fx-border-color: red;");
        }
        
        if (errors.length() > 0) {
            showAlert(Alert.AlertType.WARNING, "Validation", errors.toString());
            return;
        }
        
        // Continue with login process
        try {
            boolean isAdmin = false;
            boolean loginSuccess = false;
            User loggedInUser = null;
            
            try {
                for (User user : serviceUser.readAll()) {
                    if (user.getEmail().equals(email)) {
                        loginSuccess = true;
                        isAdmin = user.getRoleCode().equals(Role.ADMIN_CODE);
                        loggedInUser = user;
                        break;
                    }
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", 
                    "Erreur de base de données: " + e.getMessage());
                return;
            }
            
            if (loginSuccess && loggedInUser != null) {
                // Special case for admin - direct password comparison
                if (isAdmin && password.equals(loggedInUser.getMdp())) {
                    loadGestionUsers();
                }
                // For non-admin users, use password hashing
                else if (!isAdmin && PasswordUtil.checkPassword(password, loggedInUser.getMdp())) {
                    loadDashboard(loggedInUser);
                }
                else {
                    showAlert(Alert.AlertType.ERROR, "Erreur", 
                        "Email ou mot de passe incorrect");
                    tfPassword.clear();
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", 
                    "Email ou mot de passe incorrect");
                tfPassword.clear();
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Erreur lors de la connexion: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Users/RegisterUser.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) tfEmail.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur de navigation: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleForgotPassword() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Users/ForgotPassword.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) tfEmail.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Erreur de navigation: " + e.getMessage());
        }
    }
    
    private void loadGestionUsers() {
        try {
            URL fxmlUrl = getClass().getResource("/Admin/AdminDashboard.fxml");
            if (fxmlUrl == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Fichier AdminDashboard.fxml introuvable");
                return;
            }
            
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) tfEmail.getScene().getWindow();
            stage.setScene(scene);
            stage.setMaximized(true);  // Make sure it stays fullscreen
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Erreur de navigation: " + e.getMessage());
        }
    }
    
    private void loadDashboard(User user) {
        try {
            if (user == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Utilisateur non trouvé");
                return;
            }
            
            URL fxmlUrl = getClass().getResource("/Users/DashboardUser.fxml");
            if (fxmlUrl == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", 
                    "Fichier DashboardUser.fxml introuvable dans /Users/");
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
            stage.setMaximized(true);  // Add this to ensure full screen
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Erreur de navigation: " + e.getMessage());
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