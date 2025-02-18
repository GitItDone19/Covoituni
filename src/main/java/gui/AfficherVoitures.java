package gui;

import entities.Car;
import entities.Categorie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import services.CarService;
import services.CategorieService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class AfficherVoitures implements Initializable {

    @FXML
    private TableView<Car> tableView;
    @FXML
    private TableColumn<Car, Integer> idCol;
    @FXML
    private TableColumn<Car, String> plaqueCol;
    @FXML
    private TableColumn<Car, String> descriptionCol;
    @FXML
    private TableColumn<Car, String> dateCol;
    @FXML
    private TableColumn<Car, String> couleurCol;
    @FXML
    private TableColumn<Car, String> marqueCol;
    @FXML
    private TableColumn<Car, String> modeleCol;
    @FXML
    private TableColumn<Car, String> categorieCol;

    private final CarService carService = new CarService();
    private final CategorieService categorieService = new CategorieService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configureColumns();
        loadData();
    }

    private void configureColumns() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        plaqueCol.setCellValueFactory(new PropertyValueFactory<>("plaqueImatriculation"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("dateImatriculation"));
        couleurCol.setCellValueFactory(new PropertyValueFactory<>("couleur"));
        marqueCol.setCellValueFactory(new PropertyValueFactory<>("marque"));
        modeleCol.setCellValueFactory(new PropertyValueFactory<>("modele"));
        categorieCol.setCellValueFactory(cellData -> {
            try {
                Categorie categorie = categorieService.findById(cellData.getValue().getCategorieId());
                return new javafx.beans.property.SimpleStringProperty(
                    categorie != null ? categorie.getNom() : ""
                );
            } catch (SQLException e) {
                return new javafx.beans.property.SimpleStringProperty("");
            }
        });
    }

    private void loadData() {
        try {
            List<Car> voitures = carService.readAll();
            ObservableList<Car> observableList = FXCollections.observableList(voitures);
            tableView.setItems(observableList);
        } catch (SQLException e) {
            showError("Erreur lors du chargement des données", e.getMessage());
        }
    }

    @FXML
    private void handleAjouterButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterVoiture.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Ajouter une voiture");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            loadData(); // Refresh data after adding
        } catch (IOException e) {
            showError("Erreur", "Impossible d'ouvrir la fenêtre d'ajout");
        }
    }

    @FXML
    private void handleModifierButton() {
        Car selectedCar = tableView.getSelectionModel().getSelectedItem();
        if (selectedCar == null) {
            showError("Erreur", "Veuillez sélectionner une voiture à modifier");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierVoiture.fxml"));
            Parent root = loader.load();
            ModifierVoiture controller = loader.getController();
            controller.setCar(selectedCar);
            
            Stage stage = new Stage();
            stage.setTitle("Modifier une voiture");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            loadData(); // Refresh data after modification
        } catch (IOException e) {
            showError("Erreur", "Impossible d'ouvrir la fenêtre de modification");
        }
    }

    @FXML
    private void handleSupprimerButton() {
        Car selectedCar = tableView.getSelectionModel().getSelectedItem();
        if (selectedCar == null) {
            showError("Erreur", "Veuillez sélectionner une voiture à supprimer");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText(null);
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cette voiture ?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            try {
                carService.delete(selectedCar);
                loadData(); // Refresh data after deletion
            } catch (SQLException e) {
                showError("Erreur lors de la suppression", e.getMessage());
            }
        }
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 