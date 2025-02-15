package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import javafx.scene.control.Alert;

public class TestFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        try {
            // Make sure to use absolute path for resource
            URL loginFxmlUrl = getClass().getResource("/LoginUser.fxml");
            if (loginFxmlUrl == null) {
                throw new IOException("Cannot find LoginUser.fxml");
            }
            
            FXMLLoader loader = new FXMLLoader(loginFxmlUrl);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            
            // Uncomment this once you have the CSS file ready
            // scene.getStylesheets().add(getClass().getResource("/styles/global.css").toExternalForm());
            
            primaryStage.setTitle("Covoituni");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.centerOnScreen();
            primaryStage.show();
            
        } catch (IOException e) {
            System.err.println("Error loading FXML: " + e.getMessage());
            e.printStackTrace();
            // Show error dialog to user
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Application Error");
            alert.setContentText("Failed to start application: " + e.getMessage());
            alert.showAndWait();
        }
    }
}