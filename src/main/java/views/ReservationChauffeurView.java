package views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import entities.Reservation.Reservation;
import entities.Annonce.Annonce;
import services.Annonce.AnnonceService;
import services.Reservation.ReservationService;
import java.util.List;

public class ReservationChauffeurView extends VBox {
    private ListView<Reservation> listReservations;
    private ReservationService reservationService;
    private AnnonceService annonceService;

    public ReservationChauffeurView() {
        this.setPadding(new Insets(15));
        this.setSpacing(10);
        
        // Titre
        Label title = new Label("Réservations (Vue Chauffeur)");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        reservationService = new ReservationService();
        annonceService = new AnnonceService();
        listReservations = new ListView<>();
        
        // Configuration de la cellule personnalisée
        listReservations.setCellFactory(lv -> new ListCell<Reservation>() {
            @Override
            protected void updateItem(Reservation reservation, boolean empty) {
                super.updateItem(reservation, empty);
                if (empty || reservation == null) {
                    setGraphic(null);
                } else {
                    try {
                        // Conteneur principal pour chaque réservation
                        VBox container = new VBox(10);
                        container.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-border-color: #e0e0e0; -fx-border-radius: 5;");
                        
                        // Informations de la réservation
                        Annonce annonce = annonceService.readById(reservation.getAnnonceId());
                        
                        Label reservationInfo = new Label(String.format("Réservation #%d", reservation.getId()));
                        reservationInfo.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
                        
                        Label dateInfo = new Label("Date: " + reservation.getDateReservation());
                        Label annonceInfo = new Label("Annonce: " + annonce.getTitre());
                        
                        // Status avec couleur
                        Label statusLabel = new Label("Statut: " + reservation.getStatus());
                        String statusColor = switch (reservation.getStatus()) {
                            case "PENDING" -> "#FFA500";   // Orange
                            case "CONFIRMED" -> "#28A745"; // Vert
                            case "CANCELLED" -> "#DC3545"; // Rouge
                            case "REFUSED" -> "#FF4757";   // Rouge vif
                            default -> "#6C757D";          // Gris
                        };
                        statusLabel.setStyle("-fx-text-fill: " + statusColor + "; -fx-font-weight: bold;");
                        
                        // Informations de base
                        container.getChildren().addAll(
                            reservationInfo,
                            dateInfo,
                            annonceInfo,
                            statusLabel
                        );
                        
                        // Affichage du commentaire s'il existe
                        if (reservation.getComment() != null && !reservation.getComment().isEmpty()) {
                            VBox commentBox = new VBox(5);
                            commentBox.setStyle(
                                "-fx-padding: 10; " +
                                "-fx-background-color: #f8f9fa; " +
                                "-fx-background-radius: 5; " +
                                "-fx-border-color: #e0e0e0; " +
                                "-fx-border-radius: 5;"
                            );
                            
                            Label commentTitle = new Label("Commentaire du passager");
                            commentTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #2C3E50;");
                            
                            Label commentText = new Label(reservation.getComment());
                            commentText.setStyle(
                                "-fx-font-style: italic; " +
                                "-fx-text-fill: #666666; " +
                                "-fx-wrap-text: true; " +
                                "-fx-padding: 5 0 0 0;"
                            );
                            
                            // Icône de commentaire
                            Label iconLabel = new Label("💬");
                            iconLabel.setStyle("-fx-font-size: 16px;");
                            
                            HBox commentHeader = new HBox(10);
                            commentHeader.setAlignment(Pos.CENTER_LEFT);
                            commentHeader.getChildren().addAll(iconLabel, commentTitle);
                            
                            commentBox.getChildren().addAll(commentHeader, commentText);
                            container.getChildren().add(commentBox);
                        }
                        
                        // Boutons d'action
                        HBox buttonBox = new HBox(10);
                        if ("PENDING".equals(reservation.getStatus())) {
                            Button acceptBtn = new Button("Accepter");
                            acceptBtn.setStyle("-fx-background-color: #28A745; -fx-text-fill: white;");
                            acceptBtn.setOnAction(e -> accepterReservation(reservation));
                            
                            Button refuserBtn = new Button("Refuser");
                            refuserBtn.setStyle("-fx-background-color: #DC3545; -fx-text-fill: white;");
                            refuserBtn.setOnAction(e -> refuserReservation(reservation));
                            
                            buttonBox.getChildren().addAll(acceptBtn, refuserBtn);
                        }
                        
                        container.getChildren().add(buttonBox);
                        
                        setGraphic(container);
                    } catch (Exception e) {
                        setGraphic(new Label("Erreur: " + e.getMessage()));
                    }
                }
            }
        });
        
        this.getChildren().addAll(title, listReservations);
    }
    
    private void accepterReservation(Reservation reservation) {
        try {
            reservationService.updateStatus(reservation.getId(), "CONFIRMED");
            refreshReservations();
        } catch (Exception e) {
            showAlert("Erreur", "Impossible d'accepter la réservation: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private void refuserReservation(Reservation reservation) {
        try {
            reservationService.updateStatus(reservation.getId(), "REFUSED");
            refreshReservations();
        } catch (Exception e) {
            showAlert("Erreur", "Impossible de refuser la réservation: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private void refreshReservations() {
        try {
            List<Reservation> reservations = reservationService.getAllReservations();
            setReservations(reservations);
        } catch (Exception e) {
            showAlert("Erreur", "Impossible de rafraîchir les réservations: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    public void setReservations(List<Reservation> reservations) {
        listReservations.getItems().clear();
        listReservations.getItems().addAll(reservations);
    }
    
    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 