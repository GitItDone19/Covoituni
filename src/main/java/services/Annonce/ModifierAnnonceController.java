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

public class ModifierAnnonceController {
    @FXML private TextField titreField;
    @FXML private TextArea descriptionField;
    @FXML private TextField driverIdField;
    @FXML private TextField carIdField;
    @FXML private ComboBox<Trajet> trajetComboBox;
    @FXML private ComboBox<String> statusComboBox;

    private Annonce annonce;
    private final AnnonceService annonceService = new AnnonceService();
    private final TrajetService trajetService = new TrajetService();

    @FXML
    public void initialize() {
        statusComboBox.setItems(FXCollections.observableArrayList("OPEN", "CLOSED"));
        
        try {
            trajetComboBox.setItems(FXCollections.observableArrayList(trajetService.readAll()));
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

    public void setAnnonce(Annonce annonce) {
        this.annonce = annonce;
        populateFields();
    }

    private void populateFields() {
        if (annonce != null) {
            titreField.setText(annonce.getTitre());
            descriptionField.setText(annonce.getDescription());
            driverIdField.setText(String.valueOf(annonce.getDriverId()));
            carIdField.setText(String.valueOf(annonce.getCarId()));
            statusComboBox.setValue(annonce.getStatus());
            
            try {
                Trajet trajet = trajetService.readById(annonce.getTrajetId());
                trajetComboBox.setValue(trajet);
            } catch (SQLException e) {
                showAlert("Erreur", "Impossible de charger le trajet", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    void modifierAnnonce() {
        try {
            if (!validateFields()) {
                showAlert("Erreur", "Veuillez remplir tous les champs correctement", Alert.AlertType.ERROR);
                return;
            }

            annonce.setTitre(titreField.getText());
            annonce.setDescription(descriptionField.getText());
            annonce.setDriverId(Integer.parseInt(driverIdField.getText()));
            annonce.setCarId(Integer.parseInt(carIdField.getText()));
            annonce.setTrajetId(trajetComboBox.getValue().getId());
            annonce.setStatus(statusComboBox.getValue());

            annonceService.update(annonce);
            
            Stage stage = (Stage) titreField.getScene().getWindow();
            stage.close();
            
            showAlert("Succès", "Annonce modifiée avec succès", Alert.AlertType.INFORMATION);
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer des valeurs numériques valides pour les IDs", Alert.AlertType.ERROR);
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de la modification : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void annuler() {
        Stage stage = (Stage) titreField.getScene().getWindow();
        stage.close();
    }

    private boolean validateFields() {
        return !titreField.getText().isEmpty() &&
               !descriptionField.getText().isEmpty() &&
               !driverIdField.getText().isEmpty() &&
               !carIdField.getText().isEmpty() &&
               trajetComboBox.getValue() != null &&
               statusComboBox.getValue() != null;
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}