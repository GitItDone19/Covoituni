package gui.Users;

import entities.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import java.io.IOException;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import entities.Role;

public class DashboardUserController implements Initializable {
    @FXML private Label lblUserName;
    @FXML private Label lblUserEmail;
    @FXML private Label lblActiveReservations;
    @FXML private Label lblCO2Economy;
    @FXML private Label lblRating;
    @FXML private ListView<?> listViewReservations;
    @FXML private Button btnAddReclamation;
    @FXML private Button btnAjouterAvis;
    
    // Add FXML fields for all buttons that need role-based visibility
    @FXML private Button btnReservationsPassager;
    @FXML private Button btnListeEvenements;
    @FXML private Button btnListeAnnoncesEvent;
    @FXML private Button btnListeAnnonces;
    @FXML private Button btnHistorique;
    
    @FXML private Button btnReservationsChauffeur;
    @FXML private Button btnModifierTrajet;
    @FXML private Button btnModifierAnnonce;
    @FXML private Button btnListeTrajet;
    @FXML private Button btnAjoutTrajet;
    @FXML private Button btnAjoutAnnonce;
    @FXML private Button btnAjoutEvent;
    @FXML private Button btnModifierEvent;
    
    private User currentUser;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize components
        System.out.println("btnAjouterAvis: " + btnAjouterAvis);
        updateUserInfo();
        checkUserRole();
    }
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
        updateUserInfo();
        checkUserRole();
    }
    
    private void updateUserInfo() {
        if (currentUser != null) {
            lblUserName.setText(currentUser.getNom() + " " + currentUser.getPrenom());
            lblUserEmail.setText(currentUser.getEmail());
        }
    }
    
    private void checkUserRole() {
        if (currentUser != null) {
            String roleCode = currentUser.getRoleCode();
            
            // First hide all role-specific buttons
            hideAllRoleSpecificButtons();
            
            // Show buttons based on role
            switch (roleCode) {
                case Role.PASSENGER_CODE:
                    // Passenger-specific buttons
                    btnReservationsPassager.setVisible(true);
                    btnHistorique.setVisible(true);
                    btnAjouterAvis.setVisible(true);
                    
                    // Shared buttons for passengers
                    btnListeEvenements.setVisible(true);
                    btnListeAnnoncesEvent.setVisible(true);
                    btnListeAnnonces.setVisible(true);
                    break;
                    
                case Role.DRIVER_CODE:
                    // Driver-specific buttons
                    btnReservationsChauffeur.setVisible(true);
                    btnModifierTrajet.setVisible(true);
                    btnModifierAnnonce.setVisible(true);
                    btnListeTrajet.setVisible(true);
                    btnAjoutTrajet.setVisible(true);
                    btnAjoutAnnonce.setVisible(true);
                    btnAjoutEvent.setVisible(true);
                    btnModifierEvent.setVisible(true);
                    
                    // Shared buttons for drivers
                    btnListeEvenements.setVisible(true);
                    btnListeAnnoncesEvent.setVisible(true);
                    btnListeAnnonces.setVisible(true);
                    break;
                    
                case Role.ADMIN_CODE:
                    // Admin can see everything
                    showAllButtons();
                    break;
                    
                default:
                    // Unknown role - hide all role-specific buttons
                    hideAllRoleSpecificButtons();
                    break;
            }
        } else {
            // No user logged in - hide all role-specific buttons
            hideAllRoleSpecificButtons();
        }
    }
    
    private void hideAllRoleSpecificButtons() {
        // Passenger buttons
        btnReservationsPassager.setVisible(false);
        btnHistorique.setVisible(false);
        btnAjouterAvis.setVisible(false);
        
        // Driver buttons
        btnReservationsChauffeur.setVisible(false);
        btnModifierTrajet.setVisible(false);
        btnModifierAnnonce.setVisible(false);
        btnListeTrajet.setVisible(false);
        btnAjoutTrajet.setVisible(false);
        btnAjoutAnnonce.setVisible(false);
        btnAjoutEvent.setVisible(false);
        btnModifierEvent.setVisible(false);
        
        // Shared buttons
        btnListeEvenements.setVisible(false);
        btnListeAnnoncesEvent.setVisible(false);
        btnListeAnnonces.setVisible(false);
    }
    
    private void showAllButtons() {
        // Passenger buttons
        btnReservationsPassager.setVisible(true);
        btnHistorique.setVisible(true);
        btnAjouterAvis.setVisible(true);
        
        // Driver buttons
        btnReservationsChauffeur.setVisible(true);
        btnModifierTrajet.setVisible(true);
        btnModifierAnnonce.setVisible(true);
        btnListeTrajet.setVisible(true);
        btnAjoutTrajet.setVisible(true);
        btnAjoutAnnonce.setVisible(true);
        btnAjoutEvent.setVisible(true);
        btnModifierEvent.setVisible(true);
        
        // Shared buttons
        btnListeEvenements.setVisible(true);
        btnListeAnnoncesEvent.setVisible(true);
        btnListeAnnonces.setVisible(true);
    }
    

    @FXML
    private void handleViewProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/Users/ViewProfile.fxml"));
            Parent root = loader.load();
            
            ViewProfileController controller = loader.getController();
            controller.setCurrentUser(currentUser);
            
            Scene scene = new Scene(root);
            Stage stage = (Stage) lblUserName.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Erreur lors de l'ouverture du profil: " + e.getMessage());
            alert.showAndWait();
        }
    }
    

    @FXML
    private void handleLogout() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Users/LoginUser.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) lblUserName.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Erreur lors de la déconnexion: " + e.getMessage());
            alert.showAndWait();
        }
    }
    
    @FXML
    private void handleAddReclamation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Users/AddReclamation.fxml"));
            Parent root = loader.load();
            
            AddReclamationController controller = loader.getController();
            controller.setCurrentUser(currentUser);
            
            Scene scene = new Scene(root);
            Stage stage = (Stage) lblUserName.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Ajouter Réclamation");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ouverture du formulaire de réclamation: " + e.getMessage());
        }
    }
    @FXML
    private void handleAjoutAnnonce() {
        handleNavigation("/AjoutAnnonce.fxml");
    }

    @FXML
    private void handleAjoutEvent() {
        handleNavigation("/AjoutEvent.fxml");
    }

    @FXML
    private void handleAjoutTrajet() {
        handleNavigation("/AjoutTrajet.fxml");
    }

    @FXML
    private void handleHistorique() {
        handleNavigation("/Historique.fxml");
    }

    @FXML
    private void handleListeAnnonces() {
        handleNavigation("/ListeAnnonces.fxml");
    }

    @FXML
    private void handleListeEvenements() {
        handleNavigation("/ListeAnnoncesEvent.fxml");
    }

    @FXML
    private void handleListeTrajet() {
        handleNavigation("/ListeTrajet.fxml");
    }

    @FXML
    private void handleModifierEvent() {
        handleNavigation("/ModifierEvent.fxml");
    }

    @FXML
    private void handleModifierTrajet() {
        handleNavigation("/ModifierTrajet.fxml");
    }

    @FXML
    private void handleReservationsPassager() {
        handleNavigation("/ReservationsPassager.fxml");
    }

    @FXML
    private void handleReservationsChauffeur() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ReservationsChauffeur.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) lblUserName.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Erreur lors du chargement des réservations: " + e.getMessage());
        }
    }

    @FXML
    private void handleModifierAnnonce() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierAnnonce.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) lblUserName.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Erreur lors du chargement de la modification d'annonce: " + e.getMessage());
        }
    }

    // Existing handleNavigation method
    private void handleNavigation(String fxmlPath) {
        try {
            URL url = getClass().getResource(fxmlPath);
            if (url == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur",
                        "FXML file not found: " + fxmlPath);
                return;
            }

            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();
            
            // Get the controller and set the current user if it implements the right method
            Object controller = loader.getController();
            if (controller != null && currentUser != null) {
                // Use reflection to check if the controller has setCurrentUser method
                try {
                    java.lang.reflect.Method setUserMethod = 
                        controller.getClass().getMethod("setCurrentUser", User.class);
                    setUserMethod.invoke(controller, currentUser);
                } catch (Exception e) {
                    System.out.println("Controller does not implement setCurrentUser: " + 
                        controller.getClass().getName());
                }
            }

            Stage stage = (Stage) lblUserName.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Erreur de navigation: " + e.getMessage());
        }
    }
    @FXML
    private void handleAjouterAvis() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Users/AjouterAvis.fxml"));
            Parent root = loader.load();
            
            AjouterAvisController controller = loader.getController();
            controller.setCurrentUser(currentUser);
            
            Stage stage = (Stage) lblUserName.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load review page: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setAlertType(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
} 