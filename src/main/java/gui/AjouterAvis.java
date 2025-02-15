package gui;

import entities.Avis;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import Services.AvisService;

import java.io.IOException;
import java.sql.SQLException;

public class AjouterAvis {

    private final AvisService avisService = new AvisService();

    @FXML
    private TextArea commentaireArea;

    @FXML
    private ToggleGroup ratingGroup;

    @FXML
    private RadioButton star1, star2, star3, star4, star5;

    @FXML
    void initialize() {
        ratingGroup = new ToggleGroup();
        star1.setToggleGroup(ratingGroup);
        star2.setToggleGroup(ratingGroup);
        star3.setToggleGroup(ratingGroup);
        star4.setToggleGroup(ratingGroup);
        star5.setToggleGroup(ratingGroup);
    }

    @FXML
    void ajouter(ActionEvent event) {
        if (commentaireArea.getText().isEmpty() || ratingGroup.getSelectedToggle() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Veuillez donner une note et un commentaire");
            alert.showAndWait();
            return;
        }

        int note = Integer.parseInt(((RadioButton) ratingGroup.getSelectedToggle()).getText());
        
        Avis avis = new Avis(
            commentaireArea.getText(),
            note,
            "user123" // À remplacer par l'utilisateur connecté
        );

        try {
            avisService.create(avis);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setContentText("Merci pour votre avis !");
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
        commentaireArea.clear();
        ratingGroup.selectToggle(null);
    }

    @FXML
    void voirAvis(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/VoirAvis.fxml"));
            Parent root = loader.load();
            commentaireArea.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Erreur lors du chargement de la page : " + e.getMessage());
            alert.showAndWait();
        }
    }
} 