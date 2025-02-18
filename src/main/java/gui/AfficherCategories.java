package gui;

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
import services.CategorieService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class AfficherCategories implements Initializable {

    @FXML
    private TableView<Categorie> tableView;
    @FXML
    private TableColumn<Categorie, Integer> idCol;
    @FXML
    private TableColumn<Categorie, String> nomCol;
    @FXML
    private TableColumn<Categorie, String> descriptionCol;

    private final CategorieService categorieService = new CategorieService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configureColumns();
        loadData();
    }

    private void configureColumns() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
    }

    private void loadData() {
        try {
            List<Categorie> categories = categorieService.readAll();
            ObservableList<Categorie> observableList = FXCollections.observableList(categories);
            tableView.setItems(observableList);
        } catch (SQLException e) {
            showError("Erreur lors du chargement des données", e.getMessage());
        }
    }

    @FXML
    private void handleAjouterButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterCategorie.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Ajouter une catégorie");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            loadData(); // Refresh data after adding
        } catch (IOException e) {
            showError("Erreur", "Impossible d'ouvrir la fenêtre d'ajout");
        }
    }

    @FXML
    private void handleModifierButton() {
        Categorie selectedCategorie = tableView.getSelectionModel().getSelectedItem();
        if (selectedCategorie == null) {
            showError("Erreur", "Veuillez sélectionner une catégorie à modifier");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierCategorie.fxml"));
            Parent root = loader.load();
            ModifierCategorie controller = loader.getController();
            controller.setCategorie(selectedCategorie);
            
            Stage stage = new Stage();
            stage.setTitle("Modifier une catégorie");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            loadData(); // Refresh data after modification
        } catch (IOException e) {
            showError("Erreur", "Impossible d'ouvrir la fenêtre de modification");
        }
    }

    @FXML
    private void handleSupprimerButton() {
        Categorie selectedCategorie = tableView.getSelectionModel().getSelectedItem();
        if (selectedCategorie == null) {
            showError("Erreur", "Veuillez sélectionner une catégorie à supprimer");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText(null);
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cette catégorie ? Cela supprimera également toutes les voitures associées.");

        if (alert.showAndWait().get() == ButtonType.OK) {
            try {
                categorieService.delete(selectedCategorie);
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