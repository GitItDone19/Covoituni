package gui.Users;

import javafx.util.StringConverter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import entities.Annonce;
import entities.Trajet;
import entities.Event;
import Services.AnnonceService;
import Services.TrajetService;
import Services.EventService;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.List;

public class AjoutAnnonceController implements Initializable {
    @FXML private TextField titreField;
    @FXML private TextArea descriptionArea;
    @FXML private ComboBox<Trajet> trajetCombo;
    @FXML private Spinner<Integer> seatsSpinner;
    @FXML private ComboBox<String> carCombo; // À remplacer par une entité Car
    @FXML private ComboBox<Event> eventComboBox;

    private AnnonceService annonceService;
    private TrajetService trajetService;
    private EventService eventService;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        annonceService = new AnnonceService();
        trajetService = new TrajetService();
        eventService = new EventService();

        // Configuration du Spinner
        SpinnerValueFactory<Integer> valueFactory = 
            new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 8, 1);
        seatsSpinner.setValueFactory(valueFactory);

        // Chargement des trajets
        loadTrajets();
        
        // TODO: Charger les voitures une fois l'entité Car créée
        carCombo.getItems().addAll("Voiture 1", "Voiture 2"); // Temporaire

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

        // Initialiser la ComboBox des événements
        loadEvents();
    }

    private void loadTrajets() {
        try {
            trajetCombo.getItems().clear();
            trajetCombo.getItems().addAll(trajetService.readAll());
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
            List<Event> events = eventService.readAllActive();
            eventComboBox.getItems().addAll(events);
            
            // Sélectionner "Aucun événement" par défaut
            eventComboBox.getSelectionModel().selectFirst();
            
            // Personnaliser l'affichage des événements
            eventComboBox.setConverter(new StringConverter<Event>() {
                @Override
                public String toString(Event event) {
                    if (event == null) return null;
                    if (event.getIdEvent() == 0) return "Aucun événement";
                    return String.format("%s (%s à %s)", 
                        event.getNom(),
                        event.getDateEvent(),
                        event.getHeureEvent()
                    );
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

    @FXML
    private void handlePublish() {
        try {
            if (!validateFields()) {
                return;
            }

            Annonce annonce = new Annonce(
                titreField.getText(),
                descriptionArea.getText(),
                1, // TODO: Remplacer par l'ID du conducteur connecté
                1, // TODO: Remplacer par l'ID de la voiture sélectionnée
                seatsSpinner.getValue()
            );

            if (trajetCombo.getValue() != null) {
                annonce.setTrajetId(trajetCombo.getValue().getId());
            }

            // Définir l'event_id
            Event selectedEvent = eventComboBox.getValue();
            if (selectedEvent != null) {
                System.out.println("Event sélectionné: " + selectedEvent.getNom() + " (ID: " + selectedEvent.getIdEvent() + ")");
                annonce.setEventId(selectedEvent.getIdEvent());
            } else {
                annonce.setEventId(0);
            }

            annonceService.create(annonce);
            
            showAlert(Alert.AlertType.INFORMATION, "Succès", 
                     "L'annonce a été publiée avec succès!");
            closeWindow();

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                     "Erreur lors de la publication de l'annonce: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private boolean validateFields() {
        if (titreField.getText().isEmpty() || descriptionArea.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                     "Le titre et la description sont obligatoires");
            return false;
        }
        return true;
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
} 