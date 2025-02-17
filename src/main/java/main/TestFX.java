package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestFX extends Application {

    @Override
    public void start(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/UserDashbord.fxml"));
            Scene scene = new Scene(root);
            stage.setTitle("Covoituni - Dashboard Client");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            System.err.println("Error loading application: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
