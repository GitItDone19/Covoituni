package gui.Users;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import entities.Trajet;
import Services.TrajetService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ListeTrajetController implements Initializable {
    @FXML
    private ListView<Trajet> trajetListView;
    
    private TrajetService trajetService;
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        trajetService = new TrajetService();
        
        // Configuration de la cellule personnalisée
        trajetListView.setCellFactory(lv -> new ListCell<Trajet>() {
            private final Button modifyButton = new Button("Modifier");
            private final Button cancelButton = new Button("Annuler");
            private final HBox buttons = new HBox(10, modifyButton, cancelButton);
            private final HBox content = new HBox(15);
            
            {
                modifyButton.getStyleClass().add("button-secondary");
                cancelButton.getStyleClass().add("button-danger");
                buttons.setStyle("-fx-alignment: center-right;");
                content.setStyle("-fx-alignment: center-left;");
            }

            @Override
            protected void updateItem(Trajet trajet, boolean empty) {
                super.updateItem(trajet, empty);
                
                if (empty || trajet == null) {
                    setGraphic(null);
                } else {
                    VBox details = new VBox(5);
                    Label titleLabel = new Label(trajet.getTitre());
                    titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
                    
                    Label detailsLabel = new Label(
                        String.format("De %s à %s - Départ le %s - Prix: %.2f DT",
                            trajet.getDeparturePoint(),
                            trajet.getArrivalPoint(),
                            trajet.getDepartureDate().format(dateFormatter),
                            trajet.getPrice()
                        )
                    );
                    
                    details.getChildren().addAll(titleLabel, detailsLabel);
                    
                    content.getChildren().clear();
                    content.getChildren().addAll(details, buttons);
                    HBox.setHgrow(details, Priority.ALWAYS);
                    
                    modifyButton.setOnAction(e -> handleModify(trajet));
                    cancelButton.setOnAction(e -> handleCancel(trajet));
                    
                    setGraphic(content);
                }
            }
        });
        
        loadTrajets();
    }

    private void loadTrajets() {
        try {
            trajetListView.getItems().clear();
            trajetListView.getItems().addAll(trajetService.readAll());
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement des trajets: " + e.getMessage());
        }
    }

    @FXML
    private void handleAjoutTrajet() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AjoutTrajet.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Ajouter un trajet");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            
            // Recharger la liste après l'ajout
            loadTrajets();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ouverture du formulaire d'ajout");
        }
    }

    private void handleModify(Trajet trajet) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ModifierTrajet.fxml"));
            Parent root = loader.load();
            
            ModifierTrajetController controller = loader.getController();
            controller.setTrajet(trajet);
            
            Stage stage = new Stage();
            stage.setTitle("Modifier le trajet");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            
            // Recharger la liste après la modification
            loadTrajets();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ouverture du formulaire de modification");
        }
    }

    private void handleCancel(Trajet trajet) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Annuler le trajet");
        alert.setContentText("Voulez-vous vraiment annuler ce trajet ?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    trajetService.delete(trajet);
                    loadTrajets();
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Le trajet a été annulé avec succès");
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'annulation du trajet: " + e.getMessage());
                }
            }
        });
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 