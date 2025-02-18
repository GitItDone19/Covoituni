package controllers;

import entities.Evenement;
import service.EvenementService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class EventController implements Initializable {
    @FXML private TextField titleField;
    @FXML private TextArea descriptionField;
    @FXML private DatePicker eventDatePicker;
    @FXML private TextField locationField;
    @FXML private ComboBox<String> categoryComboBox;
    
    @FXML private TableView<Evenement> eventTable;
    @FXML private TableColumn<Evenement, Integer> idColumn;
    @FXML private TableColumn<Evenement, String> titleColumn;
    @FXML private TableColumn<Evenement, String> descriptionColumn;
    @FXML private TableColumn<Evenement, Timestamp> dateColumn;
    @FXML private TableColumn<Evenement, String> locationColumn;
    @FXML private TableColumn<Evenement, String> categoryColumn;

    private EvenementService evenementService;
    private ObservableList<Evenement> eventList;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        evenementService = new EvenementService();
        eventList = FXCollections.observableArrayList();
        
        // Initialiser les colonnes
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("eventDate"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("categoryEvent"));
        
        // Initialiser la ComboBox des catégories
        categoryComboBox.getItems().addAll("Culture", "Sport", "Education", "Autre");
        
        // Charger les événements
        loadEvents();
        
        // Ajouter un listener pour la sélection dans la table
        eventTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fillForm(newSelection);
            }
        });
    }

    @FXML
    private void handleAddEvent() {
        try {
            Evenement evt = getEventFromForm();
            evenementService.create(evt);
            clearForm();
            loadEvents();
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Événement ajouté avec succès!");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdateEvent() {
        Evenement selected = eventTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez sélectionner un événement!");
            return;
        }

        try {
            Evenement evt = getEventFromForm();
            evt.setId(selected.getId());
            evenementService.update(evt);
            clearForm();
            loadEvents();
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Événement mis à jour avec succès!");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la mise à jour: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteEvent() {
        Evenement selected = eventTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez sélectionner un événement!");
            return;
        }

        try {
            evenementService.delete(selected.getId());
            clearForm();
            loadEvents();
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Événement supprimé avec succès!");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la suppression: " + e.getMessage());
        }
    }

    @FXML
    private void handleClearForm() {
        clearForm();
    }

    private void loadEvents() {
        try {
            eventList.clear();
            eventList.addAll(evenementService.readAll());
            eventTable.setItems(eventList);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement des événements: " + e.getMessage());
        }
    }

    private Evenement getEventFromForm() {
        Evenement evt = new Evenement();
        evt.setTitle(titleField.getText());
        evt.setDescription(descriptionField.getText());
        evt.setEventDate(Timestamp.valueOf(eventDatePicker.getValue().atStartOfDay()));
        evt.setLocation(locationField.getText());
        evt.setCategoryEvent(categoryComboBox.getValue());
        return evt;
    }

    private void fillForm(Evenement evt) {
        titleField.setText(evt.getTitle());
        descriptionField.setText(evt.getDescription());
        eventDatePicker.setValue(evt.getEventDate().toLocalDateTime().toLocalDate());
        locationField.setText(evt.getLocation());
        categoryComboBox.setValue(evt.getCategoryEvent());
    }

    private void clearForm() {
        titleField.clear();
        descriptionField.clear();
        eventDatePicker.setValue(LocalDate.now());
        locationField.clear();
        categoryComboBox.getSelectionModel().clearSelection();
        eventTable.getSelectionModel().clearSelection();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 