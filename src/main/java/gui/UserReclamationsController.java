package gui;

import entities.Reclamation;
import entities.User;
import Services.ReclamationService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class UserReclamationsController {
    
    @FXML
    private ListView<Reclamation> listView;
    @FXML
    private Button btnNewReclamation;
    
    private User currentUser;
    private final ReclamationService reclamationService = new ReclamationService();
    
    @FXML
    void initialize() {
        if (currentUser != null) {
            refreshList();
        }
    }
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
        refreshList();
    }
    
    private void refreshList() {
        try {
            // Get all reclamations and filter by current user
            List<Reclamation> userReclamations = reclamationService.readAll().stream()
                .filter(r -> r.getUserId() == currentUser.getId())
                .toList();
            
            ObservableList<Reclamation> observableReclamations = 
                FXCollections.observableArrayList(userReclamations);
            listView.setItems(observableReclamations);
            
        } catch (SQLException e) {
            showAlert("Error loading reclamations: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleNewReclamation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterReclamation.fxml"));
            Parent root = loader.load();
            
            AjouterReclamationController controller = loader.getController();
            controller.setUserId(currentUser.getId()); // Pass only the user ID
            
            listView.getScene().setRoot(root);
        } catch (IOException e) {
            showAlert("Navigation error: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DashboardUser.fxml"));
            Parent root = loader.load();
            
            DashboardUserController controller = loader.getController();
            controller.setCurrentUser(currentUser);
            
            listView.getScene().setRoot(root);
        } catch (IOException e) {
            showAlert("Navigation error: " + e.getMessage());
        }
    }
    
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
} 