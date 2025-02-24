package gui.Users;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import entities.Annonce;
import entities.Reservation;
import Services.ReservationService;
import Services.AnnonceService;

import java.net.URL;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ReservationsPassagerController implements Initializable {
    @FXML
    private ListView<Reservation> reservationsListView;
    
    private ReservationService reservationService;
    private AnnonceService annonceService;
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        reservationService = new ReservationService();
        annonceService = new AnnonceService();
        
        // Configuration de la cellule personnalisée
        reservationsListView.setCellFactory(lv -> new ListCell<Reservation>() {
            private final Button cancelButton = new Button("Annuler");
            private final HBox buttons = new HBox(10, cancelButton);
            private final HBox content = new HBox(15);
            
            {
                cancelButton.getStyleClass().add("button-danger");
                buttons.setStyle("-fx-alignment: center-right;");
                content.setStyle("-fx-alignment: center-left;");
            }

            @Override
            protected void updateItem(Reservation reservation, boolean empty) {
                super.updateItem(reservation, empty);
                
                if (empty || reservation == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    try {
                        VBox container = new VBox(5);
                        HBox contentBox = new HBox(15);
                        
                        // Partie gauche avec les informations
                        VBox infoBox = new VBox(5);
                        
                        // Récupérer l'annonce via le service
                        String annonceInfo = "Information non disponible";
                        Long annonceId = reservation.getAnnonceId();
                        if (annonceId != null) {
                            Annonce annonce = annonceService.readById(annonceId);
                            if (annonce != null) {
                                annonceInfo = "Titre: " + annonce.getTitre();
                            }
                        }
                        
                        Label titleLabel = new Label(annonceInfo);
                        Label statusLabel = new Label("Statut: " + reservation.getStatus());
                        Label dateLabel = new Label("Date: " + reservation.getDateReservation());
                        
                        infoBox.getChildren().addAll(titleLabel, statusLabel, dateLabel);
                        
                        // Partie droite avec le bouton d'annulation
                        Button cancelButton = new Button("Annuler");
                        cancelButton.getStyleClass().add("button-danger");
                        
                        // Désactiver le bouton si la réservation n'est pas en statut PENDING ou CONFIRMED
                        if (!reservation.getStatus().equals("PENDING") && !reservation.getStatus().equals("CONFIRMED")) {
                            cancelButton.setDisable(true);
                        }
                        
                        // Gestionnaire d'événement pour le bouton d'annulation
                        cancelButton.setOnAction(e -> handleCancelReservation(reservation));
                        
                        // Ajouter les éléments au conteneur principal
                        contentBox.getChildren().addAll(infoBox, cancelButton);
                        contentBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
                        HBox.setHgrow(infoBox, Priority.ALWAYS);
                        
                        container.getChildren().add(contentBox);
                        setGraphic(container);
                        
                    } catch (SQLException e) {
                        setGraphic(new Label("Erreur de chargement"));
                        e.printStackTrace();
                    }
                }
            }
        });
        
        loadReservations();
    }

    private void loadReservations() {
        try {
            // TODO: Remplacer par l'ID du passager connecté
            int passengerId = 1;
            reservationsListView.getItems().clear();
            reservationsListView.getItems().addAll(reservationService.readByPassengerId(passengerId));
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                     "Erreur lors du chargement des réservations: " + e.getMessage());
        }
    }

    private void handleCancelReservation(Reservation reservation) {
        try {
            reservationService.cancelReservation(reservation);
            loadReservations(); // Recharger la liste des réservations
            showAlert(Alert.AlertType.INFORMATION, "Succès", 
                     "La réservation a été annulée avec succès");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                     "Erreur lors de l'annulation de la réservation: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 