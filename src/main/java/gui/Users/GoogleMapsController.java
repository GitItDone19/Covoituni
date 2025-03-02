package com.yourpackage;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

public class GoogleMapsController implements Initializable {

    @FXML
    private WebView mapView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        WebEngine webEngine = mapView.getEngine();
        String mapHTML = generateMapHTML();
        webEngine.loadContent(mapHTML);
    }

    private String generateMapHTML() {
        String apiKey = "YOUR_GOOGLE_MAPS_API_KEY";
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta name=\"viewport\" content=\"initial-scale=1.0, user-scalable=no\">\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <title>Google Maps</title>\n" +
                "    <style>\n" +
                "        html, body, #map {\n" +
                "            height: 100%;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "        }\n" +
                "    </style>\n" +
                "    <script src=\"https://maps.googleapis.com/maps/api/js?key=" + apiKey + "&callback=initMap\" async defer></script>\n" +
                "    <script>\n" +
                "        function initMap() {\n" +
                "            var location = { lat: 36.8065, lng: 10.1815 }; // Example: Tunis, Tunisia\n" +
                "            var map = new google.maps.Map(document.getElementById('map'), {\n" +
                "                zoom: 13,\n" +
                "                center: location\n" +
                "            });\n" +
                "            var marker = new google.maps.Marker({\n" +
                "                position: location,\n" +
                "                map: map\n" +
                "            });\n" +
                "        }\n" +
                "    </script>\n" +
                "</head>\n" +
                "<body onload=\"initMap()\">\n" +
                "    <div id=\"map\"></div>\n" +
                "</body>\n" +
                "</html>";
    }
}
