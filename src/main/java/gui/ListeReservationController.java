package gui;


import entities.Reservation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import Services.ReservationService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ListeReservationController {

    @FXML
    private VBox reservationContainer; // Conteneur pour les cartes de réservation

    private ReservationService reservationService = new ReservationService();

    @FXML
    public void initialize() {
        try {
            // Charger les réservations depuis la base de données
            List<Reservation> reservations = reservationService.readAll();

            // Afficher chaque réservation dans une carte
            for (Reservation reservation : reservations) {
                // Créer une carte pour la réservation
                HBox card = createReservationCard(reservation);
                reservationContainer.getChildren().add(card); // Ajouter la carte au conteneur
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private HBox createReservationCard(Reservation reservation) {
        // Créer une carte pour afficher les détails de la réservation
        HBox card = new HBox(10);
        card.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5; -fx-padding: 10;");

        // Afficher les détails de la réservation (sauf l'ID)
        Label dateLabel = new Label("Date: " + reservation.getDateReservation());
        Label clientLabel = new Label("Client: " + reservation.getNomClient());
        Label personnesLabel = new Label("Personnes: " + reservation.getNombrePersonnes());
        Label trajetLabel = new Label("Trajet: " + reservation.getTrajet());
        Label commentairesLabel = new Label("Commentaires: " + reservation.getCommentaires());

        // Boutons "Accepter" et "Refuser"
        Button acceptButton = new Button("Accepter");
        Button rejectButton = new Button("Refuser");

        // Gestion des événements pour les boutons
        acceptButton.setOnAction(event -> handleAccept(reservation));
        rejectButton.setOnAction(event -> handleReject(reservation));

        // Ajouter les éléments à la carte
        VBox details = new VBox(5, dateLabel, clientLabel, personnesLabel, trajetLabel, commentairesLabel);
        HBox buttons = new HBox(10, acceptButton, rejectButton);
        card.getChildren().addAll(details, buttons);

        return card;
    }

    private void handleAccept(Reservation reservation) {
        System.out.println("Réservation acceptée : " + reservation);
        // Ajoutez ici la logique pour accepter la réservation (par exemple, mise à jour dans la base de données)
    }

    private void handleReject(Reservation reservation) {
        System.out.println("Réservation refusée : " + reservation);
        // Ajoutez ici la logique pour refuser la réservation (par exemple, suppression dans la base de données)
    }
}