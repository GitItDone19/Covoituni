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
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ModifierVoiture implements Initializable {

    @FXML
    private TextField plaqueField;
    @FXML
    private TextArea descriptionField;
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
    private Car car;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadCategories();
    }

    public void setCar(Car car) {
        this.car = car;
        populateFields();
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

    private void populateFields() {
        plaqueField.setText(car.getPlaqueImatriculation());
        descriptionField.setText(car.getDescription());

        java.sql.Date sqlDate = (Date) car.getDateImatriculation();
        if (sqlDate != null) {
            datePicker.setValue(sqlDate.toLocalDate());
        }

        couleurField.setText(car.getCouleur());
        marqueField.setText(car.getMarque());
        modeleField.setText(car.getModele());

        categorieComboBox.getItems().stream()
                .filter(c -> c.getId() == car.getCategorieId())
                .findFirst()
                .ifPresent(categorieComboBox::setValue);
    }

    @FXML
    private void handleModifierButton() {
        if (!validateFields()) {
            return;
        }

        try {
            car.setPlaqueImatriculation(plaqueField.getText());
            car.setDescription(descriptionField.getText());

            LocalDate localDate = datePicker.getValue();
            if (localDate != null) {
                java.sql.Date sqlDate = java.sql.Date.valueOf(localDate);
                car.setDateImatriculation(sqlDate);
            }

            car.setCouleur(couleurField.getText());
            car.setMarque(marqueField.getText());
            car.setModele(modeleField.getText());
            car.setCategorieId(categorieComboBox.getValue().getId());

            carService.update(car);
            closeWindow();
        } catch (SQLException e) {
            showError("Erreur", "Impossible de modifier la voiture: " + e.getMessage());
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