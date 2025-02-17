package gui.Users;

import entities.User.Role;
import entities.User.User;
import User.ServiceUser;
import User.ServiceRole;
import javafx.collections.FXCollections;
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
import java.util.ArrayList;
import java.util.ResourceBundle;

public class RegisterUserController implements Initializable {
    
    @FXML private TextField tfUsername;
    @FXML private TextField tfNom;
    @FXML private TextField tfPrenom;
    @FXML private TextField tfTel;
    @FXML private TextField tfEmail;
    @FXML private PasswordField tfMdp;
    @FXML private PasswordField tfConfirmMdp;
    @FXML private ComboBox<String> cbRole;
    
    private ServiceUser serviceUser;
    private ServiceRole serviceRole;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        serviceUser = new ServiceUser();
        serviceRole = new ServiceRole();
        
        try {
            // Get roles from database
            ArrayList<Role> roles = serviceRole.readAll();
            ArrayList<String> roleDisplayNames = new ArrayList<>();
            for (Role role : roles) {
                // Only show driver and passenger roles for registration
                if (!role.getCode().equals(Role.ADMIN_CODE)) {
                    roleDisplayNames.add(role.getDisplayName());
                }
            }
            cbRole.setItems(FXCollections.observableArrayList(roleDisplayNames));
            
            // Set passenger as default role
            for (String displayName : roleDisplayNames) {
                if (displayName.equals("Passager")) {
                    cbRole.setValue(displayName);
                    break;
                }
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Erreur lors du chargement des rôles: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleRegister() {
        // Clear previous styling
        tfNom.setStyle("");
        tfPrenom.setStyle("");
        tfTel.setStyle("");
        tfEmail.setStyle("");
        tfMdp.setStyle("");
        tfConfirmMdp.setStyle("");
        cbRole.setStyle("");
        
        StringBuilder errors = new StringBuilder();
        
        // Nom validation
        String nom = tfNom.getText().trim();
        if (nom.isEmpty()) {
            errors.append("Le nom est requis\n");
            tfNom.setStyle("-fx-border-color: red;");
        } else if (!nom.matches("^[a-zA-ZÀ-ÿ\\s]{2,}$")) {
            errors.append("Le nom doit contenir au moins 2 lettres\n");
            tfNom.setStyle("-fx-border-color: red;");
        }
        
        // Prénom validation
        String prenom = tfPrenom.getText().trim();
        if (prenom.isEmpty()) {
            errors.append("Le prénom est requis\n");
            tfPrenom.setStyle("-fx-border-color: red;");
        } else if (!prenom.matches("^[a-zA-ZÀ-ÿ\\s]{2,}$")) {
            errors.append("Le prénom doit contenir au moins 2 lettres\n");
            tfPrenom.setStyle("-fx-border-color: red;");
        }
        
        // Téléphone validation
        String tel = tfTel.getText().trim();
        if (tel.isEmpty()) {
            errors.append("Le numéro de téléphone est requis\n");
            tfTel.setStyle("-fx-border-color: red;");
        } else if (!tel.matches("^[0-9]{8}$")) {
            errors.append("Le numéro de téléphone doit contenir 8 chiffres\n");
            tfTel.setStyle("-fx-border-color: red;");
        }
        
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
        String password = tfMdp.getText();
        if (password.isEmpty()) {
            errors.append("Le mot de passe est requis\n");
            tfMdp.setStyle("-fx-border-color: red;");
        } else if (password.length() < 6) {
            errors.append("Le mot de passe doit contenir au moins 6 caractères\n");
            tfMdp.setStyle("-fx-border-color: red;");
        }
        
        // Confirm password validation
        String confirmPassword = tfConfirmMdp.getText();
        if (!confirmPassword.equals(password)) {
            errors.append("Les mots de passe ne correspondent pas\n");
            tfConfirmMdp.setStyle("-fx-border-color: red;");
        }
        
        // Role validation
        if (cbRole.getValue() == null) {
            errors.append("Veuillez sélectionner un type de compte\n");
            cbRole.setStyle("-fx-border-color: red;");
        }
        
        // Check if email already exists
        try {
            if (serviceUser.emailExists(email)) {
                errors.append("Cet email est déjà utilisé\n");
                tfEmail.setStyle("-fx-border-color: red;");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Erreur lors de la vérification de l'email: " + e.getMessage());
            return;
        }
        
        if (errors.length() > 0) {
            showAlert(Alert.AlertType.WARNING, "Validation", errors.toString());
            return;
        }
        
        // Continue with registration process
        try {
            // Get role from database based on selection
            String roleCode = cbRole.getValue().equals("Conducteur") ? Role.DRIVER_CODE : Role.PASSENGER_CODE;
            Role selectedRole = null;
            
            // Find the role in database
            for (Role role : serviceRole.readAll()) {
                if (role.getCode().equals(roleCode)) {
                    selectedRole = role;
                    break;
                }
            }
            
            if (selectedRole == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Type de compte invalide");
                return;
            }
            
            User newUser = new User(
                0,  // ID will be generated by database
                nom,
                prenom,
                tel,
                email,
                password,
                selectedRole,
                generateVerificationCode()  // If you need verification code
            );
            
            serviceUser.create(newUser);
            showAlert(Alert.AlertType.INFORMATION, "Succès", 
                "Compte créé avec succès!");
            handleBack();
            
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Erreur lors de la création du compte: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Users/LoginUser.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) tfNom.getScene().getWindow();
            stage.setScene(scene);
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
    
    private String generateVerificationCode() {
        return String.format("%06d", (int)(Math.random() * 1000000));
    }
} 