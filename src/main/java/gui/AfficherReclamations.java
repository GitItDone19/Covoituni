package gui;

import entities.Reclamation;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import Services.ReclamationService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AfficherReclamations {

    private final ReclamationService rs = new ReclamationService();

    @FXML
    private ListView<Reclamation> listView;

    @FXML
    void initialize() {
        // Configure ListView cell factory
        listView.setCellFactory(param -> new ListCell<Reclamation>() {
            @Override
            protected void updateItem(Reclamation reclamation, boolean empty) {
                super.updateItem(reclamation, empty);
                if (empty || reclamation == null) {
                    setGraphic(null);
                } else {
                    // Create card for each reclamation
                    VBox card = new VBox(10);
                    card.getStyleClass().add("reclamation-card");
                    
                    // Header with ID and Date
                    HBox header = new HBox(10);
                    Label idLabel = new Label("#" + reclamation.getId());
                    idLabel.getStyleClass().add("card-id");
                    Label dateLabel = new Label(reclamation.getDateReclamation().toString());
                    dateLabel.getStyleClass().add("card-date");
                    Region spacer = new Region();
                    HBox.setHgrow(spacer, Priority.ALWAYS);
                    header.getChildren().addAll(idLabel, spacer, dateLabel);
                    
                    // Description
                    Label descLabel = new Label(reclamation.getDescription());
                    descLabel.getStyleClass().add("card-description");
                    descLabel.setWrapText(true);
                    
                    // Status and Actions
                    HBox actionsBox = new HBox(10);
                    actionsBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
                    
                    Label statusLabel = new Label("Status: " + reclamation.getStatus());
                    statusLabel.getStyleClass().add("status-field");
                    
                    Button editBtn = new Button("✏");
                    Button deleteBtn = new Button("🗑");
                    editBtn.getStyleClass().add("edit-button");
                    deleteBtn.getStyleClass().add("delete-button");
                    
                    editBtn.setOnAction(event -> {
                        // Edit logic here
                    });
                    
                    deleteBtn.setOnAction(event -> {
                        try {
                            rs.delete(reclamation);
                            refreshList();
                        } catch (SQLException e) {
                            showError(e.getMessage());
                        }
                    });
                    
                    actionsBox.getChildren().addAll(statusLabel, editBtn, deleteBtn);
                    
                    // Assemble card
                    card.getChildren().addAll(header, descLabel, actionsBox);
                    setGraphic(card);
                }
            }
        });

        refreshList();
    }

    private void refreshList() {
        try {
            List<Reclamation> reclamations = rs.readAll();
            listView.setItems(FXCollections.observableArrayList(reclamations));
        } catch (SQLException e) {
            showError(e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void retourAjout(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AjouterReclamation.fxml"));
            listView.getScene().setRoot(root);
        } catch (IOException e) {
            showError("Erreur lors du chargement de la page: " + e.getMessage());
        }
    }
} 