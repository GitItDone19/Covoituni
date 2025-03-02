package gui.Admin;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import entities.Event;
import Services.EventService;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import entities.User;
import javafx.scene.control.Alert;
import java.io.IOException;
import gui.Users.DashboardUserController;

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
    private User currentUser;

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
            event.setNom(nomField.getText().trim());
            event.setDateEvent(dateField.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            event.setHeureEvent(heureField.getText().trim() + ":00");
            event.setLieu(lieuField.getText().trim());
            event.setDescription(descriptionArea.getText().trim());
            
            // Récupérer l'ID du type sélectionné
            String selectedType = typeCombo.getValue();
            if (selectedType == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner un type d'événement");
                return;
            }
            
            Map<String, Integer> typeMap = eventService.getEventTypes();
            Integer typeId = typeMap.get(selectedType);
            if (typeId == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Type d'événement invalide");
                return;
            }
            
            event.setIdType(typeId);
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
        StringBuilder errors = new StringBuilder();
        
        if (nomField.getText().trim().isEmpty()) 
            errors.append("Le nom est obligatoire\n");
        if (lieuField.getText().trim().isEmpty()) 
            errors.append("Le lieu est obligatoire\n");
        if (heureField.getText().trim().isEmpty()) 
            errors.append("L'heure est obligatoire\n");
        if (dateField.getValue() == null) 
            errors.append("La date est obligatoire\n");
        if (descriptionArea.getText().trim().isEmpty()) 
            errors.append("La description est obligatoire\n");
        
        if (errors.length() > 0) {
            showAlert(Alert.AlertType.ERROR, "Erreur de validation", errors.toString());
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

    @FXML
    private void handleReturnToDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Users/Dashboard.fxml"));
            Parent root = loader.load();
            
            // Get controller and set current user
            DashboardUserController controller = loader.getController();
            if (currentUser != null) {
                controller.setCurrentUser(currentUser);
            }
            
            // Switch to dashboard scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not return to dashboard: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        // Any initialization that depends on the user
    }
} 