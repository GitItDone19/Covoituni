package gui.Users;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import entities.User;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ChatbotController implements Initializable {

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox chatBox;
    @FXML
    private TextField messageField;
    @FXML
    private Button sendButton;
    @FXML
    private Button backButton;

    // Use NVIDIA's recommended API format
    private final String API_KEY = "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"; // Replace with your actual key
    private final String API_URL = "https://integrate.api.nvidia.com/v1/chat/completions"; // NVIDIA recommended URL
    
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    private HttpClient client;
    private User currentUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        client = HttpClient.newHttpClient();

        // Auto-scroll to bottom when new messages are added
        chatBox.heightProperty().addListener((observable, oldValue, newValue) ->
                scrollPane.setVvalue(1.0));

        // Add event listener for send button and Enter key
        sendButton.setOnAction(event -> sendMessage());
        messageField.setOnAction(event -> sendMessage());

        // Welcome message
        addBotMessage("Bonjour! Je suis votre assistant Covoituni. Comment puis-je vous aider aujourd'hui avec vos besoins de covoiturage?");
    }
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
    
    @FXML
    private void handleBackButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Users/DashboardUser.fxml"));
            Parent root = loader.load();
            
            // Pass the current user to the dashboard controller
            DashboardUserController dashboardController = loader.getController();
            if (currentUser != null) {
                dashboardController.setCurrentUser(currentUser);
            }
            
            Stage stage = (Stage) backButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de retourner au tableau de bord.");
        }
    }

    @FXML
    private void sendMessage() {
        String message = messageField.getText().trim();
        if (message.isEmpty()) return;

        addUserMessage(message);
        messageField.clear();
        sendToDeepSeek(message);
    }

    private void sendToDeepSeek(String message) {
        HBox typingContainer = new HBox();
        typingContainer.setAlignment(Pos.CENTER_LEFT);
        typingContainer.setPrefWidth(Double.MAX_VALUE);
        
        TextFlow typingIndicator = new TextFlow(new Text("Assistant est en train d'écrire..."));
        typingIndicator.getStyleClass().add("bot-message");
        typingIndicator.setId("typingIndicator");
        
        typingContainer.getChildren().add(typingIndicator);
        
        Platform.runLater(() -> chatBox.getChildren().add(typingContainer));

        try {
            // Prepare JSON body
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("model", "deepseek-ai/deepseek-r1");
            jsonBody.put("temperature", 0.6);
            jsonBody.put("top_p", 0.7);
            jsonBody.put("max_tokens", 4096);
            jsonBody.put("stream", false);

            JSONArray messagesArray = new JSONArray();
            messagesArray.put(new JSONObject().put("role", "system")
                    .put("content", "Vous êtes un assistant pour une application de covoiturage appelée Covoituni. Vous aidez les utilisateurs avec leurs questions sur le covoiturage, les réservations, et l'utilisation de l'application. Soyez amical, concis et utile."));
            messagesArray.put(new JSONObject().put("role", "user").put("content", message));

            jsonBody.put("messages", messagesArray);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + API_KEY)
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody.toString()))
                    .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(responseBody -> {
                        try {
                            JSONObject response = new JSONObject(responseBody);
                            String botReply = response.getJSONArray("choices")
                                    .getJSONObject(0)
                                    .getJSONObject("message")
                                    .getString("content");

                            Platform.runLater(() -> {
                                chatBox.getChildren().remove(typingContainer);
                                addBotMessage(botReply);
                            });
                        } catch (Exception e) {
                            Platform.runLater(() -> {
                                chatBox.getChildren().remove(typingContainer);
                                addBotMessage("Désolé, une erreur s'est produite lors du traitement de votre demande.");
                                e.printStackTrace();
                            });
                        }
                    })
                    .exceptionally(e -> {
                        Platform.runLater(() -> {
                            chatBox.getChildren().remove(typingContainer);
                            addBotMessage("Erreur de connexion à l'API. Veuillez réessayer plus tard.");
                            e.printStackTrace();
                        });
                        return null;
                    });
        } catch (Exception e) {
            chatBox.getChildren().remove(typingContainer);
            addBotMessage("Erreur interne lors de la préparation de la requête.");
            e.printStackTrace();
        }
    }

    private void addUserMessage(String message) {
        VBox messageBox = new VBox(5);
        messageBox.getStyleClass().add("user-container");
        messageBox.setMaxWidth(Double.MAX_VALUE);
        
        Label messageLabel = new Label(message);
        messageLabel.getStyleClass().add("user-message");
        messageLabel.setWrapText(true);
        
        Label timeLabel = new Label(LocalTime.now().format(timeFormatter));
        timeLabel.getStyleClass().add("timestamp");
        timeLabel.setAlignment(Pos.CENTER_RIGHT);
        
        messageBox.getChildren().addAll(messageLabel, timeLabel);
        messageBox.setAlignment(Pos.CENTER_RIGHT);
        
        Platform.runLater(() -> chatBox.getChildren().add(messageBox));
    }

    private void addBotMessage(String message) {
        VBox messageBox = new VBox(5);
        messageBox.getStyleClass().add("bot-container");
        messageBox.setMaxWidth(Double.MAX_VALUE);
        
        Label messageLabel = new Label(message);
        messageLabel.getStyleClass().add("bot-message");
        messageLabel.setWrapText(true);
        
        Label timeLabel = new Label(LocalTime.now().format(timeFormatter));
        timeLabel.getStyleClass().add("timestamp");
        timeLabel.setAlignment(Pos.CENTER_LEFT);
        
        messageBox.getChildren().addAll(messageLabel, timeLabel);
        messageBox.setAlignment(Pos.CENTER_LEFT);
        
        Platform.runLater(() -> chatBox.getChildren().add(messageBox));
    }
    
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
