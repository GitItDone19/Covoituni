package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import Services.ServiceRole;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        try {
            // Initialize roles first
            ServiceRole serviceRole = new ServiceRole();
            serviceRole.initializeDefaultRoles();
            
            Parent root = FXMLLoader.load(getClass().getResource("/LoginUser.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Covoituni - Connexion");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
} 