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

public class AjouterAvisController {
    @FXML private TextField tfRating; // Text field for rating
    @FXML private Button btnSubmit; // Submit button
    @FXML private Button btnRetour; // Return button
    @FXML private ComboBox<User> cbConducteurs;
    private User currentUser; // The current user (passager)
    private User conducteur; // The selected conducteur
    private AvisService avisService;
    private ObservableList<User> conducteursList;

    public void setCurrentUser(User user) {
        this.currentUser = user;
        loadConducteurs();  // Load drivers when user is set
    }

    public void setConducteur(User conducteur) {
        this.conducteur = conducteur;
    }

    @FXML
    private void initialize() {
        avisService = new AvisService(); // Initialize the service
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
    private void handleSubmit() {
        User selectedConducteur = cbConducteurs.getValue();
        if(selectedConducteur == null) {
            showAlert(Alert.AlertType.WARNING, "Validation", "Please select a driver");
            return;
        }
        
        int rating;
        try {
            rating = Integer.parseInt(tfRating.getText().trim());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Validation", "Please enter a valid rating (1-5).");
            return;
        }

        if (rating < 1 || rating > 5) {
            showAlert(Alert.AlertType.WARNING, "Validation", "Rating must be between 1 and 5.");
            return;
        }

        Avis avis = new Avis(0, rating, currentUser, selectedConducteur, new Date());
        try {
            avisService.create(avis);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Avis submitted successfully!");
            tfRating.clear(); // Clear the input field
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error while submitting avis: " + e.getMessage());
        }
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

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 