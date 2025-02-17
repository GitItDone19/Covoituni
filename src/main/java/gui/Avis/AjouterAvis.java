package gui.Avis;

import entities.Avis;
import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import Services.Avis.AvisService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AjouterAvis implements Initializable {

    @FXML
    private HBox starsContainer;
    
    @FXML
    private TextArea commentaireArea;
    @FXML
    private Label commentaireError;
    @FXML
    private Label charCounter;

    private final int MAX_RATING = 5;
    private int currentRating = 0;
    private Label[] stars;
    private final AvisService avisService = new AvisService();

    private static final int MIN_CHARS = 10;
    private static final int MAX_CHARS = 500;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupStars();
        setupValidation();
    }

    private void setupStars() {
        stars = new Label[MAX_RATING];
        
        for (int i = 0; i < MAX_RATING; i++) {
            final int starIndex = i + 1;
            Label star = new Label("☆");  // Empty star
            star.setStyle("-fx-font-size: 24px; -fx-cursor: hand;");
            star.setTextFill(Color.GOLD);
            
            star.setOnMouseClicked(event -> updateRating(starIndex));
            star.setOnMouseEntered(event -> showStars(starIndex));
            star.setOnMouseExited(event -> showStars(currentRating));
            
            stars[i] = star;
            starsContainer.getChildren().add(star);
        }
    }

    private void updateRating(int rating) {
        currentRating = rating;
        showStars(currentRating);
    }

    private void showStars(int count) {
        for (int i = 0; i < MAX_RATING; i++) {
            stars[i].setText(i < count ? "★" : "☆");  // Filled or empty star
        }
    }

    private void setupValidation() {
        // Compteur de caractères
        commentaireArea.textProperty().addListener((observable, oldValue, newValue) -> {
            int length = newValue.length();
            charCounter.setText(length + "/" + MAX_CHARS + " caractères");
            
            if (length > MAX_CHARS) {
                charCounter.getStyleClass().add("char-counter-error");
                commentaireArea.getStyleClass().add("validation-error");
                commentaireError.setText("Le commentaire est trop long");
            } else if (length < MIN_CHARS) {
                charCounter.getStyleClass().remove("char-counter-error");
                commentaireArea.getStyleClass().add("validation-error");
                commentaireError.setText("Minimum " + MIN_CHARS + " caractères requis");
            } else {
                charCounter.getStyleClass().remove("char-counter-error");
                commentaireArea.getStyleClass().remove("validation-error");
                commentaireArea.getStyleClass().add("validation-success");
                commentaireError.setText("");
            }
        });
    }

    @FXML
    void soumettre(ActionEvent event) {
        // Validation avant soumission
        if (currentRating == 0) {
            showError("Veuillez sélectionner une note");
            return;
        }

        String commentaire = commentaireArea.getText();
        if (commentaire.length() < MIN_CHARS) {
            showError("Le commentaire doit contenir au moins " + MIN_CHARS + " caractères");
            commentaireArea.getStyleClass().add("validation-error");
            return;
        }

        if (commentaire.length() > MAX_CHARS) {
            showError("Le commentaire ne doit pas dépasser " + MAX_CHARS + " caractères");
            commentaireArea.getStyleClass().add("validation-error");
            return;
        }

        // Create a temporary user (in real app, get this from login session)
        User user = new User();
        user.setId(1);  // This should come from logged in user
        user.setNom("Test");
        user.setPrenom("User");

        // Create new Avis with constructor that sets the date
        Avis avis = new Avis(
            commentaire,
            currentRating,
            user
        );

        try {
            avisService.create(avis);
            showSuccess("Avis ajouté avec succès!");
            clearFields();
        } catch (SQLException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    void voirAvis(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/VoirAvis.fxml"));
            Parent root = loader.load();
            
            // Get the current scene's window
            Stage stage = (Stage) commentaireArea.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
            showError("Erreur lors du chargement de la page: " + e.getMessage());
        }
    }

    private void clearFields() {
        commentaireArea.clear();
        currentRating = 0;
        showStars(0);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void retourDashboard(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ClientDashboard.fxml"));
            commentaireArea.getScene().setRoot(root);
        } catch (IOException e) {
            showError("Erreur lors du chargement de la page: " + e.getMessage());
        }
    }
} 