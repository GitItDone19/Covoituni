package gui.Users;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import entities.Trajet;
import Services.TrajetService;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import entities.User;

public class AjoutTrajetController implements Initializable {
    @FXML private TextField titreField;
    @FXML private TextField departureField;
    @FXML private TextField arrivalField;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> heureCombo;
    @FXML private ComboBox<String> minuteCombo;
    @FXML private TextField priceField;

    private TrajetService trajetService;
    private User currentUser;

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

    @FXML
    private void handleSubmit() {
        try {
            // Validation des champs
            if (!validateFields()) {
                return;
            }

            // Création du trajet
            Trajet trajet = new Trajet(
                titreField.getText(),
                departureField.getText(),
                arrivalField.getText(),
                getSelectedDateTime(),
                Double.parseDouble(priceField.getText())
            );

            // Sauvegarde dans la base de données
            trajetService.create(trajet);

            // Message de succès
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Le trajet a été ajouté avec succès!");
            clearFields();

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout du trajet: " + e.getMessage());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le prix doit être un nombre valide");
        }
    }

    @FXML
    private void handleCancel() {
        clearFields();
    }

    @FXML
    private void handleReturnToDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Users/DashboardUser.fxml"));
            Parent root = loader.load();
            
            // Get controller and set current user
            DashboardUserController controller = loader.getController();
            if (currentUser != null) {
                controller.setCurrentUser(currentUser);
            }
            
            // Switch to dashboard scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not return to dashboard: " + e.getMessage());
        }
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

    private void clearFields() {
        titreField.clear();
        departureField.clear();
        arrivalField.clear();
        datePicker.setValue(null);
        heureCombo.setValue(null);
        minuteCombo.setValue(null);
        priceField.clear();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        // Any initialization based on user if needed
    }
} 