package gui;

import entities.User;
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
import java.sql.SQLException;
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
            int totalUsers = serviceUser.readAll().size();
            lblTotalUsers.setText(String.valueOf(totalUsers));
            
            // Load total drivers
            long totalDrivers = serviceUser.readAll().stream()
                    .filter(u -> "conducteur".equals(u.getRoleCode()))
                    .count();
            lblTotalDrivers.setText(String.valueOf(totalDrivers));
            
            // TODO: Load total trips when trip service is implemented
            lblTotalTrips.setText("0");
            
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", 
                "Failed to load statistics: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleUsersManagement() {
        navigateTo("/GestionUsers.fxml");
    }
    
    @FXML
    private void handleReclamations() {
        navigateTo("/AdminReclamations.fxml");
    }
    
    @FXML
    private void handleTripsManagement() {
        navigateTo("/GestionTrips.fxml");
    }
    
    @FXML
    private void handleStatistics() {
        navigateTo("/Statistics.fxml");
    }
    
    @FXML
    private void handleLogout() {
        navigateTo("/LoginUser.fxml");
    }
    
    private void navigateTo(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            // Use lblTotalUsers to get the scene since we know it exists
            lblTotalUsers.getScene().setRoot(root);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Navigation error: " + e.getMessage());
        }
    }
    
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 