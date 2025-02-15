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
            ArrayList<Role> roles = serviceRole.afficherAll();
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
                selectedRole,
                generateVerificationCode()
            );
            
            serviceUser.ajouter(newUser);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Utilisateur ajouté avec succès!");
            clearFields();
            
        } catch (Exception e) {
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
            Stage stage = (Stage) tfNom.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur de navigation: " + e.getMessage());
        }
    }
    
    private void clearFields() {
        tfNom.clear();
        tfPrenom.clear();
        tfTel.clear();
        tfEmail.clear();
        tfMdp.clear();
        cbRole.setValue(null);
    }
    
    private boolean validateInputs() {
        StringBuilder errors = new StringBuilder();
        
        if (tfNom.getText().isEmpty()) errors.append("Le nom est requis\n");
        if (tfPrenom.getText().isEmpty()) errors.append("Le prénom est requis\n");
        if (tfEmail.getText().isEmpty()) errors.append("L'email est requis\n");
        if (tfMdp.getText().isEmpty()) errors.append("Le mot de passe est requis\n");
        if (cbRole.getValue() == null) errors.append("Le rôle est requis\n");
        
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