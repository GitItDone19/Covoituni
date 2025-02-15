package gui;

import entities.Reclamation;
import Services.ReclamationService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;

public class AdminReclamationsController {
    
    @FXML
    private ListView<Reclamation> listView;
    @FXML
    private ComboBox<String> filterStatus;
    @FXML
    private Label totalLabel;
    @FXML
    private Label pendingLabel;
    
    private final ReclamationService reclamationService = new ReclamationService();
    
    @FXML
    void initialize() {
        filterStatus.setItems(FXCollections.observableArrayList(
            "Tous", "EN_ATTENTE", "EN_COURS", "RESOLUE"
        ));
        filterStatus.setValue("Tous");
        filterStatus.setOnAction(e -> refreshList());
        
        refreshList();
    }
    
    private void refreshList() {
        try {
            var reclamations = reclamationService.readAll();
            listView.setItems(FXCollections.observableArrayList(reclamations));
            
            // Update stats
            totalLabel.setText("Total: " + reclamations.size());
            long pending = reclamations.stream()
                .filter(r -> r.getStatus().equals("EN_ATTENTE"))
                .count();
            pendingLabel.setText("En attente: " + pending);
            
        } catch (SQLException e) {
            showAlert("Error loading reclamations: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleUpdateStatus(Reclamation reclamation, String newStatus) {
        try {
            reclamation.setStatus(newStatus);
            reclamationService.update(reclamation);
            refreshList();
        } catch (SQLException e) {
            showAlert("Error updating status: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleBack() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AdminDashboard.fxml"));
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