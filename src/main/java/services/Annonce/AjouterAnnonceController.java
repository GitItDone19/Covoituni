package services.Annonce;

import entities.Annonce.Annonce;
import entities.Trajet.Trajet;
import services.Trajet.TrajetService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.util.StringConverter;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Contrôleur pour l'ajout d'une nouvelle annonce
 * Gère l'interface et la logique d'ajout d'annonces
 */
public class AjouterAnnonceController {
    // Champs du formulaire
    @FXML private TextField titreField;
    @FXML private TextArea descriptionField;
    @FXML private TextField driverIdField;
    @FXML private TextField carIdField;
    @FXML private ComboBox<Trajet> trajetComboBox;
    @FXML private ComboBox<String> statusComboBox;

    // Services utilisés
    private final AnnonceService annonceService = new AnnonceService();
    private final TrajetService trajetService = new TrajetService();
    private AnnonceController parentController; // Ajout de la référence au contrôleur parent

    /**
     * Initialise le contrôleur et configure les composants
     */
    @FXML
    public void initialize() {
        statusComboBox.setItems(FXCollections.observableArrayList("OPEN", "CLOSED"));
        statusComboBox.setValue("OPEN");  // Valeur par défaut
        
        // Charger la liste des trajets
        try {
            trajetComboBox.setItems(FXCollections.observableArrayList(trajetService.readAll()));
            
            // Configurer l'affichage des trajets dans le ComboBox
            trajetComboBox.setConverter(new StringConverter<Trajet>() {
                @Override
                public String toString(Trajet trajet) {
                    if (trajet == null) return null;
                    return trajet.getTitre() + " (" + trajet.getDeparturePoint() + " → " + trajet.getArrivalPoint() + ")";
                }

                @Override
                public Trajet fromString(String string) {
                    return null;
                }
            });
        } catch (SQLException e) {
            showAlert("Erreur", "Impossible de charger les trajets", Alert.AlertType.ERROR);
        }
    }

    // Méthode pour définir le contrôleur parent
    public void setParentController(AnnonceController controller) {
        this.parentController = controller;
    }

    /**
     * Gère l'ajout d'une nouvelle annonce
     * Valide les données et crée l'annonce dans la base de données
     */
    @FXML
    void ajouterAnnonce() {
        try {
            // Validation des champs
            StringBuilder erreurs = new StringBuilder();
            
            // Vérification du titre
            if (titreField.getText().trim().isEmpty()) {
                erreurs.append("- Le titre est obligatoire\n");
            } else if (titreField.getText().length() < 5) {
                erreurs.append("- Le titre doit contenir au moins 5 caractères\n");
            }
            
            // Vérification de la description
            if (descriptionField.getText().trim().isEmpty()) {
                erreurs.append("- La description est obligatoire\n");
            } else if (descriptionField.getText().length() < 10) {
                erreurs.append("- La description doit contenir au moins 10 caractères\n");
            }
            
            // Vérification du trajet sélectionné
            if (trajetComboBox.getValue() == null) {
                erreurs.append("- Vous devez sélectionner un trajet\n");
            }
            
            // Vérification de la voiture sélectionnée
            if (carIdField.getText().isEmpty()) {
                erreurs.append("- Vous devez sélectionner une voiture\n");
            }
            
            // S'il y a des erreurs, les afficher
            if (erreurs.length() > 0) {
                showAlert("Erreurs de saisie", erreurs.toString(), Alert.AlertType.ERROR);
                return;
            }

            // Si tout est valide, créer l'annonce
            Annonce annonce = new Annonce();
            annonce.setTitre(titreField.getText().trim());
            annonce.setDescription(descriptionField.getText().trim());
            annonce.setDatePublication(new Timestamp(System.currentTimeMillis()));
            annonce.setDriverId(Integer.parseInt(driverIdField.getText()));
            annonce.setCarId(Integer.parseInt(carIdField.getText()));
            annonce.setStatus("OPEN");
            annonce.setTrajetId(trajetComboBox.getValue().getId());

            annonceService.create(annonce);

            // Fermer la fenêtre
            ((Stage) titreField.getScene().getWindow()).close();
            
            // Rafraîchir la liste des annonces
            if (parentController != null) {
                parentController.loadAnnonces();
            }

            showAlert("Succès", "Annonce ajoutée avec succès", Alert.AlertType.INFORMATION);

        } catch (NumberFormatException e) {
            showAlert("Erreur", "Les IDs doivent être des nombres", Alert.AlertType.ERROR);
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de l'ajout : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private boolean validateFields() {
        return !titreField.getText().isEmpty() &&
               !descriptionField.getText().isEmpty() &&
               !driverIdField.getText().isEmpty() &&
               !carIdField.getText().isEmpty() &&
               trajetComboBox.getValue() != null &&
               statusComboBox.getValue() != null;
    }

    @FXML
    void annuler() {
        ((Stage) titreField.getScene().getWindow()).close();
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
