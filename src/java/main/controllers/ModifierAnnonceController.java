package main.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.entities.Annonce;
import main.entities.Event;
import main.entities.Trajet;
import main.services.AnnonceService;
import main.services.EventService;
import main.services.TrajetService;
import javafx.util.StringConverter;

import java.sql.SQLException;

public class ModifierAnnonceController {
    @FXML private TextField titreField;
    @FXML private TextArea descriptionArea;
    @FXML private ComboBox<Trajet> trajetCombo;
    @FXML private Spinner<Integer> seatsSpinner;
    @FXML private ComboBox<Event> eventComboBox;
    
    private AnnonceService annonceService;
    private Annonce annonce;
    private TrajetService trajetService;
    private EventService eventService;

    @FXML
    public void initialize() {
        annonceService = new AnnonceService();
        trajetService = new TrajetService();
        eventService = new EventService();
        
        // Configuration du Spinner
        SpinnerValueFactory<Integer> valueFactory = 
            new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 8, 1);
        seatsSpinner.setValueFactory(valueFactory);
        
        // Charger les trajets et événements
        loadTrajets();
        loadEvents();
    }

    public void initData(Annonce annonce) {
        this.annonce = annonce;
        
        // Remplir les champs avec les données de l'annonce
        titreField.setText(annonce.getTitre());
        descriptionArea.setText(annonce.getDescription());
        seatsSpinner.getValueFactory().setValue(annonce.getAvailableSeats());
        
        // Sélectionner le trajet
        if (annonce.getTrajetId() != null) {
            trajetCombo.getItems().stream()
                .filter(t -> t.getId() == annonce.getTrajetId())
                .findFirst()
                .ifPresent(trajetCombo.getSelectionModel()::select);
        }
        
        // Sélectionner l'événement
        if (annonce.getEventId() > 0) {
            eventComboBox.getItems().stream()
                .filter(e -> e.getIdEvent() == annonce.getEventId())
                .findFirst()
                .ifPresent(eventComboBox.getSelectionModel()::select);
        }
    }

    @FXML
    private void handleSave() {
        try {
            if (!validateFields()) {
                return;
            }

            // Mettre à jour les données de l'annonce
            annonce.setTitre(titreField.getText());
            annonce.setDescription(descriptionArea.getText());
            annonce.setAvailableSeats(seatsSpinner.getValue());
            
            if (trajetCombo.getValue() != null) {
                annonce.setTrajetId(trajetCombo.getValue().getId());
            }
            
            Event selectedEvent = eventComboBox.getValue();
            annonce.setEventId(selectedEvent != null ? selectedEvent.getIdEvent() : 0);

            // Sauvegarder les modifications
            annonceService.update(annonce);
            
            showAlert(Alert.AlertType.INFORMATION, "Succès", 
                     "L'annonce a été modifiée avec succès!");
            closeWindow();

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                     "Erreur lors de la modification de l'annonce: " + e.getMessage());
        }
    }

    private boolean validateFields() {
        if (titreField.getText().isEmpty() || descriptionArea.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                     "Le titre et la description sont obligatoires");
            return false;
        }
        return true;
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        ((Stage) titreField.getScene().getWindow()).close();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void loadTrajets() {
        try {
            trajetCombo.getItems().clear();
            trajetCombo.getItems().addAll(trajetService.readAll());
            
            // Configuration de l'affichage des trajets
            trajetCombo.setCellFactory(lv -> new ListCell<Trajet>() {
                @Override
                protected void updateItem(Trajet trajet, boolean empty) {
                    super.updateItem(trajet, empty);
                    if (empty || trajet == null) {
                        setText(null);
                    } else {
                        setText(trajet.getTitre());
                    }
                }
            });
            
            trajetCombo.setButtonCell(new ListCell<Trajet>() {
                @Override
                protected void updateItem(Trajet trajet, boolean empty) {
                    super.updateItem(trajet, empty);
                    if (empty || trajet == null) {
                        setText(null);
                    } else {
                        setText(trajet.getTitre());
                    }
                }
            });
            
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                     "Erreur lors du chargement des trajets: " + e.getMessage());
        }
    }

    private void loadEvents() {
        try {
            eventComboBox.getItems().clear();
            
            // Ajouter l'option "Aucun événement"
            Event aucunEvent = new Event();
            aucunEvent.setIdEvent(0);
            aucunEvent.setNom("Aucun événement");
            eventComboBox.getItems().add(aucunEvent);
            
            // Ajouter les événements actifs
            eventComboBox.getItems().addAll(eventService.readAllActive());
            
            // Personnaliser l'affichage des événements
            eventComboBox.setConverter(new StringConverter<Event>() {
                @Override
                public String toString(Event event) {
                    if (event == null) return null;
                    if (event.getIdEvent() == 0) return "Aucun événement";
                    return event.getNom() + " (" + event.getDateEvent() + " " + event.getHeureEvent() + ")";
                }

                @Override
                public Event fromString(String string) {
                    return null;
                }
            });
            
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                     "Erreur lors du chargement des événements: " + e.getMessage());
        }
    }
} 