package gui;

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
import entities.User;
import Services.ServiceUser;
import entities.Role;

public class DashboardUserController implements Initializable {
    
    @FXML private Label lblWelcome;
    @FXML private Label lblTrajetsCount;
    @FXML private Label lblCO2Economy;
    @FXML private TableView tableRecentTrips;
    @FXML private Button btnMesTrajects;
    @FXML private Label lblUserName;
    @FXML private Label lblUserRole;
    @FXML private Label lblRating;
    @FXML private Label lblSavings;
    
    private User currentUser;
    private ServiceUser serviceUser;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        serviceUser = new ServiceUser();
        updateUI();
    }
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
        updateUI();
    }
    
    private void updateUI() {
        if (currentUser != null) {
            lblWelcome.setText("Bienvenue, " + currentUser.getPrenom());
            lblUserName.setText(currentUser.getPrenom() + " " + currentUser.getNom());
            lblUserRole.setText(currentUser.getRoleCode());
            updateUIBasedOnRole();
            updateDashboardStats();
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Utilisateur non trouvé");
            handleLogout();
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
    
    @FXML
    private void handleMesTrajects() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MesTrajects.fxml"));
            Parent root = loader.load();
            

            
            Scene scene = new Scene(root);
            Stage stage = (Stage) lblWelcome.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Covoituni - Mes Trajets");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Erreur de navigation: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginUser.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) lblWelcome.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Covoituni - Connexion");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Erreur lors de la déconnexion: " + e.getMessage());
        }
    }
    
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 