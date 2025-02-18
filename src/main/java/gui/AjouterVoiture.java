package gui;

import entities.Car;
import entities.Categorie;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import services.CarService;
import services.CategorieService;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

public class AjouterVoiture implements Initializable {

    @FXML
    private TextField plaqueField;
    @FXML
    private TextField descriptionField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField couleurField;
    @FXML
    private TextField marqueField;
    @FXML
    private TextField modeleField;
    @FXML
    private ComboBox<Categorie> categorieComboBox;

    private final CarService carService = new CarService();
    private final CategorieService categorieService = new CategorieService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadCategories();
        datePicker.setValue(LocalDate.now());
    }

    private void loadCategories() {
        try {
            categorieComboBox.getItems().addAll(categorieService.readAll());
            categorieComboBox.setConverter(new StringConverter<Categorie>() {
                @Override
                public String toString(Categorie categorie) {
                    return categorie == null ? "" : categorie.getNom();
                }

                @Override
                public Categorie fromString(String string) {
                    return null; // Not needed for ComboBox
                }
            });
        } catch (SQLException e) {
            showError("Erreur", "Impossible de charger les catégories");
        }
    }

    @FXML
    private void handleAjouterButton() {
        if (!validateFields()) {
            return;
        }

        try {
            Car car = new Car(
                plaqueField.getText(),
                descriptionField.getText(),
                Date.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                couleurField.getText(),
                marqueField.getText(),
                modeleField.getText(),
                categorieComboBox.getValue().getId()
            );

            carService.create(car);
            closeWindow();
        } catch (SQLException e) {
            showError("Erreur", "Impossible d'ajouter la voiture: " + e.getMessage());
        }
    }

    @FXML
    private void handleAnnulerButton() {
        closeWindow();
    }

    private boolean validateFields() {
        StringBuilder errors = new StringBuilder();

        if (plaqueField.getText().isEmpty()) {
            errors.append("La plaque d'immatriculation est requise\n");
        }
        if (descriptionField.getText().isEmpty()) {
            errors.append("La description est requise\n");
        }
        if (datePicker.getValue() == null) {
            errors.append("La date d'immatriculation est requise\n");
        }
        if (couleurField.getText().isEmpty()) {
            errors.append("La couleur est requise\n");
        }
        if (marqueField.getText().isEmpty()) {
            errors.append("La marque est requise\n");
        }
        if (modeleField.getText().isEmpty()) {
            errors.append("Le modèle est requis\n");
        }
        if (categorieComboBox.getValue() == null) {
            errors.append("La catégorie est requise\n");
        }

        if (errors.length() > 0) {
            showError("Erreur de validation", errors.toString());
            return false;
        }

        return true;
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void closeWindow() {
        Stage stage = (Stage) plaqueField.getScene().getWindow();
        stage.close();
    }
} 