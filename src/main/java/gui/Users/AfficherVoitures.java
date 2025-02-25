package gui.Users;

import entities.Car;
import entities.Categorie;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import Services.CarService;
import Services.CategorieService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class AfficherVoitures implements Initializable {

    @FXML
    private VBox vboxContainer;

    private final CarService carService = new CarService();
    private final CategorieService categorieService = new CategorieService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadData();
    }

    private void loadData() {
        vboxContainer.getChildren().clear();
        try {
            List<Car> voitures = carService.readAll();
            for (Car voiture : voitures) {
                VBox carCard = new VBox();
                carCard.getStyleClass().add("car-card");

                // Car details
                Label plaqueLabel = new Label(voiture.getPlaqueImatriculation());
                plaqueLabel.getStyleClass().add("title");

                Label marqueModeleLabel = new Label(voiture.getMarque() + " " + voiture.getModele());
                marqueModeleLabel.getStyleClass().add("subtitle");

                Label descriptionLabel = new Label(voiture.getDescription());
                descriptionLabel.getStyleClass().add("description");

                // Additional details in a grid
                Label dateLabel = new Label("Date: " + voiture.getDateImatriculation());
                Label couleurLabel = new Label("Couleur: " + voiture.getCouleur());

                // Get category name
                String categorieName = "";
                try {
                    Categorie categorie = categorieService.findById(voiture.getCategorieId());
                    if (categorie != null) {
                        categorieName = categorie.getNom();
                    }
                } catch (SQLException e) {
                    categorieName = "Non définie";
                }
                Label categorieLabel = new Label("Catégorie: " + categorieName);

                // Details container
                VBox detailsBox = new VBox(5);
                detailsBox.getStyleClass().add("details-box");
                detailsBox.getChildren().addAll(dateLabel, couleurLabel, categorieLabel);

                // Buttons container
                HBox buttonBox = new HBox();
                buttonBox.getStyleClass().add("button-box");

                Button editButton = new Button("Modifier");
                editButton.getStyleClass().add("button");
                editButton.setOnAction(e -> handleModifierVoiture(voiture));

                Button deleteButton = new Button("Supprimer");
                deleteButton.getStyleClass().addAll("button", "delete-button");
                deleteButton.setOnAction(e -> handleSupprimerVoiture(voiture));

                buttonBox.getChildren().addAll(editButton, deleteButton);

                // Add all elements to the card
                carCard.getChildren().addAll(
                        plaqueLabel,
                        marqueModeleLabel,
                        descriptionLabel,
                        detailsBox,
                        buttonBox
                );

                vboxContainer.getChildren().add(carCard);
            }
        } catch (SQLException e) {
            showError("Erreur lors du chargement des données", e.getMessage());
        }
    }

    private void handleSupprimerVoiture(Car voiture) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer la voiture");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cette voiture ?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            try {
                carService.delete(voiture);
                loadData();
            } catch (SQLException e) {
                showError("Erreur lors de la suppression", e.getMessage());
            }
        }
    }

    private void handleModifierVoiture(Car voiture) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ModifierVoiture.fxml"));
            Parent root = loader.load();

            ModifierVoiture controller = loader.getController();
            controller.setCar(voiture);

            Stage stage = new Stage();
            stage.setTitle("Modifier une voiture");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            loadData();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Erreur", "Impossible d'ouvrir la fenêtre de modification: " + e.getMessage());
        }
    }

    @FXML
    private void handleAjouterButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("AjouterVoiture.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Ajouter une voiture");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            loadData();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Erreur", "Impossible d'ouvrir la fenêtre d'ajout: " + e.getMessage());
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