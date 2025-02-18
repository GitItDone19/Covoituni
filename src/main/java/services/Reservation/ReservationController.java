package services.Reservation;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import entities.Annonce.Annonce;
import entities.Trajet.Trajet;
import services.Annonce.AnnonceService;
import services.Trajet.TrajetService;
import java.sql.SQLException;
import java.util.List;
import entities.Reservation.Reservation;
import utils.MyConnection;

/**
 * Contrôleur gérant l'interface utilisateur des réservations
 * Cette classe gère l'affichage et les interactions avec la liste des réservations
 */
public class ReservationController {
    @FXML
    private ListView<Reservation> reservationListView;  // Liste des réservations

    // Services nécessaires - initialisés directement
    private final ReservationService reservationService = new ReservationService();
    private final AnnonceService annonceService = new AnnonceService();
    private final TrajetService trajetService = new TrajetService();

    /**
     * Initialise le contrôleur et configure la liste des réservations
     */
    @FXML
    public void initialize() {
        System.out.println("Initialisation du ReservationController");
        try {
            loadReservations();
            configureListView();
            System.out.println("Initialisation réussie");
        } catch (Exception e) {
            System.err.println("Erreur lors de l'initialisation:");
            e.printStackTrace();
        }
    }

    /**
     * Configure l'affichage de chaque élément dans la liste
     */
    @FXML
    private void configureListView() {
        reservationListView.setCellFactory(lv -> new ListCell<Reservation>() {
            @Override
            protected void updateItem(Reservation reservation, boolean empty) {
                super.updateItem(reservation, empty);
                if (empty || reservation == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    try {
                        // Conteneur principal
                        VBox container = new VBox(10);
                        container.setPadding(new Insets(15));
                        container.setStyle("-fx-background-color: white; -fx-border-color: #E9ECEF; " +
                                        "-fx-border-width: 0 0 1 0; -fx-background-radius: 8;");

                        // Récupérer l'annonce et le trajet associés
                        Annonce annonce = annonceService.readById(Long.valueOf(reservation.getAnnonceId()));
                        Trajet trajet = trajetService.readById(annonce.getTrajetId());

                        // En-tête de la réservation
                        Label reservationId = new Label("Réservation #" + reservation.getId());
                        reservationId.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

                        // Informations de l'annonce et du trajet
                        Label annonceInfo = new Label("Annonce: " + annonce.getTitre());
                        Label trajetInfo = new Label(String.format("Trajet: %s → %s",
                                trajet.getDeparturePoint(),
                                trajet.getArrivalPoint()));
                        Label dateInfo = new Label("Date: " + reservation.getDateReservation());

                        // Statut avec couleur
                        Label statusLabel = new Label("Status: " + reservation.getStatus());
                        String statusColor;
                        switch (reservation.getStatus()) {
                            case "PENDING":
                                statusColor = "#34D186"; // Vert
                                break;
                            case "CANCELLED":
                                statusColor = "#FF4757"; // Rouge
                                break;
                            default:
                                statusColor = "#7A7D8C"; // Gris
                        }
                        statusLabel.setStyle("-fx-text-fill: " + statusColor + "; -fx-font-weight: bold;");

                        // Boutons d'action - montrer le bouton d'annulation uniquement pour les réservations en attente
                        HBox buttonsBox = new HBox(10);
                        buttonsBox.setAlignment(Pos.CENTER_RIGHT);

                        if (reservation.getStatus().equals("PENDING")) {
                            Button cancelButton = new Button("Annuler");
                            cancelButton.setStyle("-fx-background-color: #FF4757; -fx-text-fill: white;");
                            cancelButton.setOnAction(e -> annulerReservation(reservation));
                            buttonsBox.getChildren().add(cancelButton);
                        }

                        // Ajouter tous les éléments au conteneur
                        container.getChildren().addAll(
                            reservationId,
                            annonceInfo,
                            trajetInfo,
                            dateInfo,
                            statusLabel,
                            buttonsBox
                        );

                        setGraphic(container);

                    } catch (SQLException e) {
                        Label errorLabel = new Label("Erreur de chargement: " + e.getMessage());
                        errorLabel.setStyle("-fx-text-fill: red;");
                        setGraphic(errorLabel);
                    }
                }
            }
        });
    }

    /**
     * Charge les réservations de l'utilisateur
     */
    private void loadReservations() {
        try {
            System.out.println("Chargement des réservations..."); // Log
            List<Reservation> reservations = reservationService.getReservationsByUserId(Long.valueOf(1L));
            System.out.println("Nombre de réservations trouvées : " + reservations.size()); // Log
            reservationListView.setItems(FXCollections.observableArrayList(reservations));
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des réservations:");
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger les réservations: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Annule une réservation spécifique
     * @param reservation La réservation à annuler
     */
    @FXML
    private void annulerReservation(Reservation reservation) {
        try {
            // 1. Récupérer l'annonce associée à la réservation
            Annonce annonce = annonceService.readById(Long.valueOf(reservation.getAnnonceId()));
            
            // 2. Récupérer le trajet associé à l'annonce
            Trajet trajet = trajetService.readById(annonce.getTrajetId());
            
            // 3. Incrémenter le nombre de places disponibles
            trajet.setAvailableSeats(trajet.getAvailableSeats() + 1);
            trajetService.update(trajet);
            
            // 4. Si l'annonce était CLOSED et qu'il y a maintenant une place, la réouvrir
            if (annonce.getStatus().equals("CLOSED") && trajet.getAvailableSeats() > 0) {
                annonce.setStatus("OPEN");
                annonceService.update(annonce);
            }
            
            // 5. Changer le statut de la réservation à CANCELLED au lieu de la supprimer
            reservationService.updateStatus(reservation.getId(), "CANCELLED");
            
            // 6. Rafraîchir la liste des réservations
            loadReservations();
            
            showAlert("Succès", 
                     "Réservation annulée avec succès\nPlaces disponibles: " + trajet.getAvailableSeats(), 
                     Alert.AlertType.INFORMATION);
            
        } catch (SQLException e) {
            showAlert("Erreur", 
                     "Erreur lors de l'annulation : " + e.getMessage(), 
                     Alert.AlertType.ERROR);
        }
    }

    /**
     * Affiche une boîte de dialogue
     * @param title Titre de la boîte de dialogue
     * @param content Contenu du message
     * @param type Type d'alerte
     */
    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleReservationAction(Annonce annonce) {
        try {
            Reservation reservation = reservationService.createReservation(
                annonce.getId(),
                Long.valueOf(getCurrentUserId()),
                ""
            );
            
            if (reservation != null) {
                loadReservations();
                showAlert("Succès", "Réservation créée avec succès", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Erreur", "Impossible de créer la réservation", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de la création de la réservation: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private Long getCurrentUserId() {
        return Long.valueOf(1);
    }
} 