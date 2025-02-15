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
import java.util.ResourceBundle;

public class ProfileController implements Initializable {
    
    @FXML private TextField tfNom;
    @FXML private TextField tfPrenom;
    @FXML private TextField tfTel;
    @FXML private TextField tfEmail;
    @FXML private PasswordField tfCurrentPassword;
    @FXML private PasswordField tfNewPassword;
    @FXML private PasswordField tfConfirmPassword;
    @FXML private Label lblRole;
    
    private User currentUser;
    private ServiceUser serviceUser;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        serviceUser = new ServiceUser();
    }
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
        populateFields();
    }
    
    private void populateFields() {
        tfNom.setText(currentUser.getNom());
        tfPrenom.setText(currentUser.getPrenom());
        tfTel.setText(currentUser.getTel());
        tfEmail.setText(currentUser.getEmail());
        lblRole.setText(currentUser.getRoleDisplayName());
    }
    
    @FXML
    private void handleSave() {
        try {
            if (!validateInputs()) return;
            
            currentUser.setNom(tfNom.getText());
            currentUser.setPrenom(tfPrenom.getText());
            currentUser.setTel(tfTel.getText());
            currentUser.setEmail(tfEmail.getText());
            
            if (!tfNewPassword.getText().isEmpty()) {
                if (!tfCurrentPassword.getText().equals(currentUser.getMdp())) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Mot de passe actuel incorrect");
                    return;
                }
                currentUser.setMdp(tfNewPassword.getText());
            }
            
            serviceUser.update(currentUser);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Profil mis à jour avec succès!");
            handleBack();
            
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Erreur lors de la mise à jour: " + e.getMessage());
        }
    }
    
    private boolean validateInputs() {
        StringBuilder errors = new StringBuilder();
        
        if (tfNom.getText().isEmpty()) errors.append("Le nom est requis\n");
        if (tfPrenom.getText().isEmpty()) errors.append("Le prénom est requis\n");
        if (tfEmail.getText().isEmpty()) errors.append("L'email est requis\n");
        
        if (!tfNewPassword.getText().isEmpty()) {
            if (tfCurrentPassword.getText().isEmpty()) 
                errors.append("Le mot de passe actuel est requis\n");
            if (!tfNewPassword.getText().equals(tfConfirmPassword.getText()))
                errors.append("Les nouveaux mots de passe ne correspondent pas\n");
        }
        
        if (errors.length() > 0) {
            showAlert(Alert.AlertType.WARNING, "Validation", errors.toString());
            return false;
        }
        
        return true;
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
            stage.setTitle("Covoituni - Tableau de bord");
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