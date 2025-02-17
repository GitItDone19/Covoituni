package services.Annonce;

import entities.Annonce.Annonce;
import entities.Trajet.Trajet;
import services.Trajet.TrajetService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.collections.FXCollections;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import java.sql.SQLException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import services.Reservation.ReservationService;

/**
 * Contrôleur principal pour la gestion des annonces
 * Gère l'affichage et les opérations sur la liste des annonces
 */
public class AnnonceController {

    private final AnnonceService annonceService = new AnnonceService();
    private final TrajetService trajetService = new TrajetService();
    private final ReservationService reservationService = new ReservationService();

    // Couleurs Bolt
    private static final String BOLT_GREEN = "#34D186";
    private static final String BOLT_DARK = "#2F313F";
    private static final String BOLT_GRAY = "#7A7D8C";
    private static final String BOLT_LIGHT_BG = "#F8F9FA";
    private static final String BOLT_BORDER = "#E9ECEF";

    @FXML
    private ListView<Annonce> annonceListView;

    @FXML
    private TableColumn<Annonce, String> colTitre;

    @FXML
    private TableColumn<Annonce, String> colDescription;

    @FXML
    private TableColumn<Annonce, String> colDepartureDate;

    @FXML
    private TableColumn<Annonce, String> colDeparturePoint;

    @FXML
    private TableColumn<Annonce, String> colArrivalPoint;

    @FXML
    private TableColumn<Annonce, String> colStatus;

    /**
     * Initialise la vue et configure la liste des annonces
     */
    @FXML
    public void initialize() {
        System.out.println("Initialisation de AnnonceController");
        loadAnnonces();
        configureListView();
    }

