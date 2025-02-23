package gui.Users;

import entities.User;
import entities.Reclamation;
import Services.ReclamationService;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
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

public class ViewReclamationsController {
    @FXML private ListView<Reclamation> listViewReclamations;
    @FXML private Label lblUserName;
    @FXML private Button btnRetour;

    private User currentUser;
    private ReclamationService reclamationService;

    public void setCurrentUser(User user) {
        this.currentUser = user;
        lblUserName.setText("Réclamations de: " + user.getNom() + " " + user.getPrenom());
        loadReclamations();
    }

    @FXML
    private void initialize() {
        reclamationService = new ReclamationService();
        setupListView();
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
                    Button detailBtn = new Button("Détails");
                    detailBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
                    detailBtn.setOnAction(e -> handleDetail(reclamation));

                    controlsBox.getChildren().addAll(detailBtn);

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
            case "pending":
                return "#f39c12"; // Orange
            case "resolved":
                return "#27ae60"; // Green
            case "rejected":
                return "#e74c3c"; // Red
            default:
                return "#95a5a6"; // Gray
        }
    }
    private void loadReclamations() {
        try {
            List<Reclamation> reclamations = reclamationService.readAll();
            listViewReclamations.getItems().clear();

            for (Reclamation reclamation : reclamations) {
                if (reclamation.getUser() != null && reclamation.getUser().getId() == currentUser.getId()) {
                    listViewReclamations.getItems().add(reclamation);
                }
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error loading reclamations: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRetour() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Users/DashboardUser.fxml"));
            Parent root = loader.load();
            
            // Pass the current user to the dashboard controller
            DashboardUserController controller = loader.getController();
            controller.setCurrentUser(currentUser);
            
            Stage stage = (Stage) btnRetour.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
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

    private void handleDetail(Reclamation reclamation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Users/DetailReclamation.fxml"));
            Parent root = loader.load();

            DetailReclamationController controller = loader.getController();
            controller.setReclamation(reclamation);
            controller.setCurrentUser(currentUser);
            
            Stage stage = (Stage) listViewReclamations.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Détails de la Réclamation");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur de navigation: " + e.getMessage());
        }
    }
}