package gui.Users;

import entities.User.User;
import User.ServiceUser;
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
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ViewUsersController implements Initializable {
    
    @FXML private TextField tfRecherche;
    @FXML private ComboBox<String> cbRechercheType;
    @FXML private TableView<User> tableUsers;
    @FXML private TableColumn<User, Integer> colId;
    @FXML private TableColumn<User, String> colNom;
    @FXML private TableColumn<User, String> colPrenom;
    @FXML private TableColumn<User, String> colTel;
    @FXML private TableColumn<User, String> colEmail;
    @FXML private TableColumn<User, String> colRole;
    @FXML private TableColumn<User, Void> colActions;
    
    private ServiceUser serviceUser;
    private ObservableList<User> usersList;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        serviceUser = new ServiceUser();
        
        // Initialize search types
        cbRechercheType.setItems(FXCollections.observableArrayList("Nom", "Email", "Role"));
        
        // Initialize table columns
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colTel.setCellValueFactory(new PropertyValueFactory<>("tel"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        
        // Add action buttons to each row
        setupActionButtons();
        
        // Load users
        refreshTable();
        
        // Setup search functionality
        tfRecherche.textProperty().addListener((obs, oldValue, newValue) -> {
            filterUsers(newValue);
        });
    }
    
    private void setupActionButtons() {
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");
            private final HBox buttons = new HBox(5, editButton, deleteButton);
            
            {
                editButton.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    handleEdit(user);
                });
                
                deleteButton.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    handleDelete(user);
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buttons);
                }
            }
        });
    }
    
    @FXML
    private void handleAjouterNouveau() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterUser.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) tfRecherche.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur de navigation: " + e.getMessage());
        }
    }
    
    private void handleEdit(User user) {
        // Implement edit functionality
        showAlert(Alert.AlertType.INFORMATION, "Info", "Modification de l'utilisateur " + user.getNom());
    }
    
    private void handleDelete(User user) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setContentText("Voulez-vous vraiment supprimer cet utilisateur ?");
        
        if (confirmation.showAndWait().get() == ButtonType.OK) {
            try {
                serviceUser.delete(user);
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Utilisateur supprimé avec succès!");
                refreshTable();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la suppression: " + e.getMessage());
            }
        }
    }
    
    private void refreshTable() {
        try {
            usersList = FXCollections.observableArrayList(serviceUser.readAll());
            tableUsers.setItems(usersList);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement des utilisateurs: " + e.getMessage());
        }
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
} 