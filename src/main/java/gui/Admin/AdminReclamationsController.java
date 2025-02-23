package gui.Admin;

import entities.Reclamation;
import Services.ReclamationService;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import java.text.SimpleDateFormat;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class AdminReclamationsController {
    @FXML private ListView<Reclamation> listViewReclamations;
    @FXML private Button btnRetour;

    private ReclamationService reclamationService;

    @FXML
    private void initialize() {
        reclamationService = new ReclamationService();
        setupListView();
        loadReclamations();
    }

    private void setupListView() {
        listViewReclamations.setCellFactory(param -> new ListCell<Reclamation>() {
            @Override
            protected void updateItem(Reclamation reclamation, boolean empty) {
                super.updateItem(reclamation, empty);
                if (empty || reclamation == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Main card container
                    VBox card = new VBox(10);
                    card.setPadding(new Insets(15));
                    card.setStyle("-fx-background-color: white;" +
                            "-fx-background-radius: 15;" +
                            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 3);" +
                            "-fx-border-radius: 15;" +
                            "-fx-border-color: #e0e0e0;");

                    // Reclamation details
                    VBox detailsBox = new VBox(8);
                    Label subjectLabel = new Label(reclamation.getSubject());
                    subjectLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
                    
                    Label descLabel = new Label(reclamation.getDescription());
                    descLabel.setWrapText(true);
                    descLabel.setStyle("-fx-text-fill: #666666;");

                    // Status and date
                    HBox statusBar = new HBox(15);
                    statusBar.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
                    
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Label dateLabel = new Label("📅 " + dateFormat.format(reclamation.getDate()));
                    
                    Label statusLabel = new Label(reclamation.getState());
                    statusLabel.setStyle(String.format("-fx-background-color: %s; -fx-text-fill: white; " +
                            "-fx-padding: 5 15; -fx-background-radius: 15;",
                            getStatusColor(reclamation.getState())));

                    statusBar.getChildren().addAll(statusLabel, dateLabel);

                    // Admin controls
                    HBox controlsBox = new HBox(10);
                    Button resolveBtn = new Button("Marquer comme résolu");
                    resolveBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");
                    resolveBtn.setOnAction(e -> handleResolve(reclamation));
                    
                    Button deleteBtn = new Button("Supprimer");
                    deleteBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
                    deleteBtn.setOnAction(e -> handleDelete(reclamation));

                    Button replyBtn = new Button("Répondre");
                    replyBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
                    replyBtn.setOnAction(e -> handleReply(reclamation));

                    controlsBox.getChildren().addAll(resolveBtn, deleteBtn, replyBtn);

                    // Assemble card
                    detailsBox.getChildren().addAll(subjectLabel, descLabel, statusBar);
                    card.getChildren().addAll(detailsBox, controlsBox);
                    
                    // Hover effects
                    card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: #f8f9fa;"));
                    card.setOnMouseExited(e -> card.setStyle("-fx-background-color: white;"));

                    setGraphic(card);
                }
            }
        });
    }

    private String getStatusColor(String status) {
        switch (status.toLowerCase()) {
            case "pending": return "#f1c40f";
            case "resolved": return "#2ecc71";
            case "rejected": return "#e74c3c";
            default: return "#95a5a6";
        }
    }

    private void loadReclamations() {
        try {
            List<Reclamation> reclamations = reclamationService.readAll();
            listViewReclamations.getItems().setAll(reclamations);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur de chargement: " + e.getMessage());
        }
    }

    private void handleResolve(Reclamation reclamation) {
        try {
            reclamation.setState("resolved");
            reclamationService.update(reclamation);
            loadReclamations();
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Réclamation marquée comme résolue !");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la mise à jour: " + e.getMessage());
        }
    }

    private void handleDelete(Reclamation reclamation) {
        try {
            reclamationService.delete(reclamation);
            loadReclamations();
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Réclamation supprimée avec succès !");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la suppression: " + e.getMessage());
        }
    }

    private void handleReply(Reclamation reclamation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/ReplyReclamation.fxml"));
            Parent root = loader.load();

            // Pass the reclamation to the reply controller
            ReplyReclamationController replyController = loader.getController();
            replyController.setReclamation(reclamation);

            Stage stage = (Stage) listViewReclamations.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Répondre à la Réclamation");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur de navigation: " + e.getMessage());
        }
    }

    @FXML
    private void handleRetour() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Admin/AdminDashboard.fxml"));
            Stage stage = (Stage) btnRetour.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur de navigation: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 