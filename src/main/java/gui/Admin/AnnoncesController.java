package gui.Admin;

import gui.Users.ModifierAnnonceController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Modality;
import javafx.stage.Stage;
import entities.Annonce;
import Services.AnnonceService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AnnoncesController {
    @FXML
    private ListView<Annonce> annonceListView;
    
    private AnnonceService annonceService;
    
    @FXML
    public void initialize() {
        annonceService = new AnnonceService();
        loadAnnonces();
        setupAnnonceListView();
    }
    
    private void setupAnnonceListView() {
        annonceListView.setCellFactory(lv -> new ListCell<Annonce>() {
            @Override
            protected void updateItem(Annonce annonce, boolean empty) {
                super.updateItem(annonce, empty);
                if (empty || annonce == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox container = new HBox(10);
                    setupAnnonceCell(container, annonce);
                    setGraphic(container);
                }
            }
        });
    }

    private void loadAnnonces() {
        try {
            List<Annonce> annonces = annonceService.readAll();
            annonceListView.getItems().clear();
            annonceListView.getItems().addAll(annonces);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                     "Erreur lors du chargement des annonces: " + e.getMessage());
        }
    }

    @FXML
    private void handleModifier(Annonce annonce) {
        try {
            // Charger le FXML de modification d'annonce
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ModifierAnnonce.fxml"));
            Parent root = loader.load();
            
            // Récupérer le contrôleur et initialiser les données
            ModifierAnnonceController controller = loader.getController();
            controller.initData(annonce);
            
            // Créer et configurer la nouvelle fenêtre
            Stage stage = new Stage();
            stage.setTitle("Modifier l'annonce");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            
            // Ajouter un événement à la fermeture de la fenêtre pour rafraîchir la liste
            stage.setOnHidden(e -> loadAnnonces());
            
            stage.show();
            
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                     "Erreur lors de l'ouverture du formulaire de modification: " + e.getMessage());
        }
    }

    private void setupAnnonceCell(HBox container, Annonce annonce) {
        // Informations de l'annonce
        VBox infoContainer = new VBox(5);
        Label titleLabel = new Label(annonce.getTitre());
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        
        Label descLabel = new Label(annonce.getDescription());
        Label seatsLabel = new Label("Places disponibles: " + annonce.getAvailableSeats());
        Label statusLabel = new Label("Status: " + annonce.getStatus());
        infoContainer.getChildren().addAll(titleLabel, descLabel, seatsLabel, statusLabel);
        
        // Conteneur pour les boutons
        HBox buttonsContainer = new HBox(10);
        buttonsContainer.setAlignment(Pos.CENTER_RIGHT);
        
        // Boutons d'action
        Button modifierBtn = new Button("Modifier");
        Button supprimerBtn = new Button("Supprimer");
        Button reserverBtn = new Button("Réserver");
        Button terminerBtn = new Button("Terminer");
        
        modifierBtn.setOnAction(e -> handleModifier(annonce));
        supprimerBtn.setOnAction(e -> handleSupprimer(annonce));
        reserverBtn.setOnAction(e -> handleReserver(annonce));
        terminerBtn.setOnAction(e -> handleTerminer(annonce));
        
        // Styles des boutons
        modifierBtn.getStyleClass().add("button-primary");
        supprimerBtn.getStyleClass().add("button-danger");
        reserverBtn.getStyleClass().add("button-success");
        terminerBtn.getStyleClass().add("button-warning");
        
        // Désactiver les boutons selon le statut
        boolean isTerminated = "TERMINATED".equals(annonce.getStatus());
        boolean isClosed = "CLOSED".equals(annonce.getStatus());
        
        modifierBtn.setDisable(isTerminated || isClosed);
        supprimerBtn.setDisable(isTerminated);
        reserverBtn.setDisable(isTerminated || isClosed);
        terminerBtn.setDisable(isTerminated);
        
        // Ajouter les boutons au conteneur
        buttonsContainer.getChildren().addAll(modifierBtn, supprimerBtn, reserverBtn, terminerBtn);
        
        // Layout
        HBox.setHgrow(infoContainer, Priority.ALWAYS);
        container.getChildren().addAll(infoContainer, buttonsContainer);
        container.setAlignment(Pos.CENTER_LEFT);
        container.setPadding(new Insets(5, 10, 5, 10));
        
        // Style du conteneur
        container.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 5;");
    }

    private void handleSupprimer(Annonce annonce) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer l'annonce");
        alert.setContentText("Voulez-vous vraiment supprimer cette annonce ?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    annonceService.delete(annonce);
                    loadAnnonces();
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", 
                             "Erreur lors de la suppression de l'annonce: " + e.getMessage());
                }
            }
        });
    }

    private void handleReserver(Annonce annonce) {
        // Implémenter la logique de réservation
    }

    private void handleTerminer(Annonce annonce) {
        try {
            annonceService.updateStatus(annonce, "TERMINATED");
            loadAnnonces();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                     "Erreur lors de la terminaison de l'annonce: " + e.getMessage());
        }
    }

    @FXML
    private void handleNouvelleAnnonce() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AjoutAnnonce.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle("Nouvelle Annonce");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            
            stage.setOnHidden(e -> loadAnnonces());
            
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                     "Erreur lors de l'ouverture du formulaire d'ajout: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 