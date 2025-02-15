package gui;

import entities.User;
import Services.ServiceUser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RegisterUserController implements Initializable {
    
    @FXML private TextField tfNom;
    @FXML private TextField tfPrenom;
    @FXML private TextField tfUsername;
    @FXML private TextField tfEmail;
    @FXML private TextField tfTel;
    @FXML private PasswordField tfPassword;
    @FXML private PasswordField tfConfirmPassword;
    @FXML private ComboBox<String> roleComboBox;
    
    private final ServiceUser serviceUser = new ServiceUser();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize role combo box
        roleComboBox.getItems().addAll("passager", "conducteur");
        roleComboBox.setValue("passager");
    }
    
    @FXML
    private void handleRegister() {
        if (!validateInputs()) {
            return;
        }
        
        try {
            // Create user using setters instead of constructor
            User user = new User();
            user.setNom(tfNom.getText());
            user.setPrenom(tfPrenom.getText());
            user.setEmail(tfEmail.getText());
            user.setMdp(tfPassword.getText());
            user.setUsername(tfUsername.getText());
            user.setTel(tfTel.getText());
            user.setRoleCode(roleComboBox.getValue());
            
            serviceUser.create(user);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Account created successfully!");
            navigateToLogin();
            
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Registration failed: " + e.getMessage());
        }
    }
    
    private boolean validateInputs() {
        if (tfNom.getText().isEmpty() || tfPrenom.getText().isEmpty() ||
            tfUsername.getText().isEmpty() || tfEmail.getText().isEmpty() || 
            tfPassword.getText().isEmpty() || tfConfirmPassword.getText().isEmpty() ||
            tfTel.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill all fields");
            return false;
        }
        
        if (!tfPassword.getText().equals(tfConfirmPassword.getText())) {
            showAlert(Alert.AlertType.ERROR, "Error", "Passwords do not match");
            return false;
        }
        
        try {
            // Check if email exists by checking all users
            boolean emailExists = serviceUser.readAll().stream()
                .anyMatch(u -> u.getEmail().equals(tfEmail.getText()));
            
            if (emailExists) {
                showAlert(Alert.AlertType.ERROR, "Error", "Email already registered");
                return false;
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Database error: " + e.getMessage());
            return false;
        }
        
        // Validate phone number format (optional)
        if (!tfTel.getText().matches("\\d{8}")) {
            showAlert(Alert.AlertType.ERROR, "Error", "Le numéro de téléphone doit contenir 8 chiffres");
            return false;
        }
        
        return true;
    }
    
    @FXML
    private void handleBack() {  // Changed from handleLogin to handleBack
        navigateToLogin();
    }
    
    private void navigateToLogin() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/LoginUser.fxml"));
            tfUsername.getScene().setRoot(root);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Navigation error: " + e.getMessage());
        }
    }
    
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 