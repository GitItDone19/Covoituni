package gui.Admin;

import entities.User.Role;
import entities.User.User;
import User.ServiceUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.io.IOException;
import javafx.stage.Stage;
import javafx.application.Platform;

public class GestionPassagerController implements Initializable {
    
    @FXML private TextField tfNom;
    @FXML private TextField tfPrenom;
    @FXML private TextField tfTel;
    @FXML private TextField tfEmail;
    @FXML private TextField tfRecherche;
    @FXML private Label lblReservationsCount;
    @FXML private ListView<User> listViewPassengers;
    
    private ServiceUser serviceUser;
    private ObservableList<User> passengersList;
    private User selectedPassenger;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            Stage stage = (Stage) tfNom.getScene().getWindow();
            stage.setMaximized(true);
            stage.setMinWidth(1200);
            stage.setMinHeight(800);
        });

        serviceUser = new ServiceUser();

        // Setup search listener
        tfRecherche.textProperty().addListener((obs, oldValue, newValue) -> {
            filterPassengers(newValue);
        });

        // Load initial data
        loadPassengers();

        // Setup selection listener
        listViewPassengers.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    selectedPassenger = newSelection;
                    showPassengerDetails(newSelection);
                }
            });

        // Setup ListView cell factory
        listViewPassengers.setCellFactory(lv -> new ListCell<User>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                if (empty || user == null) {
                    setText(null);
                } else {
                    setText(String.format("%s %s - %s - Réservations: %d", 
                        user.getNom(),
                        user.getPrenom(),
                        user.getEmail(),
                        user.getTripsCount()
                    ));
                }
            }
        });
    }
    
    private void filterPassengers(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            listViewPassengers.setItems(passengersList);
            return;
        }
        
        String lowerCaseFilter = searchText.toLowerCase();
        
        FilteredList<User> filteredData = new FilteredList<>(passengersList, passenger -> {
            return passenger.getNom().toLowerCase().contains(lowerCaseFilter) ||
                   passenger.getPrenom().toLowerCase().contains(lowerCaseFilter) ||
                   passenger.getEmail().toLowerCase().contains(lowerCaseFilter);
        });
        
        listViewPassengers.setItems(filteredData);
    }
    
    private void showPassengerDetails(User passenger) {
        tfNom.setText(passenger.getNom());
        tfPrenom.setText(passenger.getPrenom());
        tfTel.setText(passenger.getTel());
        tfEmail.setText(passenger.getEmail());
        lblReservationsCount.setText(String.valueOf(passenger.getTripsCount()));
    }
    
    private void loadPassengers() {
        try {
            // Filter only passengers
            passengersList = FXCollections.observableArrayList(
                serviceUser.readAll().stream()
                    .filter(u -> u.getRoleCode().equals(Role.PASSENGER_CODE))
                    .collect(Collectors.toList())
            );
            listViewPassengers.setItems(passengersList);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Erreur lors du chargement des passagers: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleModifier() {
        if (selectedPassenger == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", 
                "Veuillez sélectionner un passager à modifier");
            return;
        }
        
        if (!validateInputs()) return;
        
        try {
            selectedPassenger.setNom(tfNom.getText());
            selectedPassenger.setPrenom(tfPrenom.getText());
            selectedPassenger.setTel(tfTel.getText());
            selectedPassenger.setEmail(tfEmail.getText());
            
            serviceUser.update(selectedPassenger);
            showAlert(Alert.AlertType.INFORMATION, "Succès", 
                "Passager modifié avec succès!");
            loadPassengers();
            
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Erreur lors de la modification: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleSupprimer() {
        if (selectedPassenger == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", 
                "Veuillez sélectionner un passager à supprimer");
            return;
        }
        
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setContentText("Voulez-vous vraiment supprimer ce passager ?");
        
        if (confirmation.showAndWait().get() == ButtonType.OK) {
            try {
                serviceUser.delete(selectedPassenger);
                showAlert(Alert.AlertType.INFORMATION, "Succès", 
                    "Passager supprimé avec succès!");
                clearFields();
                loadPassengers();
                
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", 
                    "Erreur lors de la suppression: " + e.getMessage());
            }
        }
    }
    
    @FXML
    private void handleEffacer() {
        clearFields();
        selectedPassenger = null;
        listViewPassengers.getSelectionModel().clearSelection();
    }
    
    private void clearFields() {
        tfNom.clear();
        tfPrenom.clear();
        tfTel.clear();
        tfEmail.clear();
        lblReservationsCount.setText("0");
    }
    
    private boolean validateInputs() {
        StringBuilder errors = new StringBuilder();
        
        if (tfNom.getText().isEmpty()) errors.append("Le nom est requis\n");
        if (tfPrenom.getText().isEmpty()) errors.append("Le prénom est requis\n");
        if (tfEmail.getText().isEmpty()) errors.append("L'email est requis\n");
        
        if (errors.length() > 0) {
            showAlert(Alert.AlertType.WARNING, "Validation", errors.toString());
            return false;
        }
        
        return true;
    }
    
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

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
            Stage stage = (Stage) tfNom.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Erreur de navigation: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleManageDrivers() {
        handleNavigation("/Admin/GestionConducteur.fxml");
    }

    @FXML
    private void handleManagePassengers() {
        handleNavigation("/Admin/GestionPassager.fxml");
    }

    @FXML
    private void handleManageAnnouncements() {
        showAlert(Alert.AlertType.INFORMATION, "Info", "Gestion des annonces - À implémenter");
    }

    @FXML
    private void handleManageReservations() {
        showAlert(Alert.AlertType.INFORMATION, "Info", "Gestion des réservations - À implémenter");
    }

    @FXML
    private void handleViewReviews() {
        showAlert(Alert.AlertType.INFORMATION, "Info", "Voir les avis - À implémenter");
    }

    @FXML
    private void handleManageComplaints() {
        showAlert(Alert.AlertType.INFORMATION, "Info", "Gestion des réclamations - À implémenter");
    }

    @FXML
    private void handleLogout() {
        handleNavigation("/Users/LoginUser.fxml");
    }
} 