    /**
     * Charge les annonces depuis la base de données
     */
    public void loadAnnonces() {
        try {
            System.out.println("Chargement des annonces...");
            List<Annonce> annonces = annonceService.readAll();
            System.out.println("Nombre d'annonces trouvées : " + annonces.size());
            annonceListView.setItems(FXCollections.observableArrayList(annonces));
        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement des annonces : " + e.getMessage());
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger les annonces : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void showAjouterAnnonce() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Annonce/AjouterAnnonce.fxml"));
            Parent root = loader.load();
            
            AjouterAnnonceController controller = loader.getController();
            controller.setParentController(this);
            
            Stage stage = new Stage();
            stage.setTitle("Ajouter une annonce");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            
        } catch (IOException e) {
            showAlert("Erreur", 
                     "Impossible d'ouvrir la fenêtre d'ajout : " + e.getMessage(), 
                     Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void modifierAnnonce(Annonce annonce) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Annonce/ModifierAnnonce.fxml"));
            Parent root = loader.load();
            
            ModifierAnnonceController controller = loader.getController();
            controller.setAnnonce(annonce);
            
            Stage stage = new Stage();
            stage.setTitle("Modifier l'annonce");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            
            // Rafraîchir la liste après modification
            loadAnnonces();
            
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre de modification", Alert.AlertType.ERROR);
        }
    }

    private void supprimerAnnonce(Annonce annonce) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText("Supprimer l'annonce");
        confirmation.setContentText("Êtes-vous sûr de vouloir supprimer cette annonce ?");

        if (confirmation.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                annonceService.delete(annonce.getId());
                loadAnnonces(); // Recharger la liste
                showAlert("Succès", "Annonce supprimée avec succès", Alert.AlertType.INFORMATION);
            } catch (SQLException e) {
                showAlert("Erreur", "Erreur lors de la suppression : " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    private void handleReservation(Annonce annonce) {
        try {
            // Récupérer le trajet associé
            Trajet trajet = trajetService.readById(annonce.getTrajetId());
            
            if (trajet.getAvailableSeats() <= 0) {
                showAlert("Impossible de réserver", 
                    "Désolé, il n'y a plus de places disponibles pour ce trajet.", 
                    Alert.AlertType.WARNING);
                return;
            }

            // Mettre à jour le nombre de places disponibles
            trajet.setAvailableSeats(trajet.getAvailableSeats() - 1);
            trajetService.update(trajet);

            // Créer la réservation (pour l'instant on utilise 1 comme user_id par défaut)
            reservationService.create(annonce.getId(), 1);

            // Si c'était la dernière place, mettre à jour le statut de l'annonce
            if (trajet.getAvailableSeats() == 0) {
                annonce.setStatus("CLOSED");
                annonceService.update(annonce);
            }

            // Rafraîchir la liste
            loadAnnonces();

            showAlert("Réservation confirmée", 
                "Votre réservation a été effectuée avec succès!\nPlaces restantes: " + trajet.getAvailableSeats(), 
                Alert.AlertType.INFORMATION);

        } catch (SQLException e) {
            showAlert("Erreur", 
                "Erreur lors de la réservation : " + e.getMessage(), 
                Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Configure l'affichage des éléments dans la ListView
     */
    private void configureListView() {
        annonceListView.setCellFactory(lv -> new ListCell<Annonce>() {
            @Override
            protected void updateItem(Annonce annonce, boolean empty) {
                super.updateItem(annonce, empty);
                if (empty || annonce == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Conteneur principal
                    VBox container = new VBox(8);
                    container.setPadding(new Insets(16));
                    container.setStyle(String.format(
                        "-fx-background-color: white; " +
                        "-fx-border-color: %s; " +
                        "-fx-border-width: 0 0 1 0; " +
                        "-fx-background-radius: 8; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.04), 4, 0, 0, 1);",
                        BOLT_BORDER
                    ));

                    // En-tête avec ID et titre
                    HBox header = new HBox(10);
                    header.setAlignment(Pos.CENTER_LEFT);

                    Label idLabel = new Label("Annonce #" + annonce.getId());
                    idLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: " + BOLT_DARK);

                    Label titreLabel = new Label(annonce.getTitre());
                    titreLabel.setStyle("-fx-font-size: 14; -fx-text-fill: " + BOLT_GRAY);

                    header.getChildren().addAll(idLabel, titreLabel);

                    // Description
                    Label descLabel = new Label(annonce.getDescription());
                    descLabel.setStyle("-fx-text-fill: " + BOLT_GRAY);
                    descLabel.setWrapText(true);

                    // Informations du trajet
                    HBox trajetInfo = new HBox(10);
                    trajetInfo.setAlignment(Pos.CENTER_LEFT);

                    try {
                        Trajet trajet = trajetService.readById(annonce.getTrajetId());
                        Label trajetLabel = new Label(
                            String.format("%s → %s", trajet.getDeparturePoint(), trajet.getArrivalPoint())
                        );
                        trajetLabel.setStyle("-fx-text-fill: " + BOLT_GRAY);
                        trajetInfo.getChildren().add(trajetLabel);
                    } catch (SQLException e) {
                        Label errorLabel = new Label("Trajet non disponible");
                        errorLabel.setStyle("-fx-text-fill: #FF4757");
                        trajetInfo.getChildren().add(errorLabel);
                    }

                    // Statut
                    Label statusLabel = new Label(annonce.getStatus());
                    statusLabel.setStyle(String.format(
                        "-fx-text-fill: %s; -fx-font-weight: bold;",
                        annonce.getStatus().equals("OPEN") ? "#27AE60" : "#FF4757"
                    ));

                    // Boutons d'action
                    HBox buttonsBox = new HBox(10);
                    buttonsBox.setAlignment(Pos.CENTER_RIGHT);

                    if (annonce.getStatus().equals("OPEN")) {
                        Button reserveButton = new Button("Réserver");
                        reserveButton.getStyleClass().add("button-primary");
                        reserveButton.setOnAction(e -> handleReservation(annonce));
                        buttonsBox.getChildren().add(reserveButton);
                    }

                    Button modifyButton = new Button("Modifier");
                    modifyButton.getStyleClass().add("button-secondary");
                    modifyButton.setOnAction(e -> modifierAnnonce(annonce));

                    Button deleteButton = new Button("Supprimer");
                    deleteButton.getStyleClass().add("button-danger");
                    deleteButton.setOnAction(e -> supprimerAnnonce(annonce));

                    buttonsBox.getChildren().addAll(modifyButton, deleteButton);

                    // Assembler tous les éléments
                    container.getChildren().addAll(
                        header,
                        descLabel,
                        trajetInfo,
                        statusLabel,
                        buttonsBox
                    );

                    setGraphic(container);
                }
            }
        });

        // Style de la ListView
        annonceListView.setStyle(
            "-fx-background-color: transparent; " +
            "-fx-padding: 10; " +
            String.format("-fx-selection-bar: %s22;", BOLT_GREEN) +
            String.format("-fx-selection-bar-non-focused: %s11;", BOLT_GREEN)
        );
    }
}
