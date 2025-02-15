package gui;

import entities.Reclamation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import Services.ReclamationService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;

public class AdminDashboard implements Initializable {
    @FXML
    private ListView<Reclamation> reclamationList;
    @FXML
    private ComboBox<String> filterStatus;
    @FXML
    private Label totalLabel;
    @FXML
    private Label enCoursLabel;
    @FXML
    private Label resoluesLabel;

    private final ReclamationService reclamationService = new ReclamationService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize filter options
        filterStatus.getItems().addAll("Tous", "EN_COURS", "RESOLUE");
        filterStatus.setValue("Tous");
        filterStatus.setOnAction(e -> updateList());

        // Configure ListView cell factory
        reclamationList.setCellFactory(lv -> new ListCell<Reclamation>() {
            @Override
            protected void updateItem(Reclamation reclamation, boolean empty) {
                super.updateItem(reclamation, empty);
                if (empty || reclamation == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    VBox container = new VBox(5);
                    container.getStyleClass().add("reclamation-card");

                    // Header with ID and username
                    HBox header = new HBox(10);
                    Label idLabel = new Label("#" + reclamation.getId());
                    idLabel.getStyleClass().add("card-id");
                    
                    Label usernameLabel = new Label(reclamation.getUsername());
                    usernameLabel.getStyleClass().add("card-username");

                    Label statusLabel = new Label(reclamation.getStatus());
                    statusLabel.getStyleClass().addAll(
                        "status-badge",
                        reclamation.getStatus().toLowerCase().replace("_", "-")
                    );

                    Label dateLabel = new Label(formatDate(reclamation.getDateReclamation()));
                    dateLabel.getStyleClass().add("card-date");

                    Region spacer = new Region();
                    HBox.setHgrow(spacer, Priority.ALWAYS);
                    header.getChildren().addAll(idLabel, usernameLabel, spacer, statusLabel, dateLabel);

                    // Description
                    Label descLabel = new Label(reclamation.getDescription());
                    descLabel.getStyleClass().add("card-description");
                    descLabel.setWrapText(true);

                    // Details button
                    Button detailsButton = new Button("Détails");
                    detailsButton.setOnAction(e -> showDetails(reclamation));
                    detailsButton.getStyleClass().add("details-button");

                    container.getChildren().addAll(header, descLabel, detailsButton);
                    setGraphic(container);
                }
            }
        });

        updateList();
    }

    private String formatDate(java.sql.Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return sdf.format(date);
    }

    private void updateList() {
        try {
            List<Reclamation> reclamations;
            String filter = filterStatus.getValue();
            
            if ("Tous".equals(filter)) {
                reclamations = reclamationService.readAll();
            } else {
                reclamations = reclamationService.getReclamationsByStatus(filter);
            }
            
            reclamationList.getItems().setAll(reclamations);
            
            // Update counters
            long total = reclamations.size();
            long enCours = reclamations.stream().filter(r -> "EN_COURS".equals(r.getStatus())).count();
            long resolues = reclamations.stream().filter(r -> "RESOLUE".equals(r.getStatus())).count();
            
            totalLabel.setText(String.valueOf(total));
            enCoursLabel.setText(String.valueOf(enCours));
            resoluesLabel.setText(String.valueOf(resolues));
            
        } catch (SQLException e) {
            showError("Erreur lors du chargement des réclamations: " + e.getMessage());
        }
    }

    private void showDetails(Reclamation reclamation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ReclamationDetails.fxml"));
            Parent root = loader.load();
            
            ReclamationDetails controller = loader.getController();
            controller.setReclamation(reclamation);
            
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            
            reclamationList.getScene().setRoot(root);
        } catch (IOException e) {
            showError("Erreur lors de l'ouverture des détails: " + e.getMessage());
        }
    }

    @FXML
    void gererAvis(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/VoirAvis.fxml"));
            Parent root = loader.load();
            reclamationList.getScene().setRoot(root);
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