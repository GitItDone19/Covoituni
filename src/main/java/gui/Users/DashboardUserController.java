package gui.Users;

import entities.User;
import entities.Role;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ResourceBundle;
import Services.ServiceUser;
import Services.TrajetService;

public class DashboardUserController implements Initializable {
    // UI Labels
    @FXML private Label lblUserName, lblUserEmail, lblActiveReservations, lblCO2Economy, lblRating, lblTrajetsCount;
    @FXML private ListView<?> listViewReservations;
    
    // Section containers
    @FXML private VBox passengerSection;
    @FXML private VBox driverSection;

    // Buttons
    @FXML private Button btnAddReclamation, btnAjouterAvis;
    @FXML private Button btnReservationsPassager, btnListeEvenements, btnListeAnnoncesEvent, btnListeAnnonces, btnHistorique;
    @FXML private Button btnReservationsChauffeur, btnModifierTrajet, btnModifierAnnonce, btnListeTrajet, btnAjoutTrajet;
    @FXML private Button btnAjoutAnnonce, btnAjoutEvent, btnModifierEvent;
    @FXML private Button btnAfficherVoitures, btnAjouterVoiture, btnModifierVoiture, btnAfficherCategories;
    @FXML private Button btnAjouterCategorie, btnModifierCategorie;

