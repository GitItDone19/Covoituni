package services.Trajet;

import entities.Trajet.Trajet;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;

public class AjouterTrajetController {
    @FXML private TextField titreField;
    @FXML private TextField departurePointField;
    @FXML private TextField arrivalPointField;
    @FXML private DatePicker departureDatePicker;
    @FXML private TextField availableSeatsField;
    @FXML private TextField priceField;

    private final TrajetService trajetService = new TrajetService();
    private TrajetController parentController;  // Ajout d'une référence au contrôleur parent

    // Méthode pour définir le contrôleur parent
    public void setParentController(TrajetController controller) {
        this.parentController = controller;
    }

    @FXML
    private void ajouterTrajet() {
        try {
            // Validation des champs
            StringBuilder erreurs = new StringBuilder();
            
            // Vérification du titre
            if (titreField.getText().trim().isEmpty()) {
                erreurs.append("- Le titre est obligatoire\n");
            } else if (titreField.getText().length() < 3) {
                erreurs.append("- Le titre doit contenir au moins 3 caractères\n");
            }
            
            // Vérification du point de départ
            if (departurePointField.getText().trim().isEmpty()) {
                erreurs.append("- Le point de départ est obligatoire\n");
            }
            
            // Vérification du point d'arrivée
            if (arrivalPointField.getText().trim().isEmpty()) {
                erreurs.append("- Le point d'arrivée est obligatoire\n");
            }
            
            // Vérification de la date
            if (departureDatePicker.getValue() == null) {
                erreurs.append("- La date de départ est obligatoire\n");
            } else if (departureDatePicker.getValue().isBefore(LocalDate.now())) {
                erreurs.append("- La date de départ ne peut pas être dans le passé\n");
            }
            
            // Vérification du nombre de places
            try {
                int places = Integer.parseInt(availableSeatsField.getText().trim());
                if (places <= 0) {
                    erreurs.append("- Le nombre de places doit être supérieur à 0\n");
                } else if (places > 8) {
                    erreurs.append("- Le nombre de places ne peut pas dépasser 8\n");
                }
            } catch (NumberFormatException e) {
                erreurs.append("- Le nombre de places doit être un nombre valide\n");
            }
            
            // Vérification du prix
            try {
                double prix = Double.parseDouble(priceField.getText().trim());
                if (prix <= 0) {
                    erreurs.append("- Le prix doit être supérieur à 0\n");
                } else if (prix > 200) {
                    erreurs.append("- Le prix ne peut pas dépasser 200 DT\n");
                }
            } catch (NumberFormatException e) {
                erreurs.append("- Le prix doit être un nombre valide\n");
            }
            
            // S'il y a des erreurs, les afficher
            if (erreurs.length() > 0) {
                showAlert("Erreurs de saisie", erreurs.toString(), Alert.AlertType.ERROR);
                return;
            }

            // Si tout est valide, créer le trajet
            Trajet trajet = new Trajet();
            trajet.setTitre(titreField.getText().trim());
            trajet.setDeparturePoint(departurePointField.getText().trim());
            trajet.setArrivalPoint(arrivalPointField.getText().trim());
            trajet.setDepartureDate(Timestamp.valueOf(departureDatePicker.getValue().atStartOfDay()));
            trajet.setAvailableSeats(Integer.parseInt(availableSeatsField.getText().trim()));
            trajet.setPrice(Double.parseDouble(priceField.getText().trim()));

            trajetService.create(trajet);

            // Fermer la fenêtre
            ((Stage) titreField.getScene().getWindow()).close();
            
            // Rafraîchir la liste des trajets
            if (parentController != null) {
                parentController.loadTrajets();
            }

            showAlert("Succès", "Trajet ajouté avec succès", Alert.AlertType.INFORMATION);

        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de l'ajout : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void annuler() {
        ((Stage) titreField.getScene().getWindow()).close();
    }

    private boolean validateFields() {
        return !titreField.getText().isEmpty() &&
               !departurePointField.getText().isEmpty() &&
               !arrivalPointField.getText().isEmpty() &&
               departureDatePicker.getValue() != null &&
               !availableSeatsField.getText().isEmpty() &&
               !priceField.getText().isEmpty();
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

