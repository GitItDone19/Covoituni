package gui.Avis;

import entities.Avis;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import Services.Avis.AvisService;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

public class VoirAvis {

    private final AvisService as = new AvisService();

    @FXML
    private ListView<Avis> listView;

    @FXML
    private ComboBox<String> filterNote;

    @FXML
    private Label totalLabel;

    @FXML
    private Label avgLabel;

    @FXML
    private Label fiveStarLabel;

    @FXML
    void initialize() {
        filterNote.getItems().addAll("Tous", "5 étoiles", "4 étoiles", "3 étoiles", "2 étoiles", "1 étoile");
        filterNote.setValue("Tous");
        filterNote.setOnAction(e -> refreshList());

        listView.setCellFactory(param -> new ListCell<Avis>() {
            @Override
            protected void updateItem(Avis avis, boolean empty) {
                super.updateItem(avis, empty);
                if (empty || avis == null) {
                    setGraphic(null);
                } else {
                    VBox card = new VBox(10);
                    card.getStyleClass().add("avis-card");
                    
                    // En-tête
                    HBox header = new HBox(10);
                    // Get user's full name
                    String userName = avis.getUser() != null ? 
                        avis.getUser().getNom() + " " + avis.getUser().getPrenom() : 
                        "Utilisateur inconnu";
                    Label userLabel = new Label(userName);
                    userLabel.getStyleClass().add("card-username");
                    
                    // Note avec étoiles
                    Label noteLabel = new Label("★".repeat(avis.getRating()));
                    noteLabel.getStyleClass().add("card-rating");
                    
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    Label dateLabel = new Label(sdf.format(avis.getDateAvis()));
                    dateLabel.getStyleClass().add("card-date");
                    
                    Region spacer = new Region();
                    HBox.setHgrow(spacer, Priority.ALWAYS);
                    header.getChildren().addAll(userLabel, noteLabel, spacer, dateLabel);
                    
                    // Commentaire
                    Label commentLabel = new Label(avis.getCommentaire());
                    commentLabel.getStyleClass().add("card-comment");
                    commentLabel.setWrapText(true);
                    
                    // Réponse (si elle existe)
                    if (avis.getReponseAvis() != null && !avis.getReponseAvis().isEmpty()) {
                        VBox reponseBox = new VBox(5);
                        reponseBox.getStyleClass().add("reponse-box");
                        Label reponseLabel = new Label("Réponse: " + avis.getReponseAvis());
                        reponseLabel.setWrapText(true);
                        reponseBox.getChildren().add(reponseLabel);
                        card.getChildren().addAll(header, commentLabel, reponseBox);
                    } else {
                        card.getChildren().addAll(header, commentLabel);
                    }
                    
                    setGraphic(card);
                }
            }
        });

        refreshList();
    }

    @FXML
    void retourAjout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/User/AjouterAvis.fxml"));
            Parent root = loader.load();
            listView.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Erreur lors du chargement de la page : " + e.getMessage());
            alert.showAndWait();
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

    private void refreshList() {
        try {
            List<Avis> allAvis = as.readAll();
            ObservableList<Avis> filteredList = FXCollections.observableArrayList();
            
            int total = 0;
            double sumNotes = 0;
            int fiveStars = 0;
            
            for (Avis a : allAvis) {
                total++;
                sumNotes += a.getRating();
                if (a.getRating() == 5) fiveStars++;
                
                String filter = filterNote.getValue();
                if (filter.equals("Tous") || 
                    filter.equals(a.getRating() + " étoile" + (a.getRating() > 1 ? "s" : ""))) {
                    filteredList.add(a);
                }
            }
            
            if (total > 0) {
                totalLabel.setText(String.valueOf(total));
                avgLabel.setText(String.format("%.1f", sumNotes / total));
                fiveStarLabel.setText(String.valueOf(fiveStars));
            }
            
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