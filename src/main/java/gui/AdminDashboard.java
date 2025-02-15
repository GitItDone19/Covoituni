package gui;

import entities.Reclamation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import Services.ReclamationService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class AdminDashboard implements Initializable {
    @FXML
    private ListView<Reclamation> listView;
    @FXML
    private ComboBox<String> filterStatus;
    @FXML
    private Label totalLabel;
    @FXML
    private Label pendingLabel;
    @FXML
    private Label inProgressLabel;
    @FXML
    private Label resolvedLabel;

    private final ReclamationService rs = new ReclamationService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize filter options
        filterStatus.getItems().addAll("Tous", "EN_COURS", "RESOLUE");
        filterStatus.setValue("Tous");
        filterStatus.setOnAction(e -> refreshList());

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
                    
                    Button detailsBtn = new Button("Détails");
                    detailsBtn.getStyleClass().add("details-button");
                    detailsBtn.setOnAction(event -> openReclamationDetails(reclamation));

                    actionsBox.getChildren().addAll(statusLabel, detailsBtn);

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
            List<Reclamation> allReclamations = rs.readAll();
            ObservableList<Reclamation> filteredList = FXCollections.observableArrayList();
            
            int total = 0, inProgress = 0, resolved = 0;
            
            for (Reclamation r : allReclamations) {
                total++;
                switch (r.getStatus()) {
                    case "EN_COURS": inProgress++; break;
                    case "RESOLUE": resolved++; break;
                }
                
                if (filterStatus.getValue().equals("Tous") || 
                    filterStatus.getValue().equals(r.getStatus())) {
                    filteredList.add(r);
                }
            }
            
            // Update statistics
            totalLabel.setText(String.valueOf(total));
            inProgressLabel.setText(String.valueOf(inProgress));
            resolvedLabel.setText(String.valueOf(resolved));
            
            listView.setItems(filteredList);
            
        } catch (SQLException e) {
            showError(e.getMessage());
        }
    }

    private void openReclamationDetails(Reclamation reclamation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ReclamationDetails.fxml"));
            Parent root = loader.load();
            
            ReclamationDetails controller = loader.getController();
            controller.setReclamation(reclamation);
            
            listView.getScene().setRoot(root);
        } catch (IOException e) {
            showError("Erreur lors de l'ouverture des détails: " + e.getMessage());
        }
    }

    @FXML
    void gererAvis(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/VoirAvis.fxml"));
            Parent root = loader.load();
            listView.getScene().setRoot(root);
        } catch (IOException e) {
            showError("Erreur lors du chargement de la page: " + e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setContentText(message);
        alert.showAndWait();
    }
} 