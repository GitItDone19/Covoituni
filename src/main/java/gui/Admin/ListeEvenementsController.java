package gui.Admin;

import gui.Users.ListeAnnoncesEventController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.entities.Event;
import main.entities.Annonce;
import Services.EventService;
import Services.AnnonceService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class ListeEvenementsController implements Initializable {
    @FXML
    private ListView<Event> eventListView;
    
    private EventService eventService;
    private AnnonceService annonceService;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        eventService = new EventService();
        annonceService = new AnnonceService();
        
        setupEventListView();
        loadEvents();
    }

    private void setupEventListView() {
        eventListView.setCellFactory(lv -> new ListCell<Event>() {
            @Override
            protected void updateItem(Event event, boolean empty) {
                super.updateItem(event, empty);
                if (empty || event == null) {
                    setGraphic(null);
                } else {
                    VBox container = new VBox(5);
                    setupEventCell(container, event);
                    setGraphic(container);
                }
            }
        });
    }

    private void setupEventCell(VBox container, Event event) {
        // Informations de l'événement
        Label titleLabel = new Label(event.getNom());
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        
        Label dateLabel = new Label(String.format("Date: %s à %s", event.getDateEvent(), event.getHeureEvent()));
        Label lieuLabel = new Label("Lieu: " + event.getLieu());
        Label statusLabel = new Label("Status: " + event.getStatus());
        
        VBox infoContainer = new VBox(5);
        infoContainer.getChildren().addAll(titleLabel, dateLabel, lieuLabel, statusLabel);

        // Conteneur pour les boutons
        HBox buttonsContainer = new HBox(10);
        buttonsContainer.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);

        // Création des boutons
        Button modifierBtn = new Button("Modifier");
        Button supprimerBtn = new Button("Supprimer");
        Button terminerBtn = new Button("Terminer");
        Button voirAnnoncesBtn = new Button("Voir les annonces");

        // Style des boutons
        modifierBtn.getStyleClass().add("button-primary");
        supprimerBtn.getStyleClass().add("button-danger");
        terminerBtn.getStyleClass().add("button-warning");
        voirAnnoncesBtn.getStyleClass().add("button-info");

        // Actions des boutons
        modifierBtn.setOnAction(e -> handleModifierEvent(event));
        supprimerBtn.setOnAction(e -> handleSupprimerEvent(event));
        terminerBtn.setOnAction(e -> handleTerminerEvent(event));
        voirAnnoncesBtn.setOnAction(e -> handleVoirAnnonces(event));

        // Désactiver les boutons si l'événement est terminé
        boolean isTerminated = "TERMINATED".equals(event.getStatus());
        modifierBtn.setDisable(isTerminated);
        supprimerBtn.setDisable(isTerminated);
        terminerBtn.setDisable(isTerminated);

        // Ajouter les boutons au conteneur
        buttonsContainer.getChildren().addAll(modifierBtn, supprimerBtn, terminerBtn, voirAnnoncesBtn);

        // Ajouter tous les éléments au conteneur principal
        container.getChildren().addAll(infoContainer, buttonsContainer);
        container.setStyle("-fx-padding: 10; -fx-border-color: #ddd; -fx-border-radius: 5;");
    }

    private void handleModifierEvent(Event event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ModifierEvent.fxml"));
            Parent root = loader.load();
            
            ModifierEventController controller = loader.getController();
            controller.initData(event);
            
            Stage stage = new Stage();
            stage.setTitle("Modifier l'événement");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setOnHidden(e -> loadEvents());
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                     "Erreur lors de l'ouverture du formulaire de modification");
        }
    }

    private void handleSupprimerEvent(Event event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer l'événement");
        alert.setContentText("Voulez-vous vraiment supprimer cet événement ?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    eventService.delete(event);
                    loadEvents();
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", 
                             "Erreur lors de la suppression de l'événement");
                }
            }
        });
    }

    private void handleTerminerEvent(Event event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Terminer l'événement");
        alert.setContentText("Voulez-vous vraiment terminer cet événement ? Toutes les annonces associées seront également terminées.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    // Terminer l'événement
                    eventService.terminerEvent(event);
                    
                    // Terminer toutes les annonces associées
                    List<Annonce> annonces = annonceService.readByEventId(event.getIdEvent());
                    for (Annonce annonce : annonces) {
                        annonceService.updateStatus(annonce, "TERMINATED");
                    }
                    
                    loadEvents();
                    showAlert(Alert.AlertType.INFORMATION, "Succès", 
                             "L'événement et ses annonces ont été terminés");
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", 
                             "Erreur lors de la terminaison de l'événement");
                }
            }
        });
    }

    private void handleVoirAnnonces(Event event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ListeAnnoncesEvent.fxml"));
            Parent root = loader.load();
            
            ListeAnnoncesEventController controller = loader.getController();
            controller.initData(event);
            
            Stage stage = new Stage();
            stage.setTitle("Annonces de l'événement: " + event.getNom());
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                     "Erreur lors de l'affichage des annonces");
        }
    }

    private void loadEvents() {
        try {
            eventListView.getItems().clear();
            eventListView.getItems().addAll(eventService.readAll());
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                     "Erreur lors du chargement des événements");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleNouvelEvent() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AjoutEvent.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle("Nouvel Événement");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            
            // Rafraîchir la liste après l'ajout
            stage.setOnHidden(e -> loadEvents());
            
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                     "Erreur lors de l'ouverture du formulaire d'ajout");
        }
    }
} 