package gui.Users;

import entities.Categorie;
import entities.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import Services.CategorieService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class AfficherCategories implements Initializable {

    @FXML
    private VBox vboxContainer;

    private final CategorieService categorieService = new CategorieService();
    private User currentUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadData();
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        loadData();
    }

    private void loadData() {
        vboxContainer.getChildren().clear();
        try {
            List<Categorie> categories = categorieService.readAll();
            for (Categorie categorie : categories) {
                VBox categoryCard = new VBox(10);
                categoryCard.getStyleClass().add("category-card");

                // Category name as header
                Label nameLabel = new Label(categorie.getNom());
                nameLabel.getStyleClass().add("title");

                // Description
                Label descLabel = new Label(categorie.getDescription());
                descLabel.getStyleClass().add("description");

                // Buttons container
                HBox buttonBox = new HBox(10);
                buttonBox.getStyleClass().add("button-box");

                // Modify and Delete buttons
                Button btnModifier = new Button("Modifier");
                btnModifier.getStyleClass().addAll("button", "submit-button");
                btnModifier.setOnAction(e -> handleModifierCategorie(categorie));

                Button deleteButton = new Button("Supprimer");
                deleteButton.getStyleClass().addAll("button", "delete-button");
                deleteButton.setOnAction(e -> handleSupprimerCategorie(categorie));

                buttonBox.getChildren().addAll(btnModifier, deleteButton);

                categoryCard.getChildren().addAll(nameLabel, descLabel, buttonBox);
                vboxContainer.getChildren().add(categoryCard);
            }
        } catch (SQLException e) {
            showError("Erreur lors du chargement des données", e.getMessage());
        }
    }

    private void handleSupprimerCategorie(Categorie categorie) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer la catégorie");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cette catégorie ?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    categorieService.delete(categorie);
                    loadData();
                } catch (SQLException e) {
                    showError("Erreur de suppression", e.getMessage());
                }
            }
        });
    }

    private void handleModifierCategorie(Categorie categorie) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../ModifierCategorie.fxml"));
            Parent root = loader.load();

            ModifierCategorie controller = loader.getController();
            controller.setCategorie(categorie);

            Stage stage = new Stage();
            stage.setTitle("Modifier la catégorie");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            loadData();
        } catch (IOException e) {
            showError("Erreur", "Impossible d'ouvrir la fenêtre de modification: " + e.getMessage());
        }
    }

    @FXML
    private void handleAjouterButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../AjouterCategorie.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Ajouter une catégorie");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            loadData();
        } catch (IOException e) {
            showError("Erreur", "Impossible d'ouvrir la fenêtre d'ajout: " + e.getMessage());
        }
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
