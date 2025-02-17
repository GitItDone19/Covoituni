package gui.Admin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import User.ServiceUser;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminDashboardController implements Initializable {
    
    @FXML private Label lblTotalUsers;
    @FXML private Label lblDriverCount;
    @FXML private Label lblPassengerCount;
    @FXML private Label lblTotalTrips;
    @FXML private Label lblTotalReservations;
    @FXML private TableView tableActivities;
    
    private ServiceUser serviceUser;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        serviceUser = new ServiceUser();
        loadStatistics();
    }
    
    private void loadStatistics() {
        try {
            // TODO: Get actual statistics from database
            lblTotalUsers.setText("150");
            lblDriverCount.setText("Conducteurs: 45");
            lblPassengerCount.setText("Passagers: 105");
            lblTotalTrips.setText("67");
            lblTotalReservations.setText("89");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Erreur lors du chargement des statistiques: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleManageDrivers() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/GestionConducteur.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) lblTotalUsers.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Erreur de navigation: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleManagePassengers() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/GestionPassager.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) lblTotalUsers.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Erreur de navigation: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleManageAnnouncements() {
        showAlert(Alert.AlertType.INFORMATION, "Info", "Gestion des annonces - À implémenter");
    }
    
    @FXML
    private void handleManageReservations() {
        showAlert(Alert.AlertType.INFORMATION, "Info", "Gestion des réservations - À implémenter");
    }
    
    @FXML
    private void handleViewReviews() {
        showAlert(Alert.AlertType.INFORMATION, "Info", "Voir les avis - À implémenter");
    }
    
    @FXML
    private void handleManageComplaints() {
        showAlert(Alert.AlertType.INFORMATION, "Info", "Gestion des réclamations - À implémenter");
    }
    
    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Users/LoginUser.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) lblTotalUsers.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", 
                "Failed to logout: " + e.getMessage());
        }
    }
    
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 