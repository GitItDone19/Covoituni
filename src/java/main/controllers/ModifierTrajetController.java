package main.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.entities.Trajet;
import main.services.TrajetService;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class ModifierTrajetController implements Initializable {
    @FXML private TextField titreField;
    @FXML private TextField departureField;
    @FXML private TextField arrivalField;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> heureCombo;
    @FXML private ComboBox<String> minuteCombo;
    @FXML private TextField priceField;

    private TrajetService trajetService;
    private Trajet trajet;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        trajetService = new TrajetService();
        
        // Initialiser les ComboBox pour les heures et minutes
        for (int i = 0; i < 24; i++) {
            heureCombo.getItems().add(String.format("%02d", i));
        }
        for (int i = 0; i < 60; i += 5) {
            minuteCombo.getItems().add(String.format("%02d", i));
        }
    }

    public void setTrajet(Trajet trajet) {
        this.trajet = trajet;
        
        // Remplir les champs avec les données du trajet
        titreField.setText(trajet.getTitre());
        departureField.setText(trajet.getDeparturePoint());
        arrivalField.setText(trajet.getArrivalPoint());
        datePicker.setValue(trajet.getDepartureDate().toLocalDate());
        
        LocalTime time = trajet.getDepartureDate().toLocalTime();
        heureCombo.setValue(String.format("%02d", time.getHour()));
        minuteCombo.setValue(String.format("%02d", time.getMinute() - time.getMinute() % 5));
        
        priceField.setText(String.format("%.2f", trajet.getPrice()));
    }

    @FXML
    private void handleSave() {
        try {
            if (!validateFields()) {
                return;
            }

            trajet.setTitre(titreField.getText());
            trajet.setDeparturePoint(departureField.getText());
            trajet.setArrivalPoint(arrivalField.getText());
            trajet.setDepartureDate(getSelectedDateTime());
            trajet.setPrice(Double.parseDouble(priceField.getText()));

            trajetService.update(trajet);
            
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Le trajet a été modifié avec succès!");
            closeWindow();

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la modification du trajet: " + e.getMessage());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le prix doit être un nombre valide");
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private boolean validateFields() {
        if (titreField.getText().isEmpty() || departureField.getText().isEmpty() || 
            arrivalField.getText().isEmpty() || datePicker.getValue() == null || 
            heureCombo.getValue() == null || minuteCombo.getValue() == null || 
            priceField.getText().isEmpty()) {
            
            showAlert(Alert.AlertType.ERROR, "Erreur", "Tous les champs sont obligatoires");
            return false;
        }
        return true;
    }

    private LocalDateTime getSelectedDateTime() {
        return datePicker.getValue().atTime(
            Integer.parseInt(heureCombo.getValue()),
            Integer.parseInt(minuteCombo.getValue())
        );
    }

    private void closeWindow() {
        ((Stage) titreField.getScene().getWindow()).close();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 