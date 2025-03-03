package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;
import java.io.IOException;

public class TestFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override

    public void start(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Users/LoginUser.fxml"));

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/styles/global.css").toExternalForm());

            stage.setTitle("Covoituni");
            stage.setScene(scene);

            // Set window size (Avoid full screen)
            stage.setWidth(900);
            stage.setHeight(750);
            stage.centerOnScreen(); // Centers the window

            // Ensure full-screen is disabled
            stage.setMaximized(false);
            stage.setFullScreen(false);
            stage.setResizable(true);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}