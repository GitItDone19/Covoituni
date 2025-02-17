package gui.Reclamation;

import entities.Reclamation;
import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.Node;
import javafx.stage.Stage;
import Services.Reclamation.ReclamationService;
import gui.UserDashboardController;

import java.io.IOException;
import java.sql.SQLException;

public class AjouterReclamation {

    private final ReclamationService reclamationService = new ReclamationService();

    @FXML
    private TextArea descriptionArea;

    @FXML
    void ajouter(ActionEvent event) {
        if (descriptionArea.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Veuillez décrire votre problème");
            alert.showAndWait();
            return;
        }

        // user tempo 
        User user = new User();
        user.setId(1);  // menn 3and user amal log in
        user.setNom("Test");
        user.setPrenom("User");
        user.setEmail("test@example.com");

        Reclamation r = new Reclamation(
            descriptionArea.getText(),
            "EN_COURS",
            user    // obj user fi 3oudh id 
        );

        try {
            reclamationService.create(r);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setContentText("Réclamation ajoutée avec succès");
            alert.showAndWait();
            clearFields();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void clearFields() {
        descriptionArea.clear();
    }

    @FXML
    private void afficherReclamations(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserDashboard.fxml"));
            Parent root = loader.load();
            
            // Obech nekhdh lcontroller mta3 dashboard
            UserDashboardController dashboardController = loader.getController();
            
            // Changer la scène
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            
            // Ataffichi les reclamations ba3ed ma scene ttcharja
            dashboardController.showReclamations();
            
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
} 