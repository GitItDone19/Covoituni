package services;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import main.MainFX;

/**
 * Contrôleur principal de l'application
 * Gère la navigation entre les différentes vues
 */
public class MainViewController {

    @FXML
    private Button annonceButton;  // Bouton pour afficher les annonces

    @FXML
    private VBox mainContent;

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

    // Getter pour mainContent
    public VBox getMainContent() {
        return mainContent;
    }
}
