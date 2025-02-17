package gui.Reclamation;

import entities.Reclamation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import Services.Reclamation.ReclamationService;
import javafx.geometry.Pos;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;

public class AfficherReclamations implements Initializable {

    @FXML
    private ListView<VBox> listView;
    
    private final ReclamationService reclamationService = new ReclamationService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadReclamations();
    }

    private void loadReclamations() {
        try {
            List<Reclamation> reclamations = reclamationService.readAll();
            listView.getItems().clear();
            
            for (Reclamation reclamation : reclamations) {
                VBox card = createReclamationCard(reclamation);
                listView.getItems().add(card);
            }
        } catch (SQLException e) {
            showError("Erreur lors du chargement des réclamations: " + e.getMessage());
        }
    }

    private VBox createReclamationCard(Reclamation reclamation) {
        VBox card = new VBox(10);
        card.getStyleClass().add("reclamation-card");
        card.setStyle("-fx-background-color: #2D2D2D; -fx-padding: 15; -fx-background-radius: 5;");

        // ID et Date
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        
        Label idLabel = new Label("#" + reclamation.getId());
        idLabel.setStyle("-fx-text-fill: #00FF00;");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Label dateLabel = new Label(sdf.format(reclamation.getDate()));
        dateLabel.setStyle("-fx-text-fill: #AAAAAA;");
        
        header.getChildren().addAll(idLabel, spacer, dateLabel);

        // Description
        Label descLabel = new Label(reclamation.getDescription());
        descLabel.setWrapText(true);
        descLabel.setStyle("-fx-text-fill: white;");

        // Status avec couleur
        Label statusLabel = new Label("Status: " + reclamation.getStatus());
        statusLabel.setStyle("-fx-text-fill: #00FF00; -fx-padding: 5 10; -fx-background-radius: 3;");

        // Boutons d'action
        HBox actions = new HBox(10);
        actions.setAlignment(Pos.CENTER_RIGHT);

        Button editBtn = new Button("Modifier");
        editBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        editBtn.setOnAction(e -> modifierReclamation(reclamation));

        Button deleteBtn = new Button("Supprimer");
        deleteBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        deleteBtn.setOnAction(e -> supprimerReclamation(reclamation));

        actions.getChildren().addAll(editBtn, deleteBtn);

        card.getChildren().addAll(header, descLabel, statusLabel, actions);
        return card;
    }

    private void modifierReclamation(Reclamation reclamation) {
        try {
            // Créer une boîte de dialogue personnalisée
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Modifier la réclamation");
            dialog.setHeaderText("Modification de la réclamation #" + reclamation.getId());

            // Créer le contenu
            VBox content = new VBox(10);
            content.setStyle("-fx-background-color: #2D2D2D; -fx-padding: 20;");

            Label descLabel = new Label("Description:");
            descLabel.setStyle("-fx-text-fill: white;");
            
            TextArea descArea = new TextArea(reclamation.getDescription());
            descArea.setWrapText(true);
            descArea.setPrefRowCount(4);
            descArea.setStyle("-fx-control-inner-background: #3D3D3D; -fx-text-fill: white;");
            
            content.getChildren().addAll(descLabel, descArea);

            // Configurer la boîte de dialogue
            DialogPane dialogPane = dialog.getDialogPane();
            dialogPane.setContent(content);
            dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            dialogPane.setStyle("-fx-background-color: #2D2D2D;");

            // Gérer la réponse
            dialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    String newDescription = descArea.getText().trim();
                    if (newDescription.isEmpty()) {
                        showError("La description ne peut pas être vide");
                        return;
                    }

                    try {
                        reclamation.setDescription(newDescription);
                        reclamationService.update(reclamation);
                        
                        // Recharger immédiatement la liste pour afficher les modifications
                        loadReclamations();
                        
                        // Afficher un message de succès
                        showSuccess("Réclamation modifiée avec succès");
                    } catch (SQLException ex) {
                        showError("Erreur lors de la modification: " + ex.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            showError("Erreur: " + e.getMessage());
        }
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        // Appliquer le style au DialogPane
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: #2D2D2D;");
        dialogPane.lookup(".content.label").setStyle("-fx-text-fill: white;");
        
        // Styliser les boutons
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        okButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        
        alert.showAndWait();
    }

    private void supprimerReclamation(Reclamation reclamation) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Voulez-vous vraiment supprimer cette réclamation ?");
        
        // Appliquer le style au DialogPane
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: #2D2D2D;");
        dialogPane.lookup(".content.label").setStyle("-fx-text-fill: white;");
        
        // Styliser les boutons
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        Button cancelButton = (Button) dialogPane.lookupButton(ButtonType.CANCEL);
        okButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        cancelButton.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white;");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    reclamationService.delete(reclamation);
                    loadReclamations();
                    showSuccess("Réclamation supprimée avec succès");
                } catch (SQLException e) {
                    showError("Erreur lors de la suppression: " + e.getMessage());
                }
            }
        });
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        // Appliquer le style au DialogPane
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: #2D2D2D;");
        dialogPane.lookup(".content.label").setStyle("-fx-text-fill: white;");
        
        // Styliser les boutons
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        okButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        
        alert.showAndWait();
    }

    @FXML
    void retourAjout(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/User/AjouterReclamation.fxml"));
            listView.getScene().setRoot(root);
        } catch (IOException e) {
            showError("Erreur lors du chargement de la page: " + e.getMessage());
        }
    }

    @FXML
    void retourDashboard(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/UserDashbord.fxml"));
            listView.getScene().setRoot(root);
        } catch (IOException e) {
            showError("Erreur lors du chargement de la page: " + e.getMessage());
        }
    }
} 