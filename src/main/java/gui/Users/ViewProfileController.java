package gui.Users;

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

public class ViewProfileController implements Initializable {
    @FXML private TextField tfUsername;
    @FXML private TextField tfEmail;
    @FXML private TextField tfPrenom;
    @FXML private TextField tfNom;
    @FXML private TextField tfTel;
    @FXML private PasswordField tfOldPassword;
    @FXML private PasswordField tfNewPassword;
    @FXML private PasswordField tfConfirmPassword;
    @FXML private Label lblFullName;
    @FXML private Label lblRole;
    @FXML private Label lblTripsCount;
    @FXML private Label lblRating;
    @FXML private Label lblReservationsCount;
    
    private User currentUser;
    private ServiceUser serviceUser;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        serviceUser = new ServiceUser();
    }
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
        updateProfileInfo();
    }
    
    private void updateProfileInfo() {
        if (currentUser != null) {
            tfUsername.setText(currentUser.getUsername());
            tfEmail.setText(currentUser.getEmail());
            tfPrenom.setText(currentUser.getPrenom());
            tfNom.setText(currentUser.getNom());
            tfTel.setText(currentUser.getTel());
            
            lblFullName.setText(currentUser.getPrenom() + " " + currentUser.getNom());
            lblRole.setText(currentUser.getRoleCode());
            
            // TODO: Add statistics from database
            lblTripsCount.setText("0");
            lblRating.setText("0.0");
            lblReservationsCount.setText("0");
        }
    }
    
    @FXML
    private void handleSave() {
        // Validate fields
        StringBuilder errors = new StringBuilder();
        
        if (tfNewPassword.getText().length() > 0) {
            if (!tfOldPassword.getText().equals(currentUser.getMdp())) {
                errors.append("L'ancien mot de passe est incorrect\n");
            }
            if (!tfNewPassword.getText().equals(tfConfirmPassword.getText())) {
                errors.append("Les nouveaux mots de passe ne correspondent pas\n");
            }
            if (tfNewPassword.getText().length() < 6) {
                errors.append("Le nouveau mot de passe doit contenir au moins 6 caractères\n");
            }
        }
        
        if (errors.length() > 0) {
            showAlert(Alert.AlertType.WARNING, "Validation", errors.toString());
            return;
        }
        
        try {
            currentUser.setUsername(tfUsername.getText());
            currentUser.setEmail(tfEmail.getText());
            currentUser.setPrenom(tfPrenom.getText());
            currentUser.setNom(tfNom.getText());
            currentUser.setTel(tfTel.getText());
            
            if (tfNewPassword.getText().length() > 0) {
                currentUser.setMdp(tfNewPassword.getText());
            }
            
            serviceUser.update(currentUser);
            showAlert(Alert.AlertType.INFORMATION, "Succès", 
                "Profil mis à jour avec succès!");
            
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Erreur lors de la mise à jour: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleCancel() {
        updateProfileInfo();  // Reset fields to current values
    }
    
    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/Users/DashboardUser.fxml"));
            Parent root = loader.load();
            
            DashboardUserController controller = loader.getController();
            controller.setCurrentUser(currentUser);
            
            Scene scene = new Scene(root);
            Stage stage = (Stage) tfUsername.getScene().getWindow();
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