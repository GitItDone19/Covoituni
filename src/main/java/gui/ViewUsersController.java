package gui;

import entities.User;
import Services.ServiceUser;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ViewUsersController implements Initializable {
    
    @FXML private TableView<User> tableUsers;
    @FXML private TableColumn<User, String> colUsername;
    @FXML private TableColumn<User, String> colNom;
    @FXML private TableColumn<User, String> colPrenom;
    @FXML private TableColumn<User, String> colEmail;
    @FXML private TableColumn<User, String> colRole;
    @FXML private TextField tfSearch;
    @FXML private ComboBox<String> cbSearchType;
    
    private ServiceUser serviceUser;
    private FilteredList<User> filteredUsers;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        serviceUser = new ServiceUser();
        
        // Initialize table columns
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("roleCode"));
        
        // Initialize search type combo box
        cbSearchType.setItems(FXCollections.observableArrayList(
            "Nom d'utilisateur", "Nom", "Email", "Role"
        ));
        cbSearchType.setValue("Nom d'utilisateur");
        
        // Load users
        refreshTable();
        
        // Setup search functionality
        tfSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            updateFilter();
        });
        
        cbSearchType.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateFilter();
        });
    }
    
    private void refreshTable() {
        try {
            filteredUsers = new FilteredList<>(FXCollections.observableArrayList(serviceUser.readAll()));
            tableUsers.setItems(filteredUsers);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Erreur lors du chargement des utilisateurs: " + e.getMessage());
        }
    }
    
    private void updateFilter() {
        String searchText = tfSearch.getText().toLowerCase();
        String searchType = cbSearchType.getValue();
        
        filteredUsers.setPredicate(user -> {
            if (searchText == null || searchText.isEmpty()) {
                return true;
            }
            
            switch (searchType) {
                case "Nom d'utilisateur":
                    return user.getUsername().toLowerCase().contains(searchText);
                case "Nom":
                    return user.getNom().toLowerCase().contains(searchText);
                case "Email":
                    return user.getEmail().toLowerCase().contains(searchText);
                case "Role":
                    return user.getRoleCode().toLowerCase().contains(searchText);
                default:
                    return true;
            }
        });
    }
    
    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminDashboard.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) tableUsers.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Erreur de navigation: " + e.getMessage());
        }
    }
    
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 