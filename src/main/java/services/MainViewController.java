package services;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import main.MainFX;
import views.ReservationChauffeurView;
import views.ReservationPassagerView;
import services.Reservation.ReservationService;
import entities.Reservation.Reservation;

import java.util.List;

/**
 * Contrôleur principal de l'application
 * Gère la navigation entre les différentes vues
 */
public class MainViewController {

    @FXML
    private Button annonceButton;  // Bouton pour afficher les annonces

    @FXML
    private BorderPane mainContent;

    private MainFX mainFX;

    public void setMainFX(MainFX mainFX) {
        this.mainFX = mainFX;
    }

    /**
     * Initialise le contrôleur
     * Cette méthode est appelée automatiquement après le chargement du FXML
     */
    @FXML
    public void initialize() {
        // L'affichage des annonces est géré dans MainFX
    }

    /**
     * Affiche la vue des annonces
     * @param event Événement déclencheur
     */
    @FXML
    public void showAnnonces(ActionEvent event) {
        if (mainFX != null) {
            System.out.println("Tentative d'affichage des annonces..."); // Log pour debug
            mainFX.showView("Annonce/AnnoncesView.fxml");
        } else {
            System.err.println("mainFX est null!"); // Log pour debug
        }
        if (annonceButton != null) {
            annonceButton.requestFocus();
        }
    }

    /**
     * Affiche la vue des trajets
     * @param event Événement déclencheur
     */
    @FXML
    void showTrajets(ActionEvent event) {
        if (mainFX != null) {
            mainFX.showView("Trajet/TrajetsView.fxml");
        }
    }

    /**
     * Affiche le formulaire d'ajout d'annonce
     * @param event Événement déclencheur
     */
    @FXML
    void showAjouterAnnonce(ActionEvent event) {
        if (mainFX != null) {
            mainFX.showView("Annonce/AjouterAnnonce.fxml");
        }
    }

    /**
     * Affiche le formulaire d'ajout de trajet
     * @param event Événement déclencheur
     */
    @FXML
    void showAjouterTrajet(ActionEvent event) {
        if (mainFX != null) {
            mainFX.showView("Trajet/AjouterTrajet.fxml");
        }
    }

    /**
     * Affiche la liste des réservations
     * @param event Événement déclencheur
     */
    @FXML
    void showReservations(ActionEvent event) {
        if (mainFX != null) {
            mainFX.showView("Reservation/ReservationView.fxml");
        }
    }

    @FXML
    public void showReservationsChauffeur(ActionEvent event) {
        if (mainFX != null) {
            ReservationChauffeurView reservationChauffeurView = new ReservationChauffeurView();
            mainContent.setCenter(reservationChauffeurView);
            
            // Charger les réservations
            ReservationService reservationService = new ReservationService();
            try {
                List<Reservation> reservations = reservationService.getAllReservations();
                reservationChauffeurView.setReservations(reservations);
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Erreur", "Impossible de charger les réservations: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    public void showReservationsPassager(ActionEvent event) {
        if (mainFX != null) {
            ReservationPassagerView reservationPassagerView = new ReservationPassagerView();
            mainContent.setCenter(reservationPassagerView);
            
            // Charger les réservations
            ReservationService reservationService = new ReservationService();
            try {
                List<Reservation> reservations = reservationService.getReservationsByUserId(Long.valueOf(1L));
                reservationPassagerView.setReservations(reservations);
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Erreur", "Impossible de charger les réservations: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Getter pour mainContent
    public BorderPane getMainContent() {
        return mainContent;
    }
}
