package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import User.ServiceRole;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        try {
            // Initialize roles first
            ServiceRole serviceRole = new ServiceRole();
            serviceRole.initializeDefaultRoles();
            
            Parent root = FXMLLoader.load(getClass().getResource("/Users/LoginUser.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Covoituni");
            
            // Make it fullscreen
            stage.setMaximized(true);  // This will maximize the window
            // Optional: If you want true fullscreen (no window decorations)
            // stage.setFullScreen(true);
            
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
} 