    private User currentUser;
    private ServiceUser serviceUser;
    private TrajetService trajetService;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        serviceUser = new ServiceUser();
        trajetService = new TrajetService();
        updateDashboardStats();
        updateUserInfo();
        checkUserRole();
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        updateUserInfo();
        updateDashboardStats();
        checkUserRole();

    }

    // Retrieve actual statistics from database
    private void updateDashboardStats() {
        try {
            int trajetsCount = 0;
            try {
                trajetsCount = trajetService.readAll().size();
            } catch (SQLException e) {
                System.err.println("Error retrieving trajet count: " + e.getMessage());
            }

            if (lblTrajetsCount != null) {
                lblTrajetsCount.setText(String.valueOf(trajetsCount));
            }

            double co2Economy = trajetsCount * 2.3; // Assuming 2.3 kg CO2 saved per trip
            if (lblCO2Economy != null) {
                lblCO2Economy.setText(String.format("%.1f kg", co2Economy));
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Erreur lors de la mise à jour des statistiques: " + e.getMessage());
        }
    }

    private void updateUserInfo() {
        if (currentUser != null) {
            lblUserName.setText(currentUser.getNom() + " " + currentUser.getPrenom());
            lblUserEmail.setText(currentUser.getEmail());
            if (lblRating != null) {
                lblRating.setText(String.format("%.1f", currentUser.getRating()));
            }
        }
    }

    private void checkUserRole() {
        if (currentUser == null) {
            hideAllRoleSpecificButtons();
            hideAllSections();
            return;
        }

        String roleCode = currentUser.getRoleCode();
        hideAllRoleSpecificButtons();
        hideAllSections();

        // Common buttons for all roles
        btnListeEvenements.setVisible(true);
        btnListeAnnoncesEvent.setVisible(true);
        btnListeAnnonces.setVisible(true);

        switch (roleCode) {
            case Role.PASSENGER_CODE:
                // Passenger-specific buttons
                setVisibility(true, btnReservationsPassager, btnHistorique, btnAjouterAvis);
                // Show passenger section, hide driver section
                passengerSection.setVisible(true);
                passengerSection.setManaged(true);
                driverSection.setVisible(false);
                driverSection.setManaged(false);
                break;

            case Role.DRIVER_CODE:
                // Driver-specific buttons
                setVisibility(true, btnReservationsChauffeur, btnModifierTrajet, btnModifierAnnonce,
                        btnListeTrajet, btnAjoutTrajet, btnAjoutAnnonce, btnAjoutEvent, btnModifierEvent,
                        btnAfficherVoitures, btnAjouterVoiture, btnModifierVoiture, btnAfficherCategories,
                        btnAjouterCategorie, btnModifierCategorie);
                // Show driver section, hide passenger section
                driverSection.setVisible(true);
                driverSection.setManaged(true);
                passengerSection.setVisible(false);
                passengerSection.setManaged(false);
                break;

            case Role.ADMIN_CODE:
                // Admin can see everything
                showAllButtons();
                showAllSections();
                break;
        }
    }

    private void hideAllRoleSpecificButtons() {
        setVisibility(false, btnReservationsPassager, btnHistorique, btnAjouterAvis,
                btnReservationsChauffeur, btnModifierTrajet, btnModifierAnnonce, btnListeTrajet,
                btnAjoutTrajet, btnAjoutAnnonce, btnAjoutEvent, btnModifierEvent, btnAfficherVoitures,
                btnAjouterVoiture, btnModifierVoiture, btnAfficherCategories, btnAjouterCategorie,
                btnModifierCategorie, btnListeEvenements, btnListeAnnoncesEvent, btnListeAnnonces);
    }

    private void showAllButtons() {
        setVisibility(true, btnReservationsPassager, btnHistorique, btnAjouterAvis,
                btnReservationsChauffeur, btnModifierTrajet, btnModifierAnnonce, btnListeTrajet,
                btnAjoutTrajet, btnAjoutAnnonce, btnAjoutEvent, btnModifierEvent, btnAfficherVoitures,
                btnAjouterVoiture, btnModifierVoiture, btnAfficherCategories, btnAjouterCategorie,
                btnModifierCategorie, btnListeEvenements, btnListeAnnoncesEvent, btnListeAnnonces);
    }

    private void hideAllSections() {
        // Hide and unmanage both sections to remove empty space
        if (passengerSection != null) {
            passengerSection.setVisible(false);
            passengerSection.setManaged(false);
        }
        if (driverSection != null) {
            driverSection.setVisible(false);
            driverSection.setManaged(false);
        }
    }
    
    private void showAllSections() {
        // Show and manage both sections
        if (passengerSection != null) {
            passengerSection.setVisible(true);
            passengerSection.setManaged(true);
        }
        if (driverSection != null) {
            driverSection.setVisible(true);
            driverSection.setManaged(true);
        }
    }

    private void setVisibility(boolean visible, Button... buttons) {
        for (Button btn : buttons) {
            if (btn != null) btn.setVisible(visible);
        }
    }

    // Generic method to load a view
    private void loadView(String fxmlPath, String title, boolean asDialog) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            // Try to set current user on controller if it supports it
            Object controller = loader.getController();
            if (controller != null && currentUser != null) {
                try {
                    java.lang.reflect.Method setUserMethod = controller.getClass().getMethod("setCurrentUser", User.class);
                    setUserMethod.invoke(controller, currentUser);
                } catch (Exception e) {
                    // Controller doesn't have setCurrentUser method - ignore
                }
            }

            if (asDialog) {
                Stage stage = new Stage();
                stage.setTitle(title);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(new Scene(root));
                stage.showAndWait();
            } else {
                Stage stage = (Stage) lblUserName.getScene().getWindow();
                stage.setTitle(title);
                stage.setScene(new Scene(root));
                stage.show();
            }
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur de navigation: " + e.getMessage());
        }
    }

    // Event handlers
    @FXML private void handleViewProfile() { loadView("/Users/ViewProfile.fxml", "Profile", false); }
    @FXML private void handleLogout() { loadView("/Users/LoginUser.fxml", "Login", false); }
    @FXML private void handleAddReclamation() { loadView("/Users/AddReclamation.fxml", "Ajouter Réclamation", false); }
    @FXML private void handleViewReclamations() { loadView("/Users/ViewReclamations.fxml", "Mes Réclamations", false); }
    @FXML private void handleAjouterAvis() { loadView("/Users/AjouterAvis.fxml", "Ajouter Avis", true); }
    @FXML private void handleViewTrips() { loadView("ViewTrips.fxml", "Mes Trajets", false); }
    @FXML private void handleReservationsPassager() { loadView("/ReservationsPassager.fxml", "Mes Réservations", false); }
    @FXML private void handleHistorique() { loadView("/Historique.fxml", "Historique", false); }
    @FXML private void handleListeAnnonces() { loadView("/ListeAnnonces.fxml", "Liste des Annonces", false); }
    @FXML private void handleListeAnnoncesEvent() { loadView("/ListeAnnoncesEvent.fxml", "Annonces d'Événements", false); }
    @FXML private void handleListeEvenements() { loadView("/ListeEvenements.fxml", "Liste des Événements", false); }
    @FXML private void handleReservationsChauffeur() { loadView("/ReservationsChauffeur.fxml", "Réservations", false); }
    @FXML private void handleModifierTrajet() { loadView("/ModifierTrajet.fxml", "Modifier Trajet", false); }
    @FXML private void handleModifierAnnonce() { loadView("/ModifierAnnonce.fxml", "Modifier Annonce", false); }
    @FXML private void handleListeTrajet() { loadView("/ListeTrajet.fxml", "Liste des Trajets", false); }
    @FXML private void handleAjoutTrajet() { loadView("/AjoutTrajet.fxml", "Ajouter Trajet", true); }
    @FXML private void handleAjoutAnnonce() { loadView("/AjoutAnnonce.fxml", "Ajouter Annonce", true); }
    @FXML private void handleAjoutEvent() { loadView("/AjoutEvent.fxml", "Ajouter Événement", true); }
    @FXML private void handleModifierEvent() { loadView("/ModifierEvent.fxml", "Modifier Événement", false); }
    @FXML private void handleAfficherVoitures() { loadView("/AfficherVoitures.fxml", "Mes Voitures", true); }
    @FXML private void handleAjouterVoiture() { loadView("/AjouterVoiture.fxml", "Ajouter une voiture", true); }
    @FXML private void handleModifierVoiture() { loadView("/ModifierVoiture.fxml", "Modifier Voiture", false); }
    @FXML private void handleAfficherCategories() { loadView("/AfficherCategories.fxml", "Catégories", false); }
    @FXML private void handleAjouterCategorie() { loadView("/AjouterCategorie.fxml", "Ajouter Catégorie", false); }
    @FXML private void handleModifierCategorie() { loadView("/ModifierCategorie.fxml", "Modifier Catégorie", false); }
    @FXML private void handleOpenMap() {
        // Use the path to the Users folder
        loadView("/Users/GoogleMapsView.fxml", "Cartographie", false);
    }
    @FXML private void handleOpenChatbot() {
        loadView("/Users/ChatbotView.fxml", "Assistant Covoituni", false);
    }

    @FXML
    private void handleOpenGoogleTranslator() {
        loadView("/Users/GoogleTranslatorView.fxml", "Google Translator", false);
    }
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}