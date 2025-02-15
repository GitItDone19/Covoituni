package gui;

import entities.Role;
import entities.User;
import Services.ServiceUser;
import Services.ServiceRole;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GestionUsersController implements Initializable {
    
    @FXML private TextField tfUsername;
    @FXML private TextField tfNom;
    @FXML private TextField tfPrenom;
    @FXML private TextField tfTel;
    @FXML private TextField tfEmail;
    @FXML private PasswordField tfMdp;
    @FXML private ComboBox<String> cbRole;
    @FXML private TextField tfRecherche;
    @FXML private ComboBox<String> cbRechercheType;
    
    @FXML private TableView<User> tableUsers;
    @FXML private TableColumn<User, String> colUsername;
    @FXML private TableColumn<User, String> colNom;
    @FXML private TableColumn<User, String> colPrenom;
    @FXML private TableColumn<User, String> colTel;
    @FXML private TableColumn<User, String> colEmail;
    @FXML private TableColumn<User, String> colRole;
    
    @FXML private MenuBar menuBar;
    
    private ServiceUser serviceUser;
    private ServiceRole serviceRole;
    private ObservableList<User> usersList;
    private User selectedUser;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        serviceUser = new ServiceUser();
        serviceRole = new ServiceRole();
        
        try {
            // Get roles from database
            ArrayList<Role> roles = serviceRole.readAll();
            ArrayList<String> roleDisplayNames = new ArrayList<>();
            for (Role role : roles) {
                roleDisplayNames.add(role.getDisplayName());
            }
            cbRole.setItems(FXCollections.observableArrayList(roleDisplayNames));
            
            // Initialize table columns
            colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
            colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
            colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
            colTel.setCellValueFactory(new PropertyValueFactory<>("tel"));
            colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
            colRole.setCellValueFactory(new PropertyValueFactory<>("roleCode"));
            
            refreshTable();
            
            // Setup table selection listener
            tableUsers.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    selectedUser = newSelection;
                    showUserDetails(newSelection);
                }
            });
            
            // Initialize search type combo box
            cbRechercheType.setItems(FXCollections.observableArrayList(
                "Nom d'utilisateur", "Nom", "Email", "Role"
            ));
            cbRechercheType.setValue("Nom d'utilisateur");
            
            // Setup search functionality
            tfRecherche.textProperty().addListener((obs, oldValue, newValue) -> {
                filterUsers(newValue);
            });
            
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Erreur lors de l'initialisation: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleAjouter() {
        if (!validateInputs()) return;
        
        try {
            // Check if username already exists
            if (serviceUser.readAll().stream()
                    .anyMatch(u -> u.getUsername().equals(tfUsername.getText()))) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Ce nom d'utilisateur existe déjà");
                return;
            }
            
            // Check if email already exists
            if (serviceUser.readAll().stream()
                    .anyMatch(u -> u.getEmail().equals(tfEmail.getText()))) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Cet email existe déjà");
                return;
            }
            
            // Find role by display name
            String selectedDisplayName = cbRole.getValue();
            Role selectedRole = null;
            for (Role role : serviceRole.readAll()) {
                if (role.getDisplayName().equals(selectedDisplayName)) {
                    selectedRole = role;
                    break;
                }
            }
            
            if (selectedRole == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Rôle invalide");
                return;
            }
            
            User newUser = new User(
                tfUsername.getText(),    // username
                tfNom.getText(),         // nom
                tfPrenom.getText(),      // prenom
                tfTel.getText(),         // tel
                tfEmail.getText(),       // email
                tfMdp.getText(),         // mdp
                selectedRole.getCode(),  // roleCode
                generateVerificationCode() // verificationcode
            );
            
            serviceUser.create(newUser);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Utilisateur ajouté avec succès!");
            clearFields();
            refreshTable();
            
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Erreur lors de l'ajout: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleModifier() {
        if (selectedUser == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez sélectionner un utilisateur");
            return;
        }
        
        if (!validateInputs()) return;
        
        try {
            // Find role by display name
            String selectedDisplayName = cbRole.getValue();
            Role selectedRole = null;
            for (Role role : serviceRole.readAll()) {
                if (role.getDisplayName().equals(selectedDisplayName)) {
                    selectedRole = role;
                    break;
                }
            }
            
            if (selectedRole == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Rôle invalide");
                return;
            }
            
            selectedUser.setUsername(tfUsername.getText());
            selectedUser.setNom(tfNom.getText());
            selectedUser.setPrenom(tfPrenom.getText());
            selectedUser.setTel(tfTel.getText());
            selectedUser.setEmail(tfEmail.getText());
            selectedUser.setMdp(tfMdp.getText());
            selectedUser.setRoleCode(selectedRole.getCode());
            
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
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez sélectionner un utilisateur");
            return;
        }
        
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setContentText("Voulez-vous vraiment supprimer cet utilisateur ?");
        
        if (confirmation.showAndWait().get() == ButtonType.OK) {
            try {
                serviceUser.delete(selectedUser);
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Utilisateur supprimé avec succès!");
                clearFields();
                refreshTable();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", 
                    "Erreur lors de la suppression: " + e.getMessage());
            }
        }
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
        tfUsername.setText(user.getUsername());
        tfNom.setText(user.getNom());
        tfPrenom.setText(user.getPrenom());
        tfTel.setText(user.getTel());
        tfEmail.setText(user.getEmail());
        tfMdp.setText(user.getMdp());
        
        // Find and set role display name
        try {
            for (Role role : serviceRole.readAll()) {
                if (role.getCode().equals(user.getRoleCode())) {
                    cbRole.setValue(role.getDisplayName());
                    break;
                }
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Erreur lors du chargement du rôle: " + e.getMessage());
        }
    }
    
    private void clearFields() {
        tfUsername.clear();
        tfNom.clear();
        tfPrenom.clear();
        tfTel.clear();
        tfEmail.clear();
        tfMdp.clear();
        cbRole.setValue(null);
        selectedUser = null;
        tableUsers.getSelectionModel().clearSelection();
    }
    
    private void filterUsers(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            tableUsers.setItems(usersList);
            return;
        }
        
        FilteredList<User> filteredData = new FilteredList<>(usersList, user -> {
            String searchType = cbRechercheType.getValue();
            String lowerCaseFilter = searchText.toLowerCase();
            
            switch (searchType) {
                case "Nom d'utilisateur":
                    return user.getUsername().toLowerCase().contains(lowerCaseFilter);
                case "Nom":
                    return user.getNom().toLowerCase().contains(lowerCaseFilter) ||
                           user.getPrenom().toLowerCase().contains(lowerCaseFilter);
                case "Email":
                    return user.getEmail().toLowerCase().contains(lowerCaseFilter);
                case "Role":
                    return user.getRoleCode().toLowerCase().contains(lowerCaseFilter);
                default:
                    return true;
            }
        });
        
        tableUsers.setItems(filteredData);
    }
    
    private boolean validateInputs() {
        StringBuilder errors = new StringBuilder();
        
        if (tfUsername.getText().isEmpty()) errors.append("Le nom d'utilisateur est requis\n");
        if (tfNom.getText().isEmpty()) errors.append("Le nom est requis\n");
        if (tfPrenom.getText().isEmpty()) errors.append("Le prénom est requis\n");
        if (tfTel.getText().isEmpty()) errors.append("Le téléphone est requis\n");
        if (tfEmail.getText().isEmpty()) errors.append("L'email est requis\n");
        if (tfMdp.getText().isEmpty()) errors.append("Le mot de passe est requis\n");
        if (cbRole.getValue() == null) errors.append("Le rôle est requis\n");
        
        // Validate email format
        if (!tfEmail.getText().isEmpty() && !tfEmail.getText().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            errors.append("Format d'email invalide\n");
        }
        
        // Validate username format
        if (!tfUsername.getText().isEmpty() && !tfUsername.getText().matches("^[a-zA-Z0-9_]+$")) {
            errors.append("Le nom d'utilisateur ne peut contenir que des lettres, chiffres et underscore\n");
        }
        
        // Validate phone number format
        if (!tfTel.getText().isEmpty() && !tfTel.getText().matches("^[0-9]{8}$")) {
            errors.append("Le numéro de téléphone doit contenir 8 chiffres\n");
        }
        
        if (errors.length() > 0) {
            showAlert(Alert.AlertType.WARNING, "Validation", errors.toString());
            return false;
        }
        
        return true;
    }
    
    private String generateVerificationCode() {
        return String.format("%06d", (int)(Math.random() * 1000000));
    }
    
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginUser.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) menuBar.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Erreur lors de la déconnexion: " + e.getMessage());
        }
    }
} 