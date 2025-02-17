package services.Trajet;

import entities.Trajet.Trajet;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;

/**
 * Contrôleur pour la modification d'un trajet existant
 * Gère l'interface utilisateur et la logique de modification des trajets
 */
public class ModifierTrajetController {
    // Composants de l'interface utilisateur
    @FXML private TextField titreField;           // Champ pour le titre du trajet
    @FXML private TextField departurePointField;  // Champ pour le point de départ
    @FXML private TextField arrivalPointField;    // Champ pour le point d'arrivée
    @FXML private DatePicker departureDatePicker; // Sélecteur de date de départ
    @FXML private TextField availableSeatsField;  // Champ pour le nombre de places
    @FXML private TextField priceField;           // Champ pour le prix du trajet

    private Trajet trajet;  // Trajet en cours de modification
    private final TrajetService trajetService = new TrajetService();
    private TrajetController parentController;  // Référence au contrôleur parent

    /**
     * Configure le contrôleur parent pour les mises à jour de l'interface
     * @param controller Contrôleur parent à utiliser
     */
    public void setParentController(TrajetController controller) {
        this.parentController = controller;
    }

    /**
     * Initialise les champs du formulaire avec les données du trajet
     * @param trajet Trajet à modifier
     */
    public void setTrajet(Trajet trajet) {
        this.trajet = trajet;
        
        // Remplissage des champs avec les données actuelles
        titreField.setText(trajet.getTitre());
        departurePointField.setText(trajet.getDeparturePoint());
        arrivalPointField.setText(trajet.getArrivalPoint());
        departureDatePicker.setValue(trajet.getDepartureDate().toLocalDateTime().toLocalDate());
        availableSeatsField.setText(String.valueOf(trajet.getAvailableSeats()));
        priceField.setText(String.valueOf(trajet.getPrice()));
    }

    /**
     * Valide et enregistre les modifications du trajet
     * Vérifie les données saisies et met à jour le trajet dans la base de données
     */
    @FXML
    void modifierTrajet() {
        try {
            if (!validateFields()) {
                showAlert("Erreur de validation", 
                         "Veuillez remplir tous les champs correctement", 
                         Alert.AlertType.ERROR);
                return;
            }

            // Mise à jour des données du trajet
            trajet.setTitre(titreField.getText());
            trajet.setDeparturePoint(departurePointField.getText());
            trajet.setArrivalPoint(arrivalPointField.getText());
            trajet.setDepartureDate(Timestamp.valueOf(departureDatePicker.getValue().atStartOfDay()));
            trajet.setAvailableSeats(Integer.parseInt(availableSeatsField.getText()));
            trajet.setPrice(Double.parseDouble(priceField.getText()));

            // Sauvegarde des modifications
            trajetService.update(trajet);
            
            // Mise à jour de l'interface parent
            if (parentController != null) {
                parentController.loadTrajets();
            }

            // Fermeture de la fenêtre
            ((Stage) titreField.getScene().getWindow()).close();
            
            showAlert("Succès", "Trajet modifié avec succès", Alert.AlertType.INFORMATION);
            
        } catch (NumberFormatException e) {
            showAlert("Erreur de format", 
                     "Les places et le prix doivent être des nombres valides", 
                     Alert.AlertType.ERROR);
        } catch (SQLException e) {
            showAlert("Erreur de base de données", 
                     "Erreur lors de la modification : " + e.getMessage(), 
                     Alert.AlertType.ERROR);
        }
    }

    /**
     * Valide les champs du formulaire
     * @return true si tous les champs sont valides, false sinon
     */
    private boolean validateFields() {
        return !titreField.getText().isEmpty() &&
               !departurePointField.getText().isEmpty() &&
               !arrivalPointField.getText().isEmpty() &&
               departureDatePicker.getValue() != null &&
               !availableSeatsField.getText().isEmpty() &&
               !priceField.getText().isEmpty();
    }

    /**
     * Ferme la fenêtre de modification sans sauvegarder
     */
    @FXML
    void annuler() {
        ((Stage) titreField.getScene().getWindow()).close();
    }

    /**
     * Affiche une boîte de dialogue d'alerte
     * @param title Titre de l'alerte
     * @param content Contenu du message
     * @param type Type d'alerte
     */
    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 