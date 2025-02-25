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
    
    // Add new FXML fields for car and category management buttons
    @FXML private Button btnAfficherVoitures;
    @FXML private Button btnAjouterVoiture;
    @FXML private Button btnModifierVoiture;
    @FXML private Button btnAfficherCategories;
    @FXML private Button btnAjouterCategorie;
    @FXML private Button btnModifierCategorie;
    
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
                    
                    // Car and category management buttons
                    btnAfficherVoitures.setVisible(true);
                    btnAjouterVoiture.setVisible(true);
                    btnModifierVoiture.setVisible(true);
                    btnAfficherCategories.setVisible(true);
                    btnAjouterCategorie.setVisible(true);
                    btnModifierCategorie.setVisible(true);
                    
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
        
        // Car and category management buttons
        btnAfficherVoitures.setVisible(false);
        btnAjouterVoiture.setVisible(false);
        btnModifierVoiture.setVisible(false);
        btnAfficherCategories.setVisible(false);
        btnAjouterCategorie.setVisible(false);
        btnModifierCategorie.setVisible(false);
        
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
        
        // Car and category management buttons
        btnAfficherVoitures.setVisible(true);
        btnAjouterVoiture.setVisible(true);
        btnModifierVoiture.setVisible(true);
        btnAfficherCategories.setVisible(true);
        btnAjouterCategorie.setVisible(true);
        btnModifierCategorie.setVisible(true);
        
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

    @FXML
    private void handleAfficherVoitures() {
        try {
            // Try different paths to locate the file
            URL resource = getClass().getResource("/AfficherVoitures.fxml");
            if (resource == null) {
                resource = getClass().getResource("/Users/AfficherVoitures.fxml");
            }
            if (resource == null) {
                resource = getClass().getClassLoader().getResource("AfficherVoitures.fxml");
            }
            
            if (resource == null) {
                throw new IOException("Cannot find AfficherVoitures.fxml");
            }
            
            FXMLLoader loader = new FXMLLoader(resource);
            Parent root = loader.load();
            
            // Get controller and set current user
            gui.Users.AfficherVoitures controller = loader.getController();
            if (controller != null && currentUser != null) {
                controller.setCurrentUser(currentUser);
            }
            
            // Create a new stage for the form
            Stage stage = new Stage();
            stage.setTitle("Mes Voitures");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
            
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Erreur lors du chargement de la liste des voitures: " + e.getMessage());
        }
    }

    @FXML
    private void handleAjouterVoiture() {
        try {
            // Try different paths to locate the file
            URL resource = getClass().getResource("/AjouterVoiture.fxml");
            if (resource == null) {
                resource = getClass().getResource("/Users/AjouterVoiture.fxml");
            }
            if (resource == null) {
                resource = getClass().getClassLoader().getResource("AjouterVoiture.fxml");
            }
            
            if (resource == null) {
                throw new IOException("Cannot find AjouterVoiture.fxml");
            }
            
            FXMLLoader loader = new FXMLLoader(resource);
            Parent root = loader.load();
            
            // Get controller and set current user
            gui.Users.AjouterVoiture controller = loader.getController();
            if (controller != null && currentUser != null) {
                controller.setCurrentUser(currentUser);
            }
            
            // Create a new stage for the form
            Stage stage = new Stage();
            stage.setTitle("Ajouter une voiture");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
            
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Erreur lors du chargement du formulaire d'ajout de voiture: " + e.getMessage());
        }
    }

    @FXML
    private void handleModifierVoiture() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierVoiture.fxml"));
            Parent root = loader.load();
            
            // Get controller and set current user
            gui.Users.ModifierVoiture controller = loader.getController();
            if (controller != null && currentUser != null) {
                controller.setCurrentUser(currentUser);
            }
            
            Stage stage = (Stage) lblUserName.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Erreur lors du chargement du formulaire de modification de voiture: " + e.getMessage());
        }
    }

    @FXML
    private void handleAfficherCategories() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherCategories.fxml"));
            Parent root = loader.load();
            
            // Get controller and set current user
            gui.Users.AfficherCategories controller = loader.getController();
            if (controller != null && currentUser != null) {
                controller.setCurrentUser(currentUser);
            }
            
            Stage stage = (Stage) lblUserName.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Erreur lors du chargement de la liste des catégories: " + e.getMessage());
        }
    }

    @FXML
    private void handleAjouterCategorie() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterCategorie.fxml"));
            Parent root = loader.load();
            
            // Get controller and set current user
            gui.Users.AjouterCategorie controller = loader.getController();
            if (controller != null && currentUser != null) {
                controller.setCurrentUser(currentUser);
            }
            
            Stage stage = (Stage) lblUserName.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Erreur lors du chargement du formulaire d'ajout de catégorie: " + e.getMessage());
        }
    }

    @FXML
    private void handleModifierCategorie() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierCategorie.fxml"));
            Parent root = loader.load();
            
            // Get controller and set current user
            gui.Users.ModifierCategorie controller = loader.getController();
            if (controller != null && currentUser != null) {
                controller.setCurrentUser(currentUser);
            }
            
            Stage stage = (Stage) lblUserName.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Erreur lors du chargement du formulaire de modification de catégorie: " + e.getMessage());
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