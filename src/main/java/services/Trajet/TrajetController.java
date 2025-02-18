package services.Trajet;

import entities.Trajet.Trajet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.collections.FXCollections;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Contrôleur principal pour la gestion des trajets
 * Gère l'affichage, l'ajout, la modification et la suppression des trajets
 */
public class TrajetController {

    // Constantes pour les couleurs de l'interface
    private static final String BOLT_GREEN = "#34D186";
    private static final String BOLT_DARK = "#2F313F";
    private static final String BOLT_GRAY = "#7A7D8C";
    private static final String BOLT_LIGHT_BG = "#F8F9FA";
    private static final String BOLT_BORDER = "#E9ECEF";

    // Composants de l'interface utilisateur
    @FXML
    private ListView<Trajet> trajetListView;  // Liste des trajets disponibles

    // Services
    private final TrajetService trajetService = new TrajetService();

    /**
     * Initialise le contrôleur et configure la liste des trajets
     * Appelé automatiquement après le chargement du FXML
     */
    @FXML
    public void initialize() {
        loadTrajets();
        configurerListView();
    }

    /**
     * Configure l'affichage personnalisé de la ListView des trajets
     * Définit le style et la mise en page de chaque élément
     */
    private void configurerListView() {
        trajetListView.setCellFactory(lv -> new ListCell<Trajet>() {
            @Override
            protected void updateItem(Trajet trajet, boolean empty) {
                super.updateItem(trajet, empty);
                if (empty || trajet == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Création du conteneur principal
                    VBox container = new VBox(8);
                    container.setPadding(new Insets(16));
                    container.setStyle(String.format(
                        "-fx-background-color: white; " +
                        "-fx-border-color: %s; " +
                        "-fx-border-width: 0 0 1 0; " +
                        "-fx-background-radius: 8;",
                        BOLT_BORDER
                    ));

                    // Informations du trajet
                    Label titreLabel = new Label(trajet.getTitre());
                    titreLabel.setStyle(String.format(
                        "-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: %s;",
                        BOLT_DARK
                    ));

                    // Détails du trajet
                    Label detailsLabel = new Label(String.format(
                        "%s → %s | Places: %d | Prix: %.2f DT",
                        trajet.getDeparturePoint(),
                        trajet.getArrivalPoint(),
                        trajet.getAvailableSeats(),
                        trajet.getPrice()
                    ));
                    detailsLabel.setStyle(String.format(
                        "-fx-text-fill: %s; -fx-font-size: 14;",
                        BOLT_GRAY
                    ));

                    // Boutons d'action
                    HBox buttonsBox = new HBox(10);
                    buttonsBox.setPadding(new Insets(10, 0, 0, 0));

                    // Bouton Modifier
                    Button modifierBtn = createButton("Modifier", BOLT_GREEN);
                    modifierBtn.setOnAction(e -> modifierTrajet(trajet));

                    // Bouton Supprimer
                    Button supprimerBtn = createButton("Supprimer", "#FF4757");
                    supprimerBtn.setOnAction(e -> {
                        // Afficher une boîte de dialogue de confirmation
                        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
                        confirmation.setTitle("Confirmation de suppression");
                        confirmation.setHeaderText("Supprimer le trajet");
                        confirmation.setContentText("Êtes-vous sûr de vouloir supprimer ce trajet ?");

                        if (confirmation.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                            try {
                                // Supprimer le trajet
                                trajetService.delete(trajet.getId());
                                // Recharger la liste
                                loadTrajets();
                                showAlert("Succès", "Trajet supprimé avec succès", Alert.AlertType.INFORMATION);
                            } catch (SQLException ex) {
                                showAlert("Erreur", 
                                        "Erreur lors de la suppression : " + ex.getMessage(), 
                                        Alert.AlertType.ERROR);
                            }
                        }
                    });

                    buttonsBox.getChildren().addAll(modifierBtn, supprimerBtn);

                    container.getChildren().addAll(titreLabel, detailsLabel, buttonsBox);
                    setGraphic(container);
                }
            }
        });
    }

    /**
     * Crée un bouton stylisé pour les actions
     * @param text Texte du bouton
     * @param color Couleur de fond du bouton
     * @return Button stylisé
     */
    private Button createButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle(String.format(
            "-fx-background-color: %s; -fx-text-fill: white; " +
            "-fx-padding: 8 16; -fx-background-radius: 4;",
            color
        ));
        return button;
    }

    /**
     * Charge ou actualise la liste des trajets depuis la base de données
     */
    public void loadTrajets() {
        try {
            List<Trajet> trajets = trajetService.readAll();
            trajetListView.setItems(FXCollections.observableArrayList(trajets));
        } catch (SQLException e) {
            showAlert("Erreur", 
                     "Impossible de charger les trajets : " + e.getMessage(), 
                     Alert.AlertType.ERROR);
        }
    }

    /**
     * Ouvre la fenêtre d'ajout d'un nouveau trajet
     * @param event Événement déclencheur
     */
    @FXML
    void ajouterTrajet(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Trajet/AjouterTrajet.fxml"));
            Parent root = loader.load();
            
            // Récupérer le contrôleur et définir le parent
            AjouterTrajetController controller = loader.getController();
            controller.setParentController(this);
            
            Stage stage = new Stage();
            stage.setTitle("Ajouter un trajet");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            
        } catch (IOException e) {
            showAlert("Erreur", 
                     "Impossible d'ouvrir la fenêtre d'ajout : " + e.getMessage(), 
                     Alert.AlertType.ERROR);
        }
    }

    /**
     * Ouvre la fenêtre de modification d'un trajet
     * @param trajet Trajet à modifier
     */
    private void modifierTrajet(Trajet trajet) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Trajet/ModifierTrajet.fxml"));
            Parent root = loader.load();
            
            ModifierTrajetController controller = loader.getController();
            controller.setTrajet(trajet);
            controller.setParentController(this);
            
            Stage stage = new Stage();
            stage.setTitle("Modifier le trajet");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            showAlert("Erreur", 
                     "Impossible d'ouvrir la fenêtre de modification : " + e.getMessage(), 
                     Alert.AlertType.ERROR);
        }
    }

    /**
     * Affiche une boîte de dialogue d'alerte
     * @param title Titre de l'alerte
     * @param content Message de l'alerte
     * @param type Type d'alerte
     */
    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
