package gui.Users;

import entities.Categorie;
import entities.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import Services.CategorieService;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;

import java.sql.SQLException;

public class AjouterCategorie {

    @FXML
    private TextField nomField;
    @FXML
    private TextArea descriptionField;

    private final CategorieService categorieService = new CategorieService();
    private User currentUser;

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

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeWindow() {
        Stage stage = (Stage) nomField.getScene().getWindow();
        stage.close();
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
}