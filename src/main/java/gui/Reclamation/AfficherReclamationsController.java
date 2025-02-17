package gui.Reclamation;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import entities.Reclamation;
import Services.Reclamation.ReclamationService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class AfficherReclamationsController implements Initializable {
    
    @FXML
    private ListView<VBox> listView;
    
    private final ReclamationService reclamationService = new ReclamationService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadReclamations();
    }

    private void loadReclamations() {
        try {
            List<Reclamation> reclamations = reclamationService.readAll();
            listView.getItems().clear();
            
            for (Reclamation reclamation : reclamations) {
                VBox reclamationBox = createReclamationBox(reclamation);
                listView.getItems().add(reclamationBox);
            }
        } catch (SQLException e) {
            showError("Erreur lors du chargement des réclamations: " + e.getMessage());
        }
    }

    private VBox createReclamationBox(Reclamation reclamation) {
        VBox box = new VBox(10);
        box.getStyleClass().add("reclamation-box");
        
        Label descriptionLabel = new Label(reclamation.getDescription());
        descriptionLabel.getStyleClass().add("description-label");
        
        HBox infoBox = new HBox(20);
        // Formatage de la date
        String dateStr = reclamation.getDate() != null ? 
            new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(reclamation.getDate()) : 
            "Date non disponible";
        Label dateLabel = new Label("Date: " + dateStr);
        Label statusLabel = new Label("Status: " + reclamation.getStatus());
        infoBox.getChildren().addAll(dateLabel, statusLabel);
        
        box.getChildren().addAll(descriptionLabel, infoBox);
        
        // Ajouter des styles
        dateLabel.getStyleClass().add("info-label");
        statusLabel.getStyleClass().add("status-label");
        
        // Ajouter un style spécifique selon le statut
        if (reclamation.getStatus() != null) {
            switch (reclamation.getStatus()) {
                case "EN_COURS":
                    statusLabel.getStyleClass().add("status-en-cours");
                    break;
                case "RESOLUE":
                    statusLabel.getStyleClass().add("status-resolue");
                    break;
                case "ANNULEE":
                    statusLabel.getStyleClass().add("status-annulee");
                    break;
            }
        }
        
        return box;
    }

    @FXML
    void retourDashboard(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/UserDashbord.fxml"));
            listView.getScene().setRoot(root);
        } catch (IOException e) {
            showError("Erreur lors du chargement de la page: " + e.getMessage());
        }
    }

    @FXML
    void retourAjout(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/User/AjouterReclamation.fxml"));
            listView.getScene().setRoot(root);
        } catch (IOException e) {
            showError("Erreur lors du chargement de la page: " + e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setContentText(message);
        alert.showAndWait();
    }
} 