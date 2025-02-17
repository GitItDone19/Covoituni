package gui.Avis;

import entities.Avis;
import entities.User;
import Services.Avis.AvisService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;

public class AjouterAvisController implements Initializable {
    
    @FXML
    private HBox starsContainer;
    
    @FXML
    private TextArea commentaireField;
    
    private int currentRating = 0;
    private final int MAX_STARS = 5;
    private Label[] stars;
    private AvisService avisService;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        avisService = new AvisService();
        setupStars();
    }
    
    private void setupStars() {
        stars = new Label[MAX_STARS];
        
        for (int i = 0; i < MAX_STARS; i++) {
            final int starIndex = i + 1;
            Label star = new Label("☆");  // Empty star Unicode character
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
        for (int i = 0; i < MAX_STARS; i++) {
            stars[i].setText(i < count ? "★" : "☆");  // Filled/Empty star Unicode characters
        }
    }
    
    @FXML
    private void handleSubmit() {
        if (currentRating == 0) {
            showAlert("Erreur", "Veuillez sélectionner une note");
            return;
        }
        
        if (commentaireField.getText().trim().isEmpty()) {
            showAlert("Erreur", "Veuillez ajouter un commentaire");
            return;
        }
        
        try {
            // For testing purposes, create a dummy user
            User currentUser = new User();
            currentUser.setId(1); // Set this to actual user ID
            
            Avis avis = new Avis(
                commentaireField.getText(),
                currentRating,
                currentUser
            );
            
            avisService.create(avis);
            showAlert("Succès", "Votre avis a été ajouté avec succès!");
            clearForm();
            
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur est survenue: " + e.getMessage());
        }
    }
    
    private void clearForm() {
        commentaireField.clear();
        currentRating = 0;
        showStars(0);
    }
    
    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 