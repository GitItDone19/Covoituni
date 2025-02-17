package gui.Admin;

import entities.User.Role;
import entities.User.User;
import User.ServiceUser;
import User.ServiceRole;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.application.Platform;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.io.IOException;

public class GestionConducteurController implements Initializable {
    
    @FXML private TextField tfNom;
    @FXML private TextField tfPrenom;
    @FXML private TextField tfTel;
    @FXML private TextField tfEmail;
    @FXML private TextField tfRecherche;
    @FXML private Label lblRating;
    @FXML private Label lblTripsCount;
    
    @FXML private ListView<User> listViewDrivers;
    
    private ServiceUser serviceUser;
    private ObservableList<User> driversList;
    private User selectedDriver;
    
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
            filterDrivers(newValue);
        });

        // Load initial data
        refreshTable();

        // Setup selection listener
        listViewDrivers.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    selectedDriver = newSelection;
                    showDriverDetails(newSelection);
                }
            });

        listViewDrivers.setCellFactory(lv -> new ListCell<User>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                if (empty || user == null) {
                    setText(null);
                } else {
                    setText(String.format("%s %s - %s - Note: %.1f (%d trajets)", 
                        user.getNom(),
                        user.getPrenom(),
                        user.getEmail(),
                        user.getRating(),
                        user.getTripsCount()
                    ));
                }
            }
        });
    }
    
    private void refreshTable() {
        try {
            // Filter only drivers
            driversList = FXCollections.observableArrayList(
                serviceUser.readAll().stream()
                    .filter(u -> u.getRoleCode().equals(Role.DRIVER_CODE))
                    .collect(Collectors.toList())
            );
            listViewDrivers.setItems(driversList);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Erreur lors du chargement des conducteurs: " + e.getMessage());
        }
    }
    
    private void filterDrivers(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            listViewDrivers.setItems(driversList);
            return;
        }
        
        String lowerCaseFilter = searchText.toLowerCase();
        
        FilteredList<User> filteredData = new FilteredList<>(driversList, driver -> {
            return driver.getNom().toLowerCase().contains(lowerCaseFilter) ||
                   driver.getPrenom().toLowerCase().contains(lowerCaseFilter) ||
                   driver.getEmail().toLowerCase().contains(lowerCaseFilter);
        });
        
        listViewDrivers.setItems(filteredData);
    }
    
    private void showDriverDetails(User driver) {
        tfNom.setText(driver.getNom());
        tfPrenom.setText(driver.getPrenom());
        tfTel.setText(driver.getTel());
        tfEmail.setText(driver.getEmail());
        lblRating.setText(String.format("%.1f", driver.getRating()));
        lblTripsCount.setText(String.valueOf(driver.getTripsCount()));
    }
    
    @FXML
    private void handleModifier() {
        if (selectedDriver == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", 
                "Veuillez sélectionner un conducteur à modifier");
            return;
        }
        
        if (!validateInputs()) return;
        
        try {
            selectedDriver.setNom(tfNom.getText());
            selectedDriver.setPrenom(tfPrenom.getText());
            selectedDriver.setTel(tfTel.getText());
            selectedDriver.setEmail(tfEmail.getText());
            
            serviceUser.update(selectedDriver);
            showAlert(Alert.AlertType.INFORMATION, "Succès", 
                "Conducteur modifié avec succès!");
            refreshTable();
            
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Erreur lors de la modification: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleSupprimer() {
        if (selectedDriver == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", 
                "Veuillez sélectionner un conducteur à supprimer");
            return;
        }
        
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setContentText("Voulez-vous vraiment supprimer ce conducteur ?");
        
        if (confirmation.showAndWait().get() == ButtonType.OK) {
            try {
                serviceUser.delete(selectedDriver);
                showAlert(Alert.AlertType.INFORMATION, "Succès", 
                    "Conducteur supprimé avec succès!");
                clearFields();
                refreshTable();
                
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", 
                    "Erreur lors de la suppression: " + e.getMessage());
            }
        }
    }
    
    @FXML
    private void handleEffacer() {
        clearFields();
        selectedDriver = null;
        listViewDrivers.getSelectionModel().clearSelection();
    }
    
    private void clearFields() {
        tfNom.clear();
        tfPrenom.clear();
        tfTel.clear();
        tfEmail.clear();
        lblRating.setText("0.0");
        lblTripsCount.setText("0");
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
            // Use getClass().getResource() to properly locate the FXML
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
            e.printStackTrace(); // Add this to see detailed error
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