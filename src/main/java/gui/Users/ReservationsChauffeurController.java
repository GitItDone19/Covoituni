package gui.Users;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import main.entities.Annonce;
import main.entities.Reservation;
import Services.ReservationService;
import Services.AnnonceService;

import java.net.URL;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ReservationsChauffeurController implements Initializable {
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
            private final Button acceptButton = new Button("Accepter");
            private final Button refuseButton = new Button("Refuser");
            private final HBox buttons = new HBox(10, acceptButton, refuseButton);
            private final HBox content = new HBox(15);
            
            {
                acceptButton.getStyleClass().add("button-primary");
                refuseButton.getStyleClass().add("button-danger");
                buttons.setStyle("-fx-alignment: center-right;");
                content.setStyle("-fx-alignment: center-left;");
            }

            @Override
            protected void updateItem(Reservation reservation, boolean empty) {
                super.updateItem(reservation, empty);
                
                if (empty || reservation == null) {
                    setGraphic(null);
                } else {
                    try {
                        VBox details = new VBox(5);
                        
                        // Récupérer l'annonce associée
                        Annonce annonce = annonceService.readById(reservation.getAnnonceId());
                        
                        Label titleLabel = new Label(annonce.getTitre());
                        titleLabel.getStyleClass().add("reservation-title");
                        
                        Label dateLabel = new Label("Réservé le : " + 
                            dateFormatter.format(reservation.getDateReservation()));
                        dateLabel.getStyleClass().add("reservation-details");
                        
                        Label statusLabel = new Label("Status: " + reservation.getStatus());
                        statusLabel.getStyleClass().add("status-label");
                        
                        Label commentLabel = new Label("Commentaire: " + reservation.getComment());
                        commentLabel.getStyleClass().add("reservation-details");
                        
                        details.getChildren().addAll(titleLabel, dateLabel, statusLabel, commentLabel);
                        
                        content.getChildren().clear();
                        content.getChildren().addAll(details, buttons);
                        HBox.setHgrow(details, Priority.ALWAYS);
                        
                        // N'afficher les boutons que pour les réservations en attente
                        boolean isPending = "PENDING".equals(reservation.getStatus());
                        buttons.setVisible(isPending);
                        buttons.setManaged(isPending);
                        
                        acceptButton.setOnAction(e -> handleAccept(reservation));
                        refuseButton.setOnAction(e -> handleRefuse(reservation));
                        
                        setGraphic(content);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        
        loadReservations();
    }

    private void loadReservations() {
        try {
            // TODO: Remplacer par l'ID du chauffeur connecté
            int driverId = 1;
            reservationsListView.getItems().clear();
            reservationsListView.getItems().addAll(reservationService.readByDriverId(driverId));
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                     "Erreur lors du chargement des réservations: " + e.getMessage());
        }
    }

    private void handleAccept(Reservation reservation) {
        try {
            reservation.setStatus("CONFIRMED");
            reservationService.update(reservation);
            loadReservations();
            showAlert(Alert.AlertType.INFORMATION, "Succès", 
                     "La réservation a été acceptée");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                     "Erreur lors de l'acceptation de la réservation: " + e.getMessage());
        }
    }

    private void handleRefuse(Reservation reservation) {
        try {
            reservationService.updateReservationAndSeats(reservation, "REFUSED");
            loadReservations();
            showAlert(Alert.AlertType.INFORMATION, "Succès", 
                     "La réservation a été refusée et une place a été libérée");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                     "Erreur lors du refus de la réservation: " + e.getMessage());
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