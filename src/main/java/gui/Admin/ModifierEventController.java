package gui.Admin;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.entities.Event;
import Services.EventService;

import java.sql.SQLException;

public class ModifierEventController {
    @FXML private TextField nomField;
    @FXML private DatePicker dateField;
    @FXML private TextField heureField;
    @FXML private TextField lieuField;
    @FXML private TextArea descriptionArea;
    @FXML private ComboBox<String> typeCombo;
    
    private Event event;
    private EventService eventService;

    @FXML
    public void initialize() {
        eventService = new EventService();
        
        // Initialiser le ComboBox des types
        typeCombo.getItems().addAll("Général", "Sport", "Culture", "Autre");
    }

    public void initData(Event event) {
        this.event = event;
        
        // Remplir les champs avec les données de l'événement
        nomField.setText(event.getNom());
        dateField.setValue(java.time.LocalDate.parse(event.getDateEvent()));
        heureField.setText(event.getHeureEvent());
        lieuField.setText(event.getLieu());
        descriptionArea.setText(event.getDescription());
        
        // Sélectionner le type
        typeCombo.getSelectionModel().select(0); // Par défaut "Général"
    }

    @FXML
    private void handleSave() {
        try {
            if (!validateFields()) {
                return;
            }

            // Mettre à jour les données de l'événement
            event.setNom(nomField.getText());
            event.setDateEvent(dateField.getValue().toString());
            event.setHeureEvent(heureField.getText());
            event.setLieu(lieuField.getText());
            event.setDescription(descriptionArea.getText());
            
            // Sauvegarder les modifications
            eventService.update(event);
            
            showAlert(Alert.AlertType.INFORMATION, "Succès", 
                     "L'événement a été modifié avec succès!");
            closeWindow();

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                     "Erreur lors de la modification de l'événement: " + e.getMessage());
        }
    }

    private boolean validateFields() {
        if (nomField.getText().isEmpty() || lieuField.getText().isEmpty() || 
            heureField.getText().isEmpty() || dateField.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                     "Tous les champs sont obligatoires");
            return false;
        }
        return true;
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        ((Stage) nomField.getScene().getWindow()).close();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 