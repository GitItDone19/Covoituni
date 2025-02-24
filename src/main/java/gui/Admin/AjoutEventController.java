package gui.Admin;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.entities.Event;
import Services.EventService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class AjoutEventController {
    @FXML private TextField nomField;
    @FXML private DatePicker dateField;
    @FXML private TextField heureField;
    @FXML private TextField lieuField;
    @FXML private TextArea descriptionArea;
    @FXML private ComboBox<String> typeCombo;
    
    private EventService eventService;

    @FXML
    public void initialize() {
        eventService = new EventService();
        
        try {
            // Charger les types depuis la base de données
            Map<String, Integer> typeMap = eventService.getEventTypes();
            typeCombo.getItems().addAll(typeMap.keySet());
            typeCombo.getSelectionModel().selectFirst();
            
            // Initialiser la date à aujourd'hui
            dateField.setValue(LocalDate.now());
            
            // Ajouter un placeholder pour le format de l'heure
            heureField.setPromptText("HH:mm (ex: 14:30)");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                     "Erreur lors du chargement des types d'événements");
        }
    }

    @FXML
    private void handleAdd() {
        try {
            if (!validateFields()) {
                return;
            }

            // Valider le format de l'heure
            if (!heureField.getText().matches("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")) {
                showAlert(Alert.AlertType.ERROR, "Erreur", 
                         "Format de l'heure invalide. Utilisez le format HH:mm (ex: 14:30)");
                return;
            }

            Event event = new Event();
            event.setNom(nomField.getText());
            event.setDateEvent(dateField.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            event.setHeureEvent(heureField.getText() + ":00");
            event.setLieu(lieuField.getText());
            event.setDescription(descriptionArea.getText());
            
            // Récupérer l'ID du type sélectionné
            String selectedType = typeCombo.getValue();
            Map<String, Integer> typeMap = eventService.getEventTypes();
            event.setIdType(typeMap.get(selectedType));
            
            event.setStatus("ACTIVE");

            eventService.create(event);
            
            showAlert(Alert.AlertType.INFORMATION, "Succès", 
                     "L'événement a été créé avec succès!");
            closeWindow();

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                     "Erreur lors de la création de l'événement: " + e.getMessage());
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