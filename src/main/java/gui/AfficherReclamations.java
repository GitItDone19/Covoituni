package gui;

import entities.Reclamation;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import Services.ReclamationService;

import java.io.IOException;
import java.sql.SQLException;

public class AfficherReclamations {

    private final ReclamationService rs = new ReclamationService();

    @FXML
    private TableView<Reclamation> tableView;

    @FXML
    private TableColumn<Reclamation, Integer> idCol;

    @FXML
    private TableColumn<Reclamation, String> descriptionCol;

    @FXML
    private TableColumn<Reclamation, String> statusCol;

    @FXML
    private TableColumn<Reclamation, String> dateCol;

    @FXML
    private TableColumn<Reclamation, Void> actionsCol;

    @FXML
    void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("dateReclamation"));

        // Personnalisation des boutons d'action
        actionsCol.setCellFactory(param -> new TableCell<>() {
            private final Button editBtn = new Button("✏");
            private final Button deleteBtn = new Button("🗑");
            private final HBox box = new HBox(5, editBtn, deleteBtn);

            {
                editBtn.getStyleClass().add("edit-button");
                deleteBtn.getStyleClass().add("delete-button");
                
                editBtn.setStyle("-fx-background-color: #FFA500; -fx-min-width: 40px;");
                deleteBtn.setStyle("-fx-background-color: #FF4444; -fx-min-width: 40px;");
                
                box.setAlignment(javafx.geometry.Pos.CENTER);

                editBtn.setOnAction(event -> {
                    Reclamation reclamation = getTableView().getItems().get(getIndex());
                    // Logique de modification
                });

                deleteBtn.setOnAction(event -> {
                    Reclamation reclamation = getTableView().getItems().get(getIndex());
                    try {
                        rs.delete(reclamation);
                        refreshTable();
                    } catch (SQLException e) {
                        showError(e.getMessage());
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });

        refreshTable();
    }

    private void refreshTable() {
        try {
            tableView.setItems(FXCollections.observableArrayList(rs.readAll()));
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
            tableView.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
} 