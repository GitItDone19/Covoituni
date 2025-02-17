package services.Trajet;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import entities.Trajet.Trajet;
import java.sql.SQLException;

import javafx.scene.control.Button;

/**
 * Contrôleur pour la suppression d'un trajet
 * Gère l'interface de confirmation et le processus de suppression d'un trajet
 */
public class SupprimerTrajetController {
    // Composants de l'interface utilisateur
    @FXML
    private Button annulerButton;      // Bouton pour annuler la suppression

    // Données et services
    private Trajet trajet;            // Trajet à supprimer
    private final TrajetService trajetService = new TrajetService();
    private TrajetController parentController;  // Référence au contrôleur parent pour rafraîchir la vue

    /**
     * Définit le trajet à supprimer
     * @param trajet Le trajet qui sera supprimé
     */
    public void setTrajet(Trajet trajet) {
        this.trajet = trajet;
    }

    /**
     * Configure le contrôleur parent pour la mise à jour de l'interface
     * @param controller Le contrôleur parent qui sera notifié après la suppression
     */
    public void setParentController(TrajetController controller) {
        this.parentController = controller;
    }

    /**
     * Gère la confirmation et l'exécution de la suppression du trajet
     * Affiche une boîte de dialogue de confirmation avant de procéder
     */
    @FXML
    void confirmerSuppression() {
        // Création de la boîte de dialogue de confirmation
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText("Supprimer le trajet");
        confirmation.setContentText("Êtes-vous sûr de vouloir supprimer ce trajet ?");

        // Si l'utilisateur confirme la suppression
        if (confirmation.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                // Suppression du trajet de la base de données
                trajetService.delete(trajet.getId());
                
                // Mise à jour de la liste des trajets dans l'interface parent
                if (parentController != null) {
                    parentController.loadTrajets();
                }
                
                // Fermeture de la fenêtre de suppression
                ((Stage) annulerButton.getScene().getWindow()).close();
                
                // Affichage du message de succès
                showAlert("Succès", "Trajet supprimé avec succès", Alert.AlertType.INFORMATION);
            } catch (SQLException e) {
                // Gestion des erreurs de base de données
                showAlert("Erreur", "Erreur lors de la suppression : " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    /**
     * Ferme la fenêtre de suppression sans effectuer la suppression
     * Appelé lorsque l'utilisateur annule l'opération
     */
    @FXML
    void annuler() {
        ((Stage) annulerButton.getScene().getWindow()).close();
    }

    /**
     * Utilitaire pour afficher des messages à l'utilisateur
     * @param title Titre de la boîte de dialogue
     * @param message Message à afficher
     * @param type Type d'alerte (INFORMATION, ERROR, etc.)
     */
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
} 