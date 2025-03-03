package gui.Users;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class GoogleTranslatorController {

    private static final String API_URL = "https://google-translator9.p.rapidapi.com/v2";
    private static final String API_KEY = "e70cf6b587mshe7c361c5a2b7fb7p1e1a36jsnb3fa9ae110ff"; // Replace with your actual key
    private static final String API_HOST = "google-translator9.p.rapidapi.com";

    @FXML
    private TextField textInput;
    @FXML
    private Button translateButton;
    @FXML
    private TextArea resultArea;

    // ✅ Two ComboBoxes for Source and Target Languages
    @FXML
    private ComboBox<String> sourceLanguageSelector;
    @FXML
    private ComboBox<String> targetLanguageSelector;

    @FXML
    public void initialize() {
        // ✅ Populate both dropdowns with languages
        sourceLanguageSelector.getItems().addAll("English", "French", "Arabic");
        targetLanguageSelector.getItems().addAll("English", "French", "Arabic");

        // ✅ Set default selections
        sourceLanguageSelector.setValue("English");  // Default source: English
        targetLanguageSelector.setValue("French");   // Default target: French
    }

    @FXML
    private void handleTranslateText() {
        String text = textInput.getText().trim();
        if (text.isEmpty()) {
            resultArea.setText("Please enter some text.");
            return;
        }

        // ✅ Get selected source and target languages
        String sourceLanguage = getLanguageCode(sourceLanguageSelector.getValue());
        String targetLanguage = getLanguageCode(targetLanguageSelector.getValue());

        // ✅ Run API request in a separate thread
        new Thread(() -> {
            String response = translateText(text, sourceLanguage, targetLanguage);
            Platform.runLater(() -> resultArea.setText(response));
        }).start();
    }

    private String translateText(String text, String sourceLanguage, String targetLanguage) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(API_URL);
            request.setHeader("X-RapidAPI-Key", API_KEY);
            request.setHeader("X-RapidAPI-Host", API_HOST);
            request.setHeader("Content-Type", "application/json");

            // ✅ Create JSON body
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("q", text);
            jsonBody.put("source", sourceLanguage);
            jsonBody.put("target", targetLanguage);
            jsonBody.put("format", "text");

            request.setEntity(new StringEntity(jsonBody.toString(), ContentType.APPLICATION_JSON));

            // ✅ Execute request
            try (CloseableHttpResponse response = httpClient.execute(request);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {

                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                // 🔴 Print raw response for debugging
                System.out.println("Raw API Response: " + result.toString());

                // ✅ Parse JSON response
                JSONObject jsonResponse = new JSONObject(result.toString());

                // ✅ Extract only the translated text
                if (jsonResponse.has("data") && jsonResponse.getJSONObject("data").has("translations")) {
                    return jsonResponse.getJSONObject("data")
                            .getJSONArray("translations")
                            .getJSONObject(0)
                            .getString("translatedText");
                } else {
                    return "Error: Unexpected response format.";
                }
            }
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private String getLanguageCode(String language) {
        switch (language) {
            case "English": return "en";
            case "French": return "fr";
            case "Arabic": return "ar";
            default: return "en"; // Default to English
        }
    }
}
