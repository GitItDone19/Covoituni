package gui.Admin;

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
import entities.User.User;
import User.ServiceUser;
import entities.User.Role;

public class DashboardUserController implements Initializable {
    
    @FXML private Label lblUserName;
    @FXML private Label lblUserEmail;
    @FXML private ListView<String> listViewReservations;
    @FXML private Label lblWelcome;
    @FXML private Label lblTrajetsCount;
    @FXML private Label lblCO2Economy;
    @FXML private TableView tableRecentTrips;
    @FXML private Button btnMesTrajects;
    @FXML private Label lblUserRole;
    @FXML private Label lblRating;
    @FXML private Label lblSavings;
    
    // Add profile editing fields
    @FXML private TextField tfNom;
    @FXML private TextField tfPrenom;
    @FXML private TextField tfTel;
    @FXML private TextField tfEmail;
    @FXML private TextField tfUsername;
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
        try {
            this.currentUser = user;
            if (currentUser != null) {
                lblWelcome.setText("Bienvenue, " + currentUser.getPrenom());
                lblUserName.setText(currentUser.getPrenom() + " " + currentUser.getNom());
                lblUserEmail.setText(currentUser.getEmail());
                lblUserRole.setText(currentUser.getRoleDisplayName());
                updateUIBasedOnRole();
                updateDashboardStats();
                populateProfileFields();
                loadReservations();
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Utilisateur non trouvé");
                handleLogout();
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Erreur lors de l'initialisation: " + e.getMessage());
        }
    }
    
    private void updateUIBasedOnRole() {
        if (currentUser != null) {
            String roleCode = currentUser.getRoleCode();
            if (roleCode == null) return;
            
            switch (roleCode) {
                case Role.ADMIN_CODE:
                    btnMesTrajects.setVisible(false);
                    // Show admin-specific controls
                    break;
                    
                case Role.DRIVER_CODE:
                    btnMesTrajects.setText("Mes Trajets Proposés");
                    lblUserRole.setText("Conducteur");
                    // Show driver-specific stats
                    updateDriverStats();
                    break;
                    
                case Role.PASSENGER_CODE:
                    btnMesTrajects.setText("Mes Réservations");
                    lblUserRole.setText("Passager");
                    // Show passenger-specific stats
                    updatePassengerStats();
                    break;
            }
        }
    }
    
    private void updateDashboardStats() {
        try {
            // TODO: Get actual statistics from database
            lblTrajetsCount.setText("12");
            lblCO2Economy.setText("156 kg");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Erreur lors de la mise à jour des statistiques: " + e.getMessage());
        }
    }
    
    private void updateDriverStats() {
        try {
            if (lblTrajetsCount != null) {
                lblTrajetsCount.setText(String.valueOf(currentUser.getTripsCount()));
            }
            
            // Calculate CO2 savings based on trips
            double co2Saved = currentUser.getTripsCount() * 2.3; // Example calculation
            if (lblCO2Economy != null) {
                lblCO2Economy.setText(String.format("%.1f kg", co2Saved));
            }
            
            // Add driver rating
            if (lblRating != null) {
                lblRating.setText(String.format("%.1f", currentUser.getRating()));
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Erreur lors de la mise à jour des statistiques: " + e.getMessage());
        }
    }
    
    private void updatePassengerStats() {
        try {
            if (lblTrajetsCount != null) {
                lblTrajetsCount.setText(String.valueOf(currentUser.getTripsCount()));
            }
            
            // Calculate money saved compared to solo travel
            double moneySaved = currentUser.getTripsCount() * 5.0; // Example calculation
            if (lblSavings != null) {
                lblSavings.setText(String.format("%.2f €", moneySaved));
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Erreur lors de la mise à jour des statistiques: " + e.getMessage());
        }
    }
    
    private void loadReservations() {
        // TODO: Load actual reservations
        listViewReservations.getItems().addAll(
            "Réservation #1 - En attente",
            "Réservation #2 - Confirmée",
            "Réservation #3 - Terminée"
        );
    }
    
    @FXML
    private void handleSearchAnnonces() {
        // TODO: Implement search annonces navigation
        showAlert(Alert.AlertType.INFORMATION, "Info", "Recherche d'annonces - À implémenter");
    }
    
    @FXML
    private void handleViewReservations() {
        // TODO: Implement reservations view
        showAlert(Alert.AlertType.INFORMATION, "Info", "Voir réservations - À implémenter");
    }
    
    @FXML
    private void handleViewProfile() {
        // TODO: Implement profile view
        showAlert(Alert.AlertType.INFORMATION, "Info", "Voir profil - À implémenter");
    }
    
    @FXML
    private void handleViewEvents() {
        // TODO: Implement special events view
        showAlert(Alert.AlertType.INFORMATION, "Info", "Événements spéciaux - À implémenter");
    }
    
    @FXML
    private void handleGiveReview() {
        // TODO: Implement review submission
        showAlert(Alert.AlertType.INFORMATION, "Info", "Donner un avis - À implémenter");
    }
    
    @FXML
    private void handleMakeReclamation() {
        // TODO: Implement reclamation submission
        showAlert(Alert.AlertType.INFORMATION, "Info", "Faire une réclamation - À implémenter");
    }
    
    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Users/LoginUser.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) lblUserName.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Covoituni - Connexion");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Erreur lors de la déconnexion: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleEditProfile() {
        try {
            if (!validateProfileInputs()) return;
            
            currentUser.setNom(tfNom.getText());
            currentUser.setPrenom(tfPrenom.getText());
            currentUser.setTel(tfTel.getText());
            currentUser.setEmail(tfEmail.getText());
            currentUser.setUsername(tfUsername.getText());
            
            if (!tfNewPassword.getText().isEmpty()) {
                if (!tfCurrentPassword.getText().equals(currentUser.getMdp())) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Mot de passe actuel incorrect");
                    return;
                }
                currentUser.setMdp(tfNewPassword.getText());
            }
            
            serviceUser.update(currentUser);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Profil mis à jour avec succès!");
            
            // Refresh dashboard with updated info
            setCurrentUser(currentUser);
            
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Erreur lors de la mise à jour: " + e.getMessage());
        }
    }
    
    private boolean validateProfileInputs() {
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
    
    private void populateProfileFields() {
        if (currentUser != null) {
            tfNom.setText(currentUser.getNom());
            tfPrenom.setText(currentUser.getPrenom());
            tfTel.setText(currentUser.getTel());
            tfEmail.setText(currentUser.getEmail());
            tfUsername.setText(currentUser.getUsername());
            lblRole.setText(currentUser.getRoleDisplayName());
        }
    }
    
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 