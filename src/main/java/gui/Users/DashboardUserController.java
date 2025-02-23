package gui.Users;

import entities.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import java.io.IOException;
import javafx.scene.control.Button;
import javafx.stage.Modality;

public class DashboardUserController implements Initializable {
    @FXML private Label lblUserName;
    @FXML private Label lblUserEmail;
    @FXML private Label lblActiveReservations;
    @FXML private Label lblCO2Economy;
    @FXML private Label lblRating;
    @FXML private ListView<?> listViewReservations;
    @FXML private Button btnAddReclamation;
    @FXML private Button btnAjouterAvis;
    
    private User currentUser;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize components
        System.out.println("btnAjouterAvis: " + btnAjouterAvis);
    }
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
        updateUserInfo();
    }
    
    private void updateUserInfo() {
        if (currentUser != null) {
            lblUserName.setText(currentUser.getNom() + " " + currentUser.getPrenom());
            lblUserEmail.setText(currentUser.getEmail());
        }
    }
    
    @FXML
    private void handleSearchAnnonces() {
        // TODO: Implement search announcements
    }
    
    @FXML
    private void handleViewReservations() {
        // TODO: Implement view reservations
    }
    
    @FXML
    private void handleViewProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/Users/ViewProfile.fxml"));
            Parent root = loader.load();
            
            ViewProfileController controller = loader.getController();
            controller.setCurrentUser(currentUser);
            
            Scene scene = new Scene(root);
            Stage stage = (Stage) lblUserName.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Erreur lors de l'ouverture du profil: " + e.getMessage());
            alert.showAndWait();
        }
    }
    
    @FXML
    private void handleViewEvents() {
        // TODO: Implement view events
    }
    
    @FXML
    private void handleGiveReview() {
        // TODO: Implement give review
    }
    
    @FXML
    private void handleMakeReclamation() {
        // TODO: Implement make reclamation
    }
    
    @FXML
    private void handleLogout() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Users/LoginUser.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) lblUserName.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Erreur lors de la déconnexion: " + e.getMessage());
            alert.showAndWait();
        }
    }
    
    @FXML
    private void handleAddReclamation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Users/AddReclamation.fxml"));
            Parent root = loader.load();
            
            AddReclamationController controller = loader.getController();
            controller.setCurrentUser(currentUser);
            
            // Load the new scene for the reclamation form
            Scene scene = new Scene(root);
            Stage stage = (Stage) lblUserName.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Ajouter une Réclamation"); // Set the title for the new page
            stage.show();
            
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Erreur lors de l'ouverture du formulaire de réclamation: " + e.getMessage());
            alert.showAndWait();
        }
    }
    
    @FXML
    private void handleAjouterAvis() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Users/AjouterAvis.fxml"));
            Parent root = loader.load();
            
            AjouterAvisController controller = loader.getController();
            controller.setCurrentUser(currentUser);
            
            Stage stage = (Stage) lblUserName.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Failed to load review page: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
} 