package views;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import entities.Reservation.Reservation;
import entities.Annonce.Annonce;
import services.Annonce.AnnonceService;
import services.Reservation.ReservationService;
import java.util.List;

public class ReservationPassagerView extends VBox {
    private ListView<Reservation> listReservations;
    private ReservationService reservationService;
    private AnnonceService annonceService;

    public ReservationPassagerView() {
        this.setPadding(new Insets(15));
        this.setSpacing(10);
        
        // Titre
        Label title = new Label("Mes Réservations");
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
                        Label statusLabel = new Label("Statut: " + getStatusText(reservation.getStatus()));
                        String statusColor = switch (reservation.getStatus()) {
                            case "PENDING" -> "#FFA500";   // Orange
                            case "CONFIRMED" -> "#28A745"; // Vert
                            case "CANCELLED" -> "#DC3545"; // Rouge
                            case "REFUSED" -> "#FF4757";   // Rouge vif
                            default -> "#6C757D";          // Gris
                        };
                        statusLabel.setStyle("-fx-text-fill: " + statusColor + "; -fx-font-weight: bold;");
                        
                        // Bouton d'annulation uniquement
                        HBox actionBox = new HBox(10);
                        actionBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

                        if ("PENDING".equals(reservation.getStatus())) {
                            // Bouton d'annulation
                            Button annulerBtn = new Button("Annuler");
                            annulerBtn.setStyle("-fx-background-color: #DC3545; -fx-text-fill: white;");
                            annulerBtn.setOnAction(e -> annulerReservation(reservation));
                            actionBox.getChildren().add(annulerBtn);
                        }
                        
                        // Afficher le commentaire en lecture seule
                        if (reservation.getComment() != null && !reservation.getComment().isEmpty()) {
                            VBox commentBox = new VBox(5);
                            commentBox.setStyle("-fx-padding: 10; -fx-background-color: #f8f9fa; -fx-background-radius: 5;");
                            
                            Label commentTitle = new Label("Votre commentaire:");
                            commentTitle.setStyle("-fx-font-weight: bold;");
                            
                            Label commentText = new Label(reservation.getComment());
                            commentText.setStyle("-fx-font-style: italic; -fx-text-fill: #666666; -fx-wrap-text: true;");
                            
                            commentBox.getChildren().addAll(commentTitle, commentText);
                            container.getChildren().add(commentBox);
                        }
                        
                        container.getChildren().addAll(
                            reservationInfo,
                            dateInfo,
                            annonceInfo,
                            statusLabel,
                            actionBox
                        );
                        
                        setGraphic(container);
                    } catch (Exception e) {
                        setGraphic(new Label("Erreur: " + e.getMessage()));
                    }
                }
            }
        });
        
        this.getChildren().addAll(title, listReservations);
    }
    
    private void annulerReservation(Reservation reservation) {
        try {
            reservationService.updateStatus(reservation.getId(), "CANCELLED");
            refreshReservations();
        } catch (Exception e) {
            showAlert("Erreur", "Impossible d'annuler la réservation: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private void refreshReservations() {
        try {
            List<Reservation> reservations = reservationService.getReservationsByUserId(Long.valueOf(1L));
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

    private String getStatusText(String status) {
        return switch (status) {
            case "PENDING" -> "En attente";
            case "CONFIRMED" -> "Confirmé";
            case "CANCELLED" -> "Annulé par vous";
            case "REFUSED" -> "Refusé par le chauffeur";
            default -> status;
        };
    }
} 