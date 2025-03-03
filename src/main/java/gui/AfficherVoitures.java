package gui;

import entities.Car;
import entities.Categorie;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.hc.core5.http.ParseException;
import services.CarService;
import services.CategorieService;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


import javax.swing.text.AbstractDocument;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class AfficherVoitures implements Initializable {

    @FXML
    private VBox vboxContainer;
    @FXML
    private TextField searchVoitureField; // Search field for immatriculation
    @FXML
    private BarChart<String, Number> carAgeChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;
    @FXML
    private ComboBox<String> carModelDropdown;

    @FXML
    private TextField fuelConsumptionField;

    @FXML
    private Label carbonLabel;
    @FXML
    private ComboBox<String> carMakeDropdown;

    private Map<String, String> makeIdMap = new HashMap<>(); // Stores (Make Name -> Make ID)
    private Map<String, String> modelIdMap = new HashMap<>(); // Stores (Model Name -> Model ID)

    private final CarService carService = new CarService();
    private final CategorieService categorieService = new CategorieService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadData();
        carAgeChart.setVisible(false);
        loadCarModels();
    }

    private void loadData() {
        vboxContainer.getChildren().clear();
        try {
            List<Car> voitures = carService.readAll();
            for (Car voiture : voitures) {
                VBox carCard = new VBox();
                carCard.getStyleClass().add("car-card");
                carCard.setSpacing(5); // Espacement entre les éléments

                Label plaqueLabel = new Label(voiture.getPlaqueImatriculation());
                plaqueLabel.getStyleClass().add("title");

                Label marqueModeleLabel = new Label(voiture.getMarque() + " " + voiture.getModele());
                marqueModeleLabel.getStyleClass().add("subtitle");

                Label descriptionLabel = new Label(voiture.getDescription());
                descriptionLabel.getStyleClass().add("description");

                Label dateLabel = new Label("Date: " + voiture.getDateImatriculation());
                Label couleurLabel = new Label("Couleur: " + voiture.getCouleur());

                // Récupérer la catégorie
                String categorieName = "Non définie";
                try {
                    Categorie categorie = categorieService.findById(voiture.getCategorieId());
                    if (categorie != null) {
                        categorieName = categorie.getNom();
                    }
                } catch (SQLException e) {
                    categorieName = "Erreur catégorie";
                }
                Label categorieLabel = new Label("Catégorie: " + categorieName);

                VBox detailsBox = new VBox(5, dateLabel, couleurLabel, categorieLabel);
                detailsBox.getStyleClass().add("details-box");

                HBox buttonBox = new HBox(10);
                buttonBox.getStyleClass().add("button-box");

                Button editButton = new Button("Modifier");
                editButton.setOnAction(e -> handleModifierVoiture(voiture));

                Button deleteButton = new Button("Supprimer");
                deleteButton.getStyleClass().add("delete-button");
                deleteButton.setOnAction(e -> handleSupprimerVoiture(voiture));

                buttonBox.getChildren().addAll(editButton, deleteButton);

                carCard.getChildren().addAll(plaqueLabel, marqueModeleLabel, descriptionLabel, detailsBox, buttonBox);
                vboxContainer.getChildren().add(carCard);
            }
        } catch (SQLException e) {
            showError("Erreur lors du chargement des données", e.getMessage());
        }
    }


    private void handleSupprimerVoiture(Car voiture) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer la voiture");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cette voiture ?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            try {
                carService.delete(voiture);
                loadData();
            } catch (SQLException e) {
                showError("Erreur lors de la suppression", e.getMessage());
            }
        }
    }

    private void handleModifierVoiture(Car voiture) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("/ModifierVoiture.fxml"));
            Parent root = loader.load();

            ModifierVoiture controller = loader.getController();
            controller.setCar(voiture);

            Stage stage = new Stage();
            stage.setTitle("Modifier une voiture");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            loadData();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Erreur", "Impossible d'ouvrir la fenêtre de modification: " + e.getMessage());
        }
    }

    @FXML
    private void handleAjouterButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("/AjouterVoiture.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Ajouter une voiture");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            loadData();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Erreur", "Impossible d'ouvrir la fenêtre d'ajout: " + e.getMessage());
        }
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    // Search button action
    @FXML
    private void handleSearchVoiture() {
        String query = searchVoitureField.getText().trim();
        if (query.isEmpty()) {
            loadData(); // Reload all cars if search is empty
            return;
        }

        try {
            List<Car> filteredCars = carService.searchByImmatriculation(query);
            updateVoituresList(filteredCars);
        } catch (SQLException e) {
            e.printStackTrace(); // PRINT THE REAL ERROR IN CONSOLE
            showError("Erreur", "Impossible de rechercher les voitures: " + e.getMessage());
        }
    }


    // Method to update the UI with search results
    private void updateVoituresList(List<Car> voitures) {
        vboxContainer.getChildren().clear();
        for (Car voiture : voitures) {
            VBox carCard = new VBox();
            carCard.getStyleClass().add("car-card");

            Label plaqueLabel = new Label(voiture.getPlaqueImatriculation());
            plaqueLabel.getStyleClass().add("title");

            Label marqueModeleLabel = new Label(voiture.getMarque() + " " + voiture.getModele());
            marqueModeleLabel.getStyleClass().add("subtitle");

            carCard.getChildren().addAll(plaqueLabel, marqueModeleLabel);
            vboxContainer.getChildren().add(carCard);
        }
    }
    private void loadCarStatistics() {
        try {
            Map<String, Integer> carStats = carService.getCarCountByYear();

            // Convert map to a sorted list of entries
            List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(carStats.entrySet());
            sortedEntries.sort(Comparator.comparing(Map.Entry::getKey)); // Sort by year

            // Clear previous data
            carAgeChart.getData().clear();
            xAxis.setLabel("Année");
            yAxis.setLabel("Nombre de voitures");

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Voitures");

            for (Map.Entry<String, Integer> entry : sortedEntries) {
                series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }

            carAgeChart.getData().add(series);

        } catch (SQLException e) {
            showError("Erreur", "Impossible de charger les statistiques des voitures.");
        }
    }


    @FXML
    private void toggleStatistics() {
        boolean isVisible = carAgeChart.isVisible();
        carAgeChart.setVisible(!isVisible);

        if (!isVisible) { // Load statistics when becoming visible
            loadCarStatistics();
        }
    }
    @FXML
    private void handleSortByDate() {
        try {
            List<Car> sortedCars = carService.readAll(); // Récupérer les voitures
            sortedCars.sort(Comparator.comparing(Car::getDateImatriculation)); // Trier par date
            updateVoituresList(sortedCars); // Mettre à jour l'affichage
        } catch (SQLException e) {
            showError("Erreur", "Impossible de trier les voitures.");
        }
    }
    private void loadCarModels() {
        String apiKey = "12GmUrLap23BZ09Js80jA"; // Remplace avec ta vraie clé API
        String url = "https://www.carboninterface.com/api/v1/vehicle_makes";

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            request.addHeader("Authorization", "Bearer " + apiKey);
            request.addHeader("Content-Type", "application/json");

            try (CloseableHttpResponse response = client.execute(request)) {
                int statusCode = response.getCode();
                if (statusCode != 200) {
                    showError("Erreur API", "Problème avec l'API : " + statusCode);
                    return;
                }

                String jsonResponse = EntityUtils.toString(response.getEntity());

                JSONArray jsonArray = new JSONArray(jsonResponse);
                makeIdMap.clear();
                carMakeDropdown.getItems().clear();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject dataObj = jsonArray.getJSONObject(i).getJSONObject("data");
                    String id = dataObj.getString("id");
                    String makeName = dataObj.getJSONObject("attributes").getString("name");

                    makeIdMap.put(makeName, id);
                    carMakeDropdown.getItems().add(makeName);
                }

            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            e.printStackTrace();
            showError("Erreur", "Impossible de récupérer les marques de voitures.");
        }
    }

    @FXML
    private void handleCalculateCarbon() {
        String selectedCarModel = carModelDropdown.getValue();
        String fuelConsumptionText = fuelConsumptionField.getText().trim();

        if (selectedCarModel == null || fuelConsumptionText.isEmpty()) {
            carbonLabel.setText("Veuillez sélectionner un modèle et entrer la consommation.");
            return;
        }

        try {
            double fuelConsumption = Double.parseDouble(fuelConsumptionText);
            double carbonFootprint = fetchCarbonFootprintFromAPI(selectedCarModel, fuelConsumption);

            if (carbonFootprint != -1) {
                carbonLabel.setText("Empreinte carbone: " + carbonFootprint + " kg CO₂");
            } else {
                carbonLabel.setText("Erreur lors du calcul.");
            }
        } catch (NumberFormatException e) {
            carbonLabel.setText("Veuillez entrer un nombre valide.");
        }
    }
    private double fetchCarbonFootprintFromAPI(String carModel, double fuelConsumption) {
        String apiKey = "12GmUrLap23BZ09Js80jA"; // Remplace par ta vraie clé
        String url = "https://www.carboninterface.com/api/v1/estimates";

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url + "?vehicle_model=" + carModel + "&fuel_consumption=" + fuelConsumption);
            request.addHeader("Authorization", "Bearer " + apiKey);
            request.addHeader("Content-Type", "application/json");

            try (CloseableHttpResponse response = client.execute(request)) {
                int statusCode = response.getCode();
                String jsonResponse = EntityUtils.toString(response.getEntity());

                // Vérifie que la réponse est bien en JSON avant de parser
                if (statusCode != 200 || jsonResponse.isEmpty() || !jsonResponse.trim().startsWith("{")) {
                    showError("Erreur API", "Réponse non valide de l'API.");
                    return -1;
                }

                JSONObject json = new JSONObject(jsonResponse);
                return json.getJSONObject("data").getDouble("carbon_kg");

            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur", "Impossible de récupérer les données de l'API.");
            return -1; // En cas d'erreur
        }
    }




    @FXML
    private void handleMakeSelection() {
        String selectedMake = carMakeDropdown.getValue();
        if (selectedMake == null) {
            showError("Sélection invalide", "Veuillez sélectionner une marque.");
            return;
        }

        String makeId = makeIdMap.get(selectedMake);
        if (makeId != null) {
            loadVehicleModels(makeId);
        } else {
            showError("Erreur", "ID de marque introuvable.");
        }
    }


    private void loadVehicleModels(String makeId) {
        String apiKey = "12GmUrLap23BZ09Js80jA"; // Remplace avec ta vraie clé API
        String url = "https://www.carboninterface.com/api/v1/vehicle_makes/" + makeId + "/vehicle_models";

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            request.addHeader("Authorization", "Bearer " + apiKey);
            request.addHeader("Content-Type", "application/json");

            try (CloseableHttpResponse response = client.execute(request)) {
                int statusCode = response.getCode();
                if (statusCode != 200) {
                    showError("Erreur API", "Problème avec l'API : " + statusCode);
                    return;
                }

                String jsonResponse = EntityUtils.toString(response.getEntity());

                JSONArray dataArray = new JSONArray(jsonResponse);
                carModelDropdown.getItems().clear();
                modelIdMap.clear();

                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject dataObj = dataArray.getJSONObject(i).getJSONObject("data");
                    String id = dataObj.getString("id");
                    String modelName = dataObj.getJSONObject("attributes").getString("name");

                    modelIdMap.put(modelName, id);
                    carModelDropdown.getItems().add(modelName);
                }

            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            e.printStackTrace();
            showError("Erreur", "Impossible de charger les modèles de voitures.");
        }
    }









}