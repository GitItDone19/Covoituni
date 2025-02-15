package gui;

import entities.Reclamation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import Services.ReclamationService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ReclamationAdmin {

    private final ReclamationService rs = new ReclamationService();

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

    @FXML
    void initialize() {
        // Configuration du filtre
        filterStatus.getItems().addAll("Tous", "EN_ATTENTE", "EN_COURS", "RESOLUE");
        filterStatus.setValue("Tous");
        filterStatus.setOnAction(e -> refreshList());

        // Configuration de la ListView
        listView.setCellFactory(param -> new ListCell<Reclamation>() {
            @Override
            protected void updateItem(Reclamation reclamation, boolean empty) {
                super.updateItem(reclamation, empty);
                if (empty || reclamation == null) {
                    setGraphic(null);
                } else {
                    // Création de la carte pour chaque réclamation
                    VBox card = new VBox(10);
                    card.getStyleClass().add("reclamation-card");
                    
                    // En-tête de la carte
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
                    
                    // Zone de statut
                    HBox statusBox = new HBox(10);
                    statusBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
                    
                    ComboBox<String> statusCombo = new ComboBox<>();
                    statusCombo.getItems().addAll("EN_ATTENTE", "EN_COURS", "RESOLUE");
                    statusCombo.setValue(reclamation.getStatus());
                    statusCombo.getStyleClass().add("status-combo");
                    
                    Button saveBtn = new Button("Mettre à jour");
                    saveBtn.getStyleClass().add("save-button");
                    saveBtn.setOnAction(event -> {
                        String newStatus = statusCombo.getValue();
                        if (newStatus != null && !newStatus.equals(reclamation.getStatus())) {
                            reclamation.setStatus(newStatus);
                            try {
                                rs.update(reclamation);
                                refreshList();
                            } catch (SQLException e) {
                                showError(e.getMessage());
                            }
                        }
                    });
                    
                    statusBox.getChildren().addAll(statusCombo, saveBtn);
                    
                    // Assemblage de la carte
                    card.getChildren().addAll(header, descLabel, statusBox);
                    setGraphic(card);
                }
            }
        });

        refreshList();
    }

    @FXML
    void gererAvis(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/VoirAvis.fxml"));
            Parent root = loader.load();
            listView.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Erreur lors du chargement de la page : " + e.getMessage());
            alert.showAndWait();
        }
    }

    private void refreshList() {
        try {
            List<Reclamation> allReclamations = rs.readAll();
            ObservableList<Reclamation> filteredList = FXCollections.observableArrayList();
            
            int total = 0, pending = 0, inProgress = 0, resolved = 0;
            
            for (Reclamation r : allReclamations) {
                total++;
                switch (r.getStatus()) {
                    case "EN_ATTENTE": pending++; break;
                    case "EN_COURS": inProgress++; break;
                    case "RESOLUE": resolved++; break;
                }
                
                if (filterStatus.getValue().equals("Tous") || 
                    filterStatus.getValue().equals(r.getStatus())) {
                    filteredList.add(r);
                }
            }
            
            // Mise à jour des statistiques
            totalLabel.setText(String.valueOf(total));
            pendingLabel.setText(String.valueOf(pending));
            inProgressLabel.setText(String.valueOf(inProgress));
            resolvedLabel.setText(String.valueOf(resolved));
            
            listView.setItems(filteredList);
            
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
} 