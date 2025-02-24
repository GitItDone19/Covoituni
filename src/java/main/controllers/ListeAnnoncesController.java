package main.controllers;

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
import main.entities.Annonce;
import main.services.AnnonceService;
import main.entities.Reservation;
import main.services.ReservationService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ListeAnnoncesController implements Initializable {
    @FXML
    private ListView<Annonce> annonceListView;
    
    private AnnonceService annonceService;
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        annonceService = new AnnonceService();
        
        // Configuration de la cellule personnalisée
        annonceListView.setCellFactory(lv -> new ListCell<Annonce>() {
            private final Button modifyButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");
            private final Button reserveButton = new Button("Réserver");
            private final Button finishButton = new Button("Terminer");
            private final HBox buttons = new HBox(10, modifyButton, deleteButton, reserveButton, finishButton);
            private final HBox content = new HBox(15);
            
            {
                modifyButton.getStyleClass().add("button-secondary");
                deleteButton.getStyleClass().add("button-danger");
                reserveButton.getStyleClass().add("button-primary");
                finishButton.getStyleClass().add("button-warning");
                buttons.setStyle("-fx-alignment: center-right;");
                content.setStyle("-fx-alignment: center-left;");
            }

            @Override
            protected void updateItem(Annonce annonce, boolean empty) {
                super.updateItem(annonce, empty);
                
                if (empty || annonce == null) {
                    setGraphic(null);
                } else {
                    VBox details = new VBox(5);
                    Label titleLabel = new Label(annonce.getTitre());
                    titleLabel.getStyleClass().add("trajet-title");
                    
                    Label detailsLabel = new Label(
                        String.format("%s - Places disponibles: %d - Status: %s",
                            annonce.getDescription(),
                            annonce.getAvailableSeats(),
                            annonce.getStatus()
                        )
                    );
                    detailsLabel.getStyleClass().add("trajet-details");
                    
                    details.getChildren().addAll(titleLabel, detailsLabel);
                    
                    content.getChildren().clear();
                    content.getChildren().addAll(details, buttons);
                    HBox.setHgrow(details, Priority.ALWAYS);
                    
                    // Gérer la visibilité des boutons selon le statut
                    boolean isActive = "OPEN".equals(annonce.getStatus());
                    
                    modifyButton.setVisible(isActive);
                    deleteButton.setVisible(isActive);
                    
                    // N'afficher le bouton réserver que si l'annonce est OPEN et a des places
                    boolean canReserve = "OPEN".equals(annonce.getStatus()) && 
                                       annonce.getAvailableSeats() > 0;
                    reserveButton.setVisible(canReserve);
                    
                    // Le bouton terminer est visible pour les annonces OPEN et CLOSED
                    boolean canFinish = "OPEN".equals(annonce.getStatus()) || 
                                      "CLOSED".equals(annonce.getStatus());
                    finishButton.setVisible(canFinish);
                    
                    // Appliquer un style différent si l'annonce est fermée
                    if ("CLOSED".equals(annonce.getStatus())) {
                        getStyleClass().add("annonce-closed");
                    } else {
                        getStyleClass().remove("annonce-closed");
                    }
                    
                    modifyButton.setOnAction(e -> handleModify(annonce));
                    deleteButton.setOnAction(e -> handleDelete(annonce));
                    reserveButton.setOnAction(e -> handleReservation(annonce));
                    finishButton.setOnAction(e -> handleFinish(annonce));
                    
                    setGraphic(content);
                }
            }
        });
        
        loadAnnonces();
    }

    private void loadAnnonces() {
        try {
            annonceListView.getItems().clear();
            annonceListView.getItems().addAll(annonceService.readAll());
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                     "Erreur lors du chargement des annonces: " + e.getMessage());
        }
    }

    @FXML
    private void handleNouvelleAnnonce() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AjoutAnnonce.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Nouvelle annonce");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            
            // Recharger la liste après l'ajout
            loadAnnonces();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                     "Erreur lors de l'ouverture du formulaire d'ajout");
        }
    }

    private void handleModify(Annonce annonce) {
        try {
            // Charger le FXML de modification d'annonce
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ModifierAnnonce.fxml"));
            Parent root = loader.load();
            
            // Récupérer le contrôleur et initialiser les données
            ModifierAnnonceController controller = loader.getController();
            controller.initData(annonce);
            
            // Créer et configurer la nouvelle fenêtre
            Stage stage = new Stage();
            stage.setTitle("Modifier l'annonce");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            
            // Ajouter un événement à la fermeture de la fenêtre pour rafraîchir la liste
            stage.setOnHidden(e -> loadAnnonces());
            
            stage.show();
            
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                     "Erreur lors de l'ouverture du formulaire de modification: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleDelete(Annonce annonce) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer l'annonce");
        alert.setContentText("Voulez-vous vraiment supprimer cette annonce ?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
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
        });
    }

    private void handleFinish(Annonce annonce) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Terminer l'annonce");
        alert.setContentText("Voulez-vous vraiment marquer cette annonce comme terminée ?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    annonceService.updateStatus(annonce, "TERMINATED");
                    loadAnnonces();
                    showAlert(Alert.AlertType.INFORMATION, "Succès", 
                            "L'annonce a été marquée comme terminée et déplacée dans l'historique");
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", 
                            "Erreur lors de la mise à jour du statut: " + e.getMessage());
                }
            }
        });
    }

    private void handleReservation(Annonce annonce) {
        if (annonce.getAvailableSeats() <= 0) {
            showAlert(Alert.AlertType.WARNING, "Désolé", "Il n'y a plus de places disponibles pour cette annonce.");
            return;
        }

        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Réservation");
        dialog.setHeaderText("Réserver une place pour : " + annonce.getTitre());

        TextArea commentArea = new TextArea();
        commentArea.setPromptText("Votre commentaire (optionnel)");
        commentArea.setPrefRowCount(3);

        VBox content = new VBox(10);
        content.getChildren().addAll(
            new Label("Places disponibles : " + annonce.getAvailableSeats()),
            new Label("Commentaire :"),
            commentArea
        );
        dialog.getDialogPane().setContent(content);

        ButtonType reserverButtonType = new ButtonType("Réserver", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(reserverButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == reserverButtonType) {
                return commentArea.getText();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(comment -> {
            try {
                // Créer la réservation
                Reservation reservation = new Reservation();
                reservation.setAnnonceId(annonce.getId());
                reservation.setPassengerId(1); // TODO: Remplacer par l'ID de l'utilisateur connecté
                reservation.setComment(comment);
                reservation.setStatus("PENDING");

                ReservationService reservationService = new ReservationService();
                reservationService.create(reservation);

                // Mettre à jour le nombre de places disponibles
                annonce.setAvailableSeats(annonce.getAvailableSeats() - 1);
                annonceService.update(annonce);

                // Vérifier si l'annonce doit être fermée
                if (annonce.getAvailableSeats() <= 0) {
                    annonceService.closeAnnonce(annonce);
                }

                showAlert(Alert.AlertType.INFORMATION, "Succès", 
                         "Votre réservation a été effectuée avec succès!");
                
                // Recharger la liste pour refléter les changements
                loadAnnonces();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", 
                         "Erreur lors de la réservation: " + e.getMessage());
            }
        });
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 