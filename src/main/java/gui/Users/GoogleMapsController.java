package gui.Users;

import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class GoogleMapsController {

    @FXML
    private WebView webView;

    private static final String API_KEY = "AIzaSyDS5UbPHEoKmTSGLtRLBzbSclyaV-lufcI"; // Replace with your key

    @FXML
    public void initialize() {
        WebEngine webEngine = webView.getEngine();
        String mapHtml = generateMapHtml(API_KEY);
        webEngine.loadContent(mapHtml);
    }

    private String generateMapHtml(String apiKey) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset='utf-8'>\n" +
                "    <meta name='viewport' content='initial-scale=1.0, user-scalable=no'>\n" +
                "    <style>\n" +
                "        html, body { height: 100%; margin: 0; padding: 0; }\n" +
                "        #map { height: 100%; width: 100%; }\n" +
                "    </style>\n" +
                "    <script src='https://maps.googleapis.com/maps/api/js?key=" + apiKey + "'></script>\n" +
                "    <script>\n" +
                "        function initMap() {\n" +
                "            var location = { lat: 36.8065, lng: 10.1815 }; // Update with valid coordinates\n" +
                "            var map = new google.maps.Map(document.getElementById('map'), {\n" +
                "                zoom: 14,\n" +
                "                center: location,\n" +
                "                mapTypeId: 'roadmap' // Ensure it's not satellite mode\n" +
                "            });\n" +
                "            var marker = new google.maps.Marker({ position: location, map: map });\n" +
                "        }\n" +
                "        window.onload = initMap;\n" +
                "    </script>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div id='map'></div>\n" +
                "</body>\n" +
                "</html>";
    }

}
