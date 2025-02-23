package gui.Users;

import entities.Avis;
import entities.Role;
import entities.User;
import Services.AvisService;
import Services.ServiceUser;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Services.ServiceUser;
import java.sql.SQLException;
import java.util.Date;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class AjouterAvisController {
    @FXML private TextField tfRating; // Text field for rating
    @FXML private Button btnSubmit; // Submit button
    @FXML private Button btnRetour; // Return button
    @FXML private ComboBox<User> cbConducteurs;
    @FXML private ImageView star1;
    @FXML private ImageView star2;
    @FXML private ImageView star3;
    @FXML private ImageView star4;
    @FXML private ImageView star5;
    private User currentUser; // The current user (passager)
    private User conducteur; // The selected conducteur
    private AvisService avisService;
    private ObservableList<User> conducteursList;
    private int currentRating = 0;
    private Image emptyStarImage;
    private Image fullStarImage;

    public void setCurrentUser(User user) {
        this.currentUser = user;
        loadConducteurs();  // Load drivers when user is set
    }

    public void setConducteur(User conducteur) {
        this.conducteur = conducteur;
    }

    @FXML
    private void initialize() {
        avisService = new AvisService();
        
        // Load star images
        emptyStarImage = new Image(getClass().getResourceAsStream("/images/emptystar.png"));
        fullStarImage = new Image(getClass().getResourceAsStream("/images/fullstar.png"));
        
        // Add hover effects for stars
        ImageView[] stars = {star1, star2, star3, star4, star5};
        for (int i = 0; i < stars.length; i++) {
            final int rating = i + 1;
            stars[i].setOnMouseEntered(e -> updateStarsOnHover(rating));
            stars[i].setOnMouseExited(e -> updateStarsToCurrentRating());
        }
    }

    private void loadConducteurs() {
        try {
            ServiceUser serviceUser = new ServiceUser();
            conducteursList = FXCollections.observableArrayList(
                serviceUser.getUsersByRole(Role.DRIVER_CODE)
            );
            cbConducteurs.setItems(conducteursList);
            cbConducteurs.setCellFactory(lv -> new ListCell<User>() {
                @Override
                protected void updateItem(User user, boolean empty) {
                    super.updateItem(user, empty);
                    setText(user != null ? user.getFullName() : "");
                }
            });
            cbConducteurs.setButtonCell(new ListCell<User>() {
                @Override
                protected void updateItem(User user, boolean empty) {
                    super.updateItem(user, empty);
                    setText(user != null ? user.getFullName() : "");
                }
            });
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load drivers: " + e.getMessage());
        }
    }

    @FXML
    private void handleStarClick(MouseEvent event) {
        ImageView clickedStar = (ImageView) event.getSource();
        int rating = 0;
        
        if (clickedStar == star1) rating = 1;
        else if (clickedStar == star2) rating = 2;
        else if (clickedStar == star3) rating = 3;
        else if (clickedStar == star4) rating = 4;
        else if (clickedStar == star5) rating = 5;
        
        currentRating = rating;
        updateStarsToCurrentRating();
    }

    private void updateStarsOnHover(int hoveredRating) {
        ImageView[] stars = {star1, star2, star3, star4, star5};
        for (int i = 0; i < stars.length; i++) {
            stars[i].setImage(i < hoveredRating ? fullStarImage : emptyStarImage);
        }
    }

    private void updateStarsToCurrentRating() {
        ImageView[] stars = {star1, star2, star3, star4, star5};
        for (int i = 0; i < stars.length; i++) {
            stars[i].setImage(i < currentRating ? fullStarImage : emptyStarImage);
        }
    }

    @FXML
    private void handleSubmit() {
        User selectedConducteur = cbConducteurs.getValue();
        if (selectedConducteur == null) {
            showAlert(Alert.AlertType.WARNING, "Validation", "Veuillez sélectionner un conducteur");
            return;
        }

        if (currentRating == 0) {
            showAlert(Alert.AlertType.WARNING, "Validation", "Veuillez donner une note");
            return;
        }

        // Create the new Avis
        Avis avis = new Avis(0, currentRating, currentUser, selectedConducteur, new Date());
        try {
            avisService.create(avis); // Save the review

            // Update the driver's average rating
            updateDriverRating(selectedConducteur.getId(), currentRating);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Avis soumis avec succès!");
            currentRating = 0;
            updateStarsToCurrentRating();
            cbConducteurs.setValue(null);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la soumission: " + e.getMessage());
        }
    }

    private void updateDriverRating(int driverId, int newRating) throws SQLException {
        ServiceUser serviceUser = new ServiceUser();
        double averageRating = serviceUser.calculateNewAverageRating(driverId, newRating);
        serviceUser.updateUserRating(driverId, averageRating);
    }

    @FXML
    private void handleRetour() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Users/DashboardUser.fxml"));
            Parent root = loader.load();
            DashboardUserController controller = loader.getController();
            controller.setCurrentUser(currentUser); // Pass the current user
            Stage stage = (Stage) btnRetour.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error returning to dashboard: " + e.getMessage());
        }
    }

    @FXML
    private void handleButtonHover(MouseEvent event) {
        Button button = (Button) event.getSource();
        if (button == btnSubmit) {
            button.setStyle("-fx-background-color: #098f5a; -fx-text-fill: white; -fx-font-size: 14px; " +
                           "-fx-font-weight: bold; -fx-padding: 12 30; -fx-background-radius: 8; " +
                           "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 4, 0, 0, 2);");
        } else if (button == btnRetour) {
            button.setStyle("-fx-background-color: #28444c; -fx-text-fill: white; -fx-font-size: 14px; " +
                           "-fx-font-weight: bold; -fx-padding: 12 30; -fx-background-radius: 8; " +
                           "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 4, 0, 0, 2);");
        }
    }

    @FXML
    private void handleButtonExit(MouseEvent event) {
        Button button = (Button) event.getSource();
        if (button == btnSubmit) {
            button.setStyle("-fx-background-color: #24f0a0; -fx-text-fill: white; -fx-font-size: 14px; " +
                           "-fx-font-weight: bold; -fx-padding: 12 30; -fx-background-radius: 8; " +
                           "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 2);");
        } else if (button == btnRetour) {
            button.setStyle("-fx-background-color: #25765d; -fx-text-fill: white; -fx-font-size: 14px; " +
                           "-fx-font-weight: bold; -fx-padding: 12 30; -fx-background-radius: 8; " +
                           "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 2);");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 