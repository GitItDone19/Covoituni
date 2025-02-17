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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class GestionUsersController implements Initializable {
    
    @FXML private TextField tfNom;
    @FXML private TextField tfPrenom;
    @FXML private TextField tfTel;
    @FXML private TextField tfEmail;
    @FXML private ComboBox<String> cbRole;
    @FXML private TextField tfRecherche;
    @FXML private ComboBox<String> cbRechercheType;
    
    @FXML private TableView<User> tableUsers;
    @FXML private TableColumn<User, Integer> colId;
    @FXML private TableColumn<User, String> colNom;
    @FXML private TableColumn<User, String> colPrenom;
    @FXML private TableColumn<User, String> colTel;
    @FXML private TableColumn<User, String> colEmail;
    @FXML private TableColumn<User, String> colRole;
    
    private ServiceUser serviceUser;
    private ServiceRole serviceRole;
    private ObservableList<User> usersList;
    private User selectedUser;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        serviceUser = new ServiceUser();
        serviceRole = new ServiceRole();
        
        try {
            // Setup roles
            ArrayList<Role> roles = serviceRole.readAll();
            ArrayList<String> roleDisplayNames = new ArrayList<>();
            for (Role role : roles) {
                roleDisplayNames.add(role.getDisplayName());
            }
            cbRole.setItems(FXCollections.observableArrayList(roleDisplayNames));
            
            // Setup table columns
            colId.setCellValueFactory(new PropertyValueFactory<>("id"));
            colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
            colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
            colTel.setCellValueFactory(new PropertyValueFactory<>("tel"));
            colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
            colRole.setCellValueFactory(new PropertyValueFactory<>("roleDisplayName"));
            
            // Add CSS classes to columns
            colId.getStyleClass().add("table-column");
            colNom.getStyleClass().add("table-column");
            colPrenom.getStyleClass().add("table-column");
            colTel.getStyleClass().add("table-column");
            colEmail.getStyleClass().add("table-column");
            colRole.getStyleClass().add("table-column");
            
            // Setup search types
            cbRechercheType.setItems(FXCollections.observableArrayList(
                "Nom", "Email", "Role"
            ));
            cbRechercheType.setValue("Nom");
            
            // Setup search listener
            tfRecherche.textProperty().addListener((obs, oldValue, newValue) -> {
                filterUsers(newValue);
            });
            
            // Load initial data
            refreshTable();
            
            // Setup selection listener
            tableUsers.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        selectedUser = newSelection;
                        showUserDetails(newSelection);
                    }
                });
            
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Erreur lors de l'initialisation: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleModifier() {
        if (selectedUser == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", 
                "Veuillez sélectionner un utilisateur à modifier");
            return;
        }
        
        if (!validateInputs()) return;
        
        try {
            Role selectedRole = findRoleByDisplayName(cbRole.getValue());
            if (selectedRole == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Rôle invalide");
                return;
            }
            
            selectedUser.setNom(tfNom.getText());
            selectedUser.setPrenom(tfPrenom.getText());
            selectedUser.setTel(tfTel.getText());
            selectedUser.setEmail(tfEmail.getText());
            selectedUser.setRole(selectedRole);
            
            serviceUser.update(selectedUser);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Utilisateur modifié avec succès!");
            refreshTable();
            
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Erreur lors de la modification: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleSupprimer() {
        if (selectedUser == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", 
                "Veuillez sélectionner un utilisateur à supprimer");
            return;
        }
        
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setContentText("Voulez-vous vraiment supprimer cet utilisateur ?");
        
        if (confirmation.showAndWait().get() == ButtonType.OK) {
            try {
                serviceUser.delete(selectedUser);
                showAlert(Alert.AlertType.INFORMATION, "Succès", 
                    "Utilisateur supprimé avec succès!");
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
        selectedUser = null;
        tableUsers.getSelectionModel().clearSelection();
    }
    
    private void refreshTable() {
        try {
            usersList = FXCollections.observableArrayList(serviceUser.readAll());
            tableUsers.setItems(usersList);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Erreur lors du chargement des utilisateurs: " + e.getMessage());
        }
    }
    
    private void showUserDetails(User user) {
        tfNom.setText(user.getNom());
        tfPrenom.setText(user.getPrenom());
        tfTel.setText(user.getTel());
        tfEmail.setText(user.getEmail());
        cbRole.setValue(user.getRoleDisplayName());
    }
    
    private void clearFields() {
        tfNom.clear();
        tfPrenom.clear();
        tfTel.clear();
        tfEmail.clear();
        cbRole.setValue(null);
    }
    
    private boolean validateInputs() {
        StringBuilder errors = new StringBuilder();
        
        if (tfNom.getText().isEmpty()) errors.append("Le nom est requis\n");
        if (tfPrenom.getText().isEmpty()) errors.append("Le prénom est requis\n");
        if (tfEmail.getText().isEmpty()) errors.append("L'email est requis\n");
        if (cbRole.getValue() == null) errors.append("Le rôle est requis\n");
        
        if (errors.length() > 0) {
            showAlert(Alert.AlertType.WARNING, "Validation", errors.toString());
            return false;
        }
        
        return true;
    }
    
    private void filterUsers(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            tableUsers.setItems(usersList);
            return;
        }
        
        String searchType = cbRechercheType.getValue();
        String lowerCaseFilter = searchText.toLowerCase();
        
        FilteredList<User> filteredData = new FilteredList<>(usersList, user -> {
            switch (searchType) {
                case "Nom":
                    return user.getNom().toLowerCase().contains(lowerCaseFilter) ||
                           user.getPrenom().toLowerCase().contains(lowerCaseFilter);
                case "Email":
                    return user.getEmail().toLowerCase().contains(lowerCaseFilter);
                case "Role":
                    return user.getRoleDisplayName().toLowerCase().contains(lowerCaseFilter);
                default:
                    return true;
            }
        });
        
        tableUsers.setItems(filteredData);
    }
    
    private Role findRoleByDisplayName(String displayName) throws SQLException {
        for (Role role : serviceRole.readAll()) {
            if (role.getDisplayName().equals(displayName)) {
                return role;
            }
        }
        return null;
    }
    
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 