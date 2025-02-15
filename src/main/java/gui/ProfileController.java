package gui;

import entities.Role;
import entities.User;
import Services.ServiceUser;
import Services.ServiceRole;
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

public class ProfileController implements Initializable {
    
    @FXML private TextField tfUsername;
    @FXML private TextField tfNom;
    @FXML private TextField tfPrenom;
    @FXML private TextField tfTel;
    @FXML private TextField tfEmail;
    @FXML private Label lblRole;
    @FXML private PasswordField tfCurrentMdp;
    @FXML private PasswordField tfNewMdp;
    @FXML private PasswordField tfConfirmMdp;
    
    private User currentUser;
    private ServiceUser serviceUser;
    private ServiceRole serviceRole;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        serviceUser = new ServiceUser();
        serviceRole = new ServiceRole();
    }
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
        updateUI();
    }
    
    private void updateUI() {
        if (currentUser == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Utilisateur non trouvé");
            handleBack();
            return;
        }
        
        tfUsername.setText(currentUser.getUsername());
        tfNom.setText(currentUser.getNom());
        tfPrenom.setText(currentUser.getPrenom());
        tfTel.setText(currentUser.getTel());
        tfEmail.setText(currentUser.getEmail());
        
        // Get role display name from role code
        try {
            for (Role role : serviceRole.readAll()) {
                if (role.getCode().equals(currentUser.getRoleCode())) {
                    lblRole.setText(role.getDisplayName());
                    break;
                }
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Erreur lors du chargement du rôle: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleUpdateProfile() {
        if (!validateInputs()) return;
        
        try {
            // Check if email is changed and already exists
            if (!currentUser.getEmail().equals(tfEmail.getText()) &&
                serviceUser.readAll().stream()
                    .anyMatch(u -> u.getEmail().equals(tfEmail.getText()))) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Cet email est déjà utilisé");
                return;
            }
            
            // Check if username is changed and already exists
            if (!currentUser.getUsername().equals(tfUsername.getText()) &&
                serviceUser.readAll().stream()
                    .anyMatch(u -> u.getUsername().equals(tfUsername.getText()))) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Ce nom d'utilisateur existe déjà");
                return;
            }
            
            currentUser.setUsername(tfUsername.getText());
            currentUser.setNom(tfNom.getText());
            currentUser.setPrenom(tfPrenom.getText());
            currentUser.setTel(tfTel.getText());
            currentUser.setEmail(tfEmail.getText());
            
            serviceUser.update(currentUser);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Profil mis à jour avec succès!");
            
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Erreur lors de la mise à jour: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleUpdatePassword() {
        if (!validatePasswordInputs()) return;
        
        try {
            if (!currentUser.getMdp().equals(tfCurrentMdp.getText())) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Mot de passe actuel incorrect");
                return;
            }
            
            currentUser.setMdp(tfNewMdp.getText());
            serviceUser.update(currentUser);
            
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Mot de passe mis à jour avec succès!");
            clearPasswordFields();
            
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Erreur lors de la mise à jour du mot de passe: " + e.getMessage());
        }
    }
    
    private boolean validateInputs() {
        StringBuilder errors = new StringBuilder();
        
        if (tfUsername.getText().isEmpty()) errors.append("Le nom d'utilisateur est requis\n");
        if (tfNom.getText().isEmpty()) errors.append("Le nom est requis\n");
        if (tfPrenom.getText().isEmpty()) errors.append("Le prénom est requis\n");
        if (tfTel.getText().isEmpty()) errors.append("Le téléphone est requis\n");
        if (tfEmail.getText().isEmpty()) errors.append("L'email est requis\n");
        
        // Validate email format
        if (!tfEmail.getText().isEmpty() && !tfEmail.getText().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            errors.append("Format d'email invalide\n");
        }
        
        // Validate username format
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
    
    private boolean validatePasswordInputs() {
        StringBuilder errors = new StringBuilder();
        
        if (tfCurrentMdp.getText().isEmpty()) errors.append("Le mot de passe actuel est requis\n");
        if (tfNewMdp.getText().isEmpty()) errors.append("Le nouveau mot de passe est requis\n");
        if (tfConfirmMdp.getText().isEmpty()) errors.append("La confirmation du mot de passe est requise\n");
        
        if (!tfNewMdp.getText().equals(tfConfirmMdp.getText())) {
            errors.append("Les mots de passe ne correspondent pas\n");
        }
        
        if (errors.length() > 0) {
            showAlert(Alert.AlertType.WARNING, "Validation", errors.toString());
            return false;
        }
        
        return true;
    }
    
    private void clearPasswordFields() {
        tfCurrentMdp.clear();
        tfNewMdp.clear();
        tfConfirmMdp.clear();
    }
    
    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DashboardUser.fxml"));
            Parent root = loader.load();
            DashboardUserController controller = loader.getController();
            controller.setCurrentUser(currentUser);
            Scene scene = new Scene(root);
            Stage stage = (Stage) tfNom.getScene().getWindow();
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