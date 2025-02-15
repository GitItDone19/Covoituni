package gui;

import entities.Role;
import entities.User;
import Services.ServiceUser;
import Services.ServiceRole;
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
            ArrayList<Role> roles = serviceRole.afficherAll();
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
        if (!validateInputs()) return;
        
        try {
            // Check if email already exists
            boolean emailExists = serviceUser.afficherAll().stream()
                    .anyMatch(u -> u.getEmail().equals(tfEmail.getText()));
            
            if (emailExists) {
                showAlert(Alert.AlertType.WARNING, "Erreur", "Cet email est déjà utilisé");
                return;
            }
            
            // Find role by display name
            String selectedDisplayName = cbRole.getValue();
            Role selectedRole = null;
            for (Role role : serviceRole.afficherAll()) {
                if (role.getDisplayName().equals(selectedDisplayName)) {
                    selectedRole = role;
                    break;
                }
            }
            
            if (selectedRole == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Rôle invalide");
                return;
            }
            
            User newUser = new User(
                0,
                tfNom.getText(),
                tfPrenom.getText(),
                tfTel.getText(),
                tfEmail.getText(),
                tfMdp.getText(),
                selectedRole,  // Pass the Role object
                generateVerificationCode()
            );
            
            serviceUser.ajouter(newUser);
            showAlert(Alert.AlertType.INFORMATION, "Succès", 
                "Inscription réussie! Vous pouvez maintenant vous connecter.");
            handleBack();
            
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Erreur lors de l'inscription: " + e.getMessage());
        }
    }
    
    private boolean validateInputs() {
        StringBuilder errors = new StringBuilder();
        
        if (tfNom.getText().isEmpty()) errors.append("Le nom est requis\n");
        if (tfPrenom.getText().isEmpty()) errors.append("Le prénom est requis\n");
        if (tfEmail.getText().isEmpty()) errors.append("L'email est requis\n");
        if (tfMdp.getText().isEmpty()) errors.append("Le mot de passe est requis\n");
        if (!tfMdp.getText().equals(tfConfirmMdp.getText())) 
            errors.append("Les mots de passe ne correspondent pas\n");
        if (cbRole.getValue() == null) errors.append("Le rôle est requis\n");
        
        if (errors.length() > 0) {
            showAlert(Alert.AlertType.WARNING, "Validation", errors.toString());
            return false;
        }
        
        return true;
    }
    
    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginUser.fxml"));
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