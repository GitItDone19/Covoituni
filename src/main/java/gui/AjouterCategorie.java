package gui;

import entities.Categorie;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.CategorieService;

import java.sql.SQLException;

public class AjouterCategorie {

    @FXML
    private TextField nomField;
    @FXML
    private TextArea descriptionField;

    private final CategorieService categorieService = new CategorieService();

    @FXML
    private void handleAjouterButton() {
        if (!validateFields()) {
            return;
        }

        try {
            Categorie categorie = new Categorie(
                nomField.getText(),
                descriptionField.getText()
            );

            categorieService.create(categorie);
            closeWindow();
        } catch (SQLException e) {
            showError("Erreur", "Impossible d'ajouter la catégorie: " + e.getMessage());
        }
    }

    @FXML
    private void handleAnnulerButton() {
        closeWindow();
    }

    private boolean validateFields() {
        StringBuilder errors = new StringBuilder();

        if (nomField.getText().isEmpty()) {
            errors.append("Le nom est requis\n");
        }
        if (descriptionField.getText().isEmpty()) {
            errors.append("La description est requise\n");
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
        Stage stage = (Stage) nomField.getScene().getWindow();
        stage.close();
    }
}