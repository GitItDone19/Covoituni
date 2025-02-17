package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import java.io.IOException;

public class UserDashboardController {
    
    @FXML
    private StackPane contentArea;
    
    @FXML
    public void showReclamations() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/User/AfficherReclamations.fxml"));
            Parent view = loader.load();

            view.setStyle("-fx-background-color: #1E1E1E;");
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void showAddReclamation() {
        loadView("/User/AjouterReclamation.fxml");
    }
    
    @FXML
    public void showAjouterAvis() {
        loadView("/User/AjouterAvis.fxml");
    }
    
    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleLogout() {

    }
} 