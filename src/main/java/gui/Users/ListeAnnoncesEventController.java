package gui.Users;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import main.entities.Event;
import main.entities.Annonce;
import main.entities.Reservation;
import Services.AnnonceService;
import Services.ReservationService;

import java.sql.SQLException;
import java.util.List;

public class ListeAnnoncesEventController {
    @FXML private ListView<Annonce> annonceListView;
    @FXML private Label eventTitleLabel;
    
    private Event event;
    private AnnonceService annonceService;
    private ReservationService reservationService;

    public void initData(Event event) {
        this.event = event;
        this.annonceService = new AnnonceService();
        this.reservationService = new ReservationService();
        
        eventTitleLabel.setText("Annonces pour l'événement: " + event.getNom());
        setupAnnonceListView();
        loadAnnonces();
    }

    private void setupAnnonceListView() {
        annonceListView.setCellFactory(lv -> new ListCell<Annonce>() {
            @Override
            protected void updateItem(Annonce annonce, boolean empty) {
                super.updateItem(annonce, empty);
                if (empty || annonce == null) {
                    setGraphic(null);
                } else {
                    VBox container = new VBox(5);
                    setupAnnonceCell(container, annonce);
                    setGraphic(container);
                }
            }
        });
    }

    private void setupAnnonceCell(VBox container, Annonce annonce) {
        // Informations de l'annonce
        Label titleLabel = new Label(annonce.getTitre());
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        
        Label descLabel = new Label(annonce.getDescription());
        Label seatsLabel = new Label("Places disponibles: " + annonce.getAvailableSeats());
        Label statusLabel = new Label("Status: " + annonce.getStatus());
        
        VBox infoContainer = new VBox(5);
        infoContainer.getChildren().addAll(titleLabel, descLabel, seatsLabel, statusLabel);

        // Conteneur pour les boutons
        HBox buttonsContainer = new HBox(10);
        buttonsContainer.setAlignment(Pos.CENTER_RIGHT);

        // Création des boutons
        Button reserverBtn = new Button("Réserver");
        Button supprimerBtn = new Button("Supprimer");

        // Style des boutons
        reserverBtn.getStyleClass().add("button-primary");
        supprimerBtn.getStyleClass().add("button-danger");

        // Actions des boutons
        reserverBtn.setOnAction(e -> handleReserver(annonce));
        supprimerBtn.setOnAction(e -> handleSupprimer(annonce));

        // Désactiver le bouton réserver si plus de places disponibles ou annonce fermée
        boolean isClosed = "CLOSED".equals(annonce.getStatus()) || 
                         "TERMINATED".equals(annonce.getStatus()) ||
                         annonce.getAvailableSeats() <= 0;
        reserverBtn.setDisable(isClosed);

        // Ajouter les boutons au conteneur
        buttonsContainer.getChildren().addAll(reserverBtn, supprimerBtn);

        // Ajouter tous les éléments au conteneur principal
        container.getChildren().addAll(infoContainer, buttonsContainer);
        container.setStyle("-fx-padding: 10; -fx-border-color: #ddd; -fx-border-radius: 5;");
    }

    private void handleReserver(Annonce annonce) {
        try {
            // Créer une boîte de dialogue pour le commentaire
            TextArea commentArea = new TextArea();
            commentArea.setPromptText("Ajoutez un commentaire (optionnel)");
            commentArea.setPrefRowCount(3);

            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Réserver une place");
            dialog.setHeaderText("Réservation pour: " + annonce.getTitre());

            VBox content = new VBox(10);
            content.getChildren().addAll(
                new Label("Places disponibles : " + annonce.getAvailableSeats()),
                new Label("Commentaire :"),
                commentArea
            );
            dialog.getDialogPane().setContent(content);

            ButtonType reserverButtonType = new ButtonType("Réserver", ButtonBar.ButtonData.OK_DONE);
            ButtonType annulerButtonType = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(reserverButtonType, annulerButtonType);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == reserverButtonType) {
                    return commentArea.getText();
                }
                return null;
            });

            dialog.showAndWait().ifPresent(comment -> {
                if (comment != null) { // Seulement si l'utilisateur a cliqué sur Réserver
                    try {
                        // Créer la réservation
                        Reservation reservation = new Reservation();
                        reservation.setAnnonceId(annonce.getId());
                        reservation.setPassengerId(1); // TODO: Remplacer par l'ID de l'utilisateur connecté
                        reservation.setComment(comment);
                        reservation.setStatus("PENDING");

                        reservationService.create(reservation);

                        // Mettre à jour le nombre de places disponibles
                        annonce.setAvailableSeats(annonce.getAvailableSeats() - 1);
                        annonceService.update(annonce);

                        // Vérifier si l'annonce doit être fermée
                        if (annonce.getAvailableSeats() <= 0) {
                            annonceService.updateStatus(annonce, "CLOSED");
                        }

                        showAlert(Alert.AlertType.INFORMATION, "Succès", 
                                 "Votre réservation a été effectuée avec succès!");
                        
                        loadAnnonces(); // Rafraîchir la liste seulement après une réservation réussie
                    } catch (SQLException e) {
                        showAlert(Alert.AlertType.ERROR, "Erreur", 
                                 "Erreur lors de la réservation: " + e.getMessage());
                    }
                }
                // Si comment est null, l'utilisateur a annulé, ne rien faire
            });
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                     "Erreur lors de la réservation: " + e.getMessage());
        }
    }

    private void handleSupprimer(Annonce annonce) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer l'annonce");
        alert.setContentText("Voulez-vous vraiment supprimer cette annonce ?");

        ButtonType supprimerButtonType = new ButtonType("Supprimer", ButtonBar.ButtonData.OK_DONE);
        ButtonType annulerButtonType = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(supprimerButtonType, annulerButtonType);

        alert.showAndWait().ifPresent(response -> {
            if (response == supprimerButtonType) { // Seulement si l'utilisateur confirme la suppression
                try {
                    annonceService.delete(annonce);
                    loadAnnonces();
                    showAlert(Alert.AlertType.INFORMATION, "Succès", 
                             "L'annonce a été supprimée avec succès");
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", 
                             "Erreur lors de la suppression de l'annonce: " + e.getMessage());
                }
            }
            // Si l'utilisateur a cliqué sur Annuler, ne rien faire
        });
    }

    private void loadAnnonces() {
        try {
            List<Annonce> annonces = annonceService.readByEventId(event.getIdEvent());
            annonceListView.getItems().clear();
            if (annonces.isEmpty()) {
                System.out.println("Aucune annonce trouvée pour l'événement " + event.getIdEvent());
            } else {
                System.out.println("Nombre d'annonces trouvées: " + annonces.size());
            }
            annonceListView.getItems().addAll(annonces);
        } catch (SQLException e) {
            e.printStackTrace(); // Pour voir l'erreur complète
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                     "Erreur lors du chargement des annonces: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 