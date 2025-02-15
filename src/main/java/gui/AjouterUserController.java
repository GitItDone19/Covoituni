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

public class AjouterUserController implements Initializable {
    
    @FXML private TextField tfUsername;
    @FXML private TextField tfNom;
    @FXML private TextField tfPrenom;
    @FXML private TextField tfTel;
    @FXML private TextField tfEmail;
    @FXML private PasswordField tfMdp;
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
                roleDisplayNames.add(role.getDisplayName());
            }
            cbRole.setItems(FXCollections.observableArrayList(roleDisplayNames));
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Erreur lors du chargement des rôles: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleAjouter() {
        if (!validateInputs()) return;
        
        try {
            // Check if username already exists
            if (serviceUser.readAll().stream()
                    .anyMatch(u -> u.getUsername().equals(tfUsername.getText()))) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Ce nom d'utilisateur existe déjà");
                return;
            }
            
            // Check if email already exists
            if (serviceUser.readAll().stream()
                    .anyMatch(u -> u.getEmail().equals(tfEmail.getText()))) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Cet email existe déjà");
                return;
            }
            
            // Find role by display name
            String selectedDisplayName = cbRole.getValue();
            Role selectedRole = null;
            for (Role role : serviceRole.readAll()) {
                if (role.getDisplayName().equals(selectedDisplayName)) {
                    selectedRole = role;
                    break;
                }
            }
            
            if (selectedRole == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Rôle invalide");
                return;
            }
            
            // Create new user
            User newUser = new User(
                tfUsername.getText(),    // username
                tfNom.getText(),         // nom
                tfPrenom.getText(),      // prenom
                tfTel.getText(),         // tel
                tfEmail.getText(),       // email
                tfMdp.getText(),         // mdp
                selectedRole.getCode(),  // roleCode
                generateVerificationCode() // verificationcode
            );
            
            serviceUser.create(newUser);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Utilisateur ajouté avec succès!");
            clearFields();
            
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Erreur lors de l'ajout: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleEffacer() {
        clearFields();
    }
    
    @FXML
    private void handleVoirListe() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ViewUsers.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) tfUsername.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Erreur de navigation: " + e.getMessage());
        }
    }
    
    private void clearFields() {
        tfUsername.clear();
        tfNom.clear();
        tfPrenom.clear();
        tfTel.clear();
        tfEmail.clear();
        tfMdp.clear();
        cbRole.setValue(null);
    }
    
    private boolean validateInputs() {
        StringBuilder errors = new StringBuilder();
        
        if (tfUsername.getText().isEmpty()) errors.append("Le nom d'utilisateur est requis\n");
        if (tfNom.getText().isEmpty()) errors.append("Le nom est requis\n");
        if (tfPrenom.getText().isEmpty()) errors.append("Le prénom est requis\n");
        if (tfTel.getText().isEmpty()) errors.append("Le téléphone est requis\n");
        if (tfEmail.getText().isEmpty()) errors.append("L'email est requis\n");
        if (tfMdp.getText().isEmpty()) errors.append("Le mot de passe est requis\n");
        if (cbRole.getValue() == null) errors.append("Le rôle est requis\n");
        
        // Validate email format
        if (!tfEmail.getText().isEmpty() && !tfEmail.getText().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            errors.append("Format d'email invalide\n");
        }
        
        // Validate username format (alphanumeric and underscore only)
        if (!tfUsername.getText().isEmpty() && !tfUsername.getText().matches("^[a-zA-Z0-9_]+$")) {
            errors.append("Le nom d'utilisateur ne peut contenir que des lettres, chiffres et underscore\n");
        }
        
        // Validate phone number format
        if (!tfTel.getText().isEmpty() && !tfTel.getText().matches("^[0-9]{8}$")) {
            errors.append("Le numéro de téléphone doit contenir 8 chiffres\n");
        }
        
        if (errors.length() > 0) {
            showAlert(Alert.AlertType.WARNING, "Validation", errors.toString());
            return false;
        }
        
        return true;
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