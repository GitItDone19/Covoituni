package gui;

import entities.Role;
import entities.User;
import Services.ServiceUser;
import Services.ServiceRole;
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
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GestionUsersController implements Initializable {
    
    @FXML private TextField tfNom;
    @FXML private TextField tfPrenom;
    @FXML private TextField tfTel;
    @FXML private TextField tfEmail;
    @FXML private PasswordField tfMdp;
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
            ArrayList<Role> roles = serviceRole.afficherAll();
            ArrayList<String> roleDisplayNames = new ArrayList<>();
            for (Role role : roles) {
                roleDisplayNames.add(role.getDisplayName());
            }
            cbRole.setItems(FXCollections.observableArrayList(roleDisplayNames));
            
            // Initialize table columns
            colId.setCellValueFactory(new PropertyValueFactory<>("id"));
            colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
            colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
            colTel.setCellValueFactory(new PropertyValueFactory<>("tel"));
            colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
            colRole.setCellValueFactory(new PropertyValueFactory<>("roleDisplayName"));
            
            refreshTable();
            
            // Setup table selection listener
            tableUsers.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    selectedUser = newSelection;
                    showUserDetails(newSelection);
                }
            });
            
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Erreur lors de l'initialisation: " + e.getMessage());
        }
        
        // Initialize search type combo box
        cbRechercheType.setItems(FXCollections.observableArrayList(
            "Nom", "Email", "Role"
        ));
        
        // Initialize table columns with proper widths
        colId.setPrefWidth(50);
        colNom.setPrefWidth(100);
        colPrenom.setPrefWidth(100);
        colTel.setPrefWidth(100);
        colEmail.setPrefWidth(150);
        colRole.setPrefWidth(100);
        
        // Setup search functionality
        tfRecherche.textProperty().addListener((obs, oldValue, newValue) -> {
            filterUsers(newValue);
        });
        
        // Add menu bar for logout
        Menu fileMenu = new Menu("File");
        MenuItem logoutItem = new MenuItem("Déconnexion");
        logoutItem.setOnAction(e -> handleLogout());
        fileMenu.getItems().add(logoutItem);
        menuBar.getMenus().add(fileMenu);
    }
    
    @FXML
    private void handleAjouter() {
        if (!validateInputs()) return;
        
        try {
            // Find role by display name
            String selectedDisplayName = cbRole.getValue();
            Role selectedRole = null;
            for (Role role : serviceRole.afficherAll()) {
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
                0, // ID will be generated by database
                tfNom.getText(),
                tfPrenom.getText(),
                tfTel.getText(),
                tfEmail.getText(),
                tfMdp.getText(),
                selectedRole,  // Pass the Role object instead of code
                generateVerificationCode()
            );
            
            serviceUser.ajouter(newUser);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Utilisateur ajouté avec succès!");
            clearFields();
            refreshTable();
            
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout: " + e.getMessage());
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
            // Find role by display name
            String selectedDisplayName = cbRole.getValue();
            Role selectedRole = null;
            for (Role role : serviceRole.afficherAll()) {
                if (role.getDisplayName().equals(selectedDisplayName)) {
                    selectedRole = role;
                    break;
                }
            }
            
            if (selectedRole == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Rôle invalide");
                return;
            }
            
            selectedUser.setNom(tfNom.getText());
            selectedUser.setPrenom(tfPrenom.getText());
            selectedUser.setTel(tfTel.getText());
            selectedUser.setEmail(tfEmail.getText());
            selectedUser.setMdp(tfMdp.getText());
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
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez sélectionner un utilisateur à supprimer");
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
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la suppression: " + e.getMessage());
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
            usersList = FXCollections.observableArrayList(serviceUser.afficherAll());
            tableUsers.setItems(usersList);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement des utilisateurs: " + e.getMessage());
        }
    }
    
    private void showUserDetails(User user) {
        tfNom.setText(user.getNom());
        tfPrenom.setText(user.getPrenom());
        tfTel.setText(user.getTel());
        tfEmail.setText(user.getEmail());
        tfMdp.setText(user.getMdp());
        cbRole.setValue(user.getRoleDisplayName());
    }
    
    private void clearFields() {
        tfNom.clear();
        tfPrenom.clear();
        tfTel.clear();
        tfEmail.clear();
        tfMdp.clear();
        cbRole.setValue(null);
    }
    
    private boolean validateInputs() {
        StringBuilder errors = new StringBuilder();
        
        if (tfNom.getText().isEmpty()) errors.append("Le nom est requis\n");
        if (tfPrenom.getText().isEmpty()) errors.append("Le prénom est requis\n");
        if (tfEmail.getText().isEmpty()) errors.append("L'email est requis\n");
        if (tfMdp.getText().isEmpty()) errors.append("Le mot de passe est requis\n");
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
        
        FilteredList<User> filteredData = new FilteredList<>(usersList, user -> {
            String searchType = cbRechercheType.getValue();
            String lowerCaseFilter = searchText.toLowerCase();
            
            if (searchType == null || searchType.equals("Nom")) {
                return user.getNom().toLowerCase().contains(lowerCaseFilter) ||
                       user.getPrenom().toLowerCase().contains(lowerCaseFilter);
            } else if (searchType.equals("Email")) {
                return user.getEmail().toLowerCase().contains(lowerCaseFilter);
            } else if (searchType.equals("Role")) {
                return user.getRoleDisplayName().toLowerCase().contains(lowerCaseFilter);
            }
            return true;
        });
        
        tableUsers.setItems(filteredData);
    }
    
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    private String generateVerificationCode() {
        // Generate a random 6-character verification code
        return String.format("%06d", (int)(Math.random() * 1000000));
    }
    
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginUser.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) menuBar.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Connexion");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Erreur lors de la déconnexion: " + e.getMessage());
        }
    }
} 