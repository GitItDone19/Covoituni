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

public class MesTrajetsController implements Initializable {
    
    @FXML private TableView tableTrajets;
    @FXML private Label lblTitle;
    
    private User currentUser;
    private ServiceUser serviceUser;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        serviceUser = new ServiceUser();
        initializeTable();
    }
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
        lblTitle.setText("Trajets de " + currentUser.getPrenom());
        loadUserTrajets();
    }
    
    private void initializeTable() {
        // TODO: Initialize table columns
    }
    
    private void loadUserTrajets() {
        // TODO: Load user's trajets from database
    }
    
    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DashboardUser.fxml"));
            Parent root = loader.load();
            
            DashboardUserController controller = loader.getController();
            controller.setCurrentUser(currentUser);
            
            Scene scene = new Scene(root);
            Stage stage = (Stage) tableTrajets.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Covoituni - Tableau de bord");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Erreur de navigation: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleAddTrajet() {
        // TODO: Implement add trajet functionality
    }
    
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 