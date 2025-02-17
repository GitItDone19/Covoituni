package gui.Reclamation;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import entities.Reclamation;
import Services.Reclamation.ReclamationService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ModifierReclamationController implements Initializable {
    
    @FXML
    private TextArea descriptionArea;
    
    @FXML
    private ComboBox<String> statusComboBox;
    
    private Reclamation reclamation;
    private final ReclamationService reclamationService = new ReclamationService();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialiser le ComboBox avec les statuts possibles
        statusComboBox.getItems().addAll("EN_COURS", "RESOLUE", "ANNULEE");
    }
    
    public void initData(Reclamation reclamation) {
        this.reclamation = reclamation;
        // Remplir les champs avec les données de la réclamation
        descriptionArea.setText(reclamation.getDescription());
        statusComboBox.setValue(reclamation.getStatus());
    }
    
    @FXML
    void sauvegarder(ActionEvent event) {
        try {
            reclamation.setDescription(descriptionArea.getText());
            reclamation.setStatus(statusComboBox.getValue());
            
            reclamationService.update(reclamation);
            showSuccess("Réclamation modifiée avec succès!");
            retourListe(event);
        } catch (SQLException e) {
            showError("Erreur lors de la modification: " + e.getMessage());
        }
    }
    
    @FXML
    void retourListe(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/User/AfficherReclamations.fxml"));
            descriptionArea.getScene().setRoot(root);
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
    
    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setContentText(message);
        alert.showAndWait();
    }
} 