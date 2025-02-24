package gui.Users;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import main.entities.Annonce;
import Services.AnnonceService;

import java.net.URL;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class HistoriqueController implements Initializable {
    @FXML
    private ListView<Annonce> historiqueListView;
    
    private AnnonceService annonceService;
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        annonceService = new AnnonceService();
        
        // Configuration de la cellule personnalisée
        historiqueListView.setCellFactory(lv -> new ListCell<Annonce>() {
            private final HBox content = new HBox(15);
            
            {
                content.setStyle("-fx-alignment: center-left;");
            }

            @Override
            protected void updateItem(Annonce annonce, boolean empty) {
                super.updateItem(annonce, empty);
                
                if (empty || annonce == null) {
                    setGraphic(null);
                } else {
                    VBox details = new VBox(5);
                    Label titleLabel = new Label(annonce.getTitre());
                    titleLabel.getStyleClass().add("trajet-title");
                    
                    Label datePublicationLabel = new Label("Publié le : " + 
                        dateFormatter.format(annonce.getDatePublication()));
                    datePublicationLabel.getStyleClass().add("trajet-details");
                    
                    Label dateTerminationLabel;
                    if (annonce.getDateTermination() != null) {
                        dateTerminationLabel = new Label("Terminé le : " + 
                            dateFormatter.format(annonce.getDateTermination()));
                    } else {
                        dateTerminationLabel = new Label("Terminé le : Non disponible");
                    }
                    dateTerminationLabel.getStyleClass().add("trajet-details");
                    
                    Label detailsLabel = new Label(
                        String.format("%s - Places disponibles: %d",
                            annonce.getDescription(),
                            annonce.getAvailableSeats()
                        )
                    );
                    detailsLabel.getStyleClass().add("trajet-details");
                    
                    Label statusLabel = new Label(annonce.getStatus());
                    statusLabel.getStyleClass().addAll("status-label", "status-" + annonce.getStatus().toLowerCase());
                    
                    details.getChildren().addAll(
                        titleLabel, 
                        datePublicationLabel,
                        dateTerminationLabel,
                        detailsLabel, 
                        statusLabel
                    );
                    
                    content.getChildren().clear();
                    content.getChildren().add(details);
                    HBox.setHgrow(details, Priority.ALWAYS);
                    
                    setGraphic(content);
                }
            }
        });
        
        loadHistorique();
    }

    private void loadHistorique() {
        try {
            historiqueListView.getItems().clear();
            historiqueListView.getItems().addAll(annonceService.readHistorique());
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                     "Erreur lors du chargement de l'historique: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 