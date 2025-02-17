package gui.Reclamation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import java.io.IOException;

public class ClientDashboardController {
    
    @FXML
    private StackPane contentArea;
    
    @FXML
    public void initialize() {
        // Load default view (Mes Réclamations)
        loadPage("/User/AfficherReclamations.fxml");
    }
    
    @FXML
    void showReclamations(ActionEvent event) {
        loadPage("/User/AfficherReclamations.fxml");
    }
    
    @FXML
    void showAddReclamation(ActionEvent event) {
        loadPage("/User/AjouterReclamation.fxml");
    }
    
    @FXML
    void showAjouterAvis(ActionEvent event) {
        loadPage("/User/AjouterAvis.fxml");
    }
    
    @FXML
    void showAvis(ActionEvent event) {
        loadPage("/Admin/VoirAvis.fxml");
    }
    
    @FXML
    void handleLogout(ActionEvent event) {
        try {
            Parent loginPage = FXMLLoader.load(getClass().getResource("/LoginUser.fxml"));
            Stage stage = (Stage) contentArea.getScene().getWindow();
            stage.setScene(new Scene(loginPage));
        } catch (IOException e) {
            e.printStackTrace();
            showError("Erreur lors de la déconnexion: " + e.getMessage());
        }
    }
    
    private void loadPage(String fxmlPath) {
        try {
            Parent page = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentArea.getChildren().clear();
            contentArea.getChildren().add(page);
        } catch (IOException e) {
            e.printStackTrace();
            showError("Erreur lors du chargement de la page: " + e.getMessage());
        }
    }
    
    private void showError(String message) {
        // Show error alert
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setContentText(message);
        alert.showAndWait();
    }
} 