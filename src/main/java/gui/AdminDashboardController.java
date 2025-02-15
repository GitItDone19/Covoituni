package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import Services.ServiceUser;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminDashboardController implements Initializable {
    
    @FXML private Label lblTotalUsers;
    @FXML private Label lblTotalTrips;
    @FXML private Label lblTotalDrivers;
    @FXML private TableView tableActivities;
    
    private ServiceUser serviceUser;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        serviceUser = new ServiceUser();
        loadStatistics();
    }
    
    private void loadStatistics() {
        try {
            // Load total users
            int totalUsers = serviceUser.afficherAll().size();
            lblTotalUsers.setText(String.valueOf(totalUsers));
            
            // Load total drivers
            long totalDrivers = serviceUser.afficherAll().stream()
                    .filter(u -> "conducteur".equals(u.getRoleCode()))
                    .count();
            lblTotalDrivers.setText(String.valueOf(totalDrivers));
            
            // TODO: Load total trips when trip service is implemented
            lblTotalTrips.setText("0");
            
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", 
                "Failed to load statistics: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleUsersManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionUsers.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) lblTotalUsers.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", 
                "Failed to load users management: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleTripsManagement() {
        // TODO: Implement trips management
        showAlert(Alert.AlertType.INFORMATION, "Info", 
            "Trips management feature coming soon!");
    }
    
    @FXML
    private void handleStatistics() {
        // TODO: Implement detailed statistics view
        showAlert(Alert.AlertType.INFORMATION, "Info", 
            "Detailed statistics feature coming soon!");
    }
    
    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginUser.fxml"));
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