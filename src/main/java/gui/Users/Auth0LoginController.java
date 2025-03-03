package gui.Users;

import auth.Auth0User;
import auth.AuthService;
import entities.User;
import User.ServiceUser;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Controller for Auth0 login screen
 */
public class Auth0LoginController implements Initializable {
    
    @FXML private Button btnAuth0Login;
    @FXML private Button btnTraditionalLogin;
    @FXML private Label lblStatus;
    @FXML private ProgressIndicator progressIndicator;
    
    private ServiceUser serviceUser;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        serviceUser = new ServiceUser();
        progressIndicator.setVisible(false);
    }
    
    /**
     * Handle Auth0 login button click
     */
    @FXML
    private void handleAuth0Login() {
        lblStatus.setText("Connexion en cours...");
        progressIndicator.setVisible(true);
        btnAuth0Login.setDisable(true);
        
        AuthService.authenticate(
            // Success callback
            auth0User -> {
                try {
                    handleSuccessfulAuth0Login(auth0User);
                } catch (Exception e) {
                    handleLoginError(e);
                }
            },
            // Error callback
            this::handleLoginError
        );
    }
    
    /**
     * Handle traditional login button click
     */
    @FXML
    private void handleTraditionalLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Users/LoginUser.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) btnTraditionalLogin.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page de connexion traditionnelle.");
        }
    }
    
    /**
     * Process successful Auth0 login
     * @param auth0User User information from Auth0
     * @throws SQLException if database operation fails
     * @throws IOException if navigation fails
     */
    private void handleSuccessfulAuth0Login(Auth0User auth0User) throws SQLException, IOException {
        // Check if user exists in database
        User user = serviceUser.findByEmail(auth0User.getEmail());
        
        if (user == null) {
            // Create new user if not exists
            user = createUserFromAuth0(auth0User);
        } else {
            // Update existing user with Auth0 info
            updateUserWithAuth0Info(user, auth0User);
        }
        
        // Navigate to dashboard
        navigateToDashboard(user);
    }
    
    /**
     * Create a new user from Auth0 user information
     * @param auth0User User information from Auth0
     * @return Created User entity
     * @throws SQLException if database operation fails
     */
    private User createUserFromAuth0(Auth0User auth0User) throws SQLException {
        User newUser = new User();
        newUser.setEmail(auth0User.getEmail());
        newUser.setUsername(auth0User.getEmail().split("@")[0]); // Default username from email
        newUser.setPrenom(auth0User.getFirstName());
        newUser.setNom(auth0User.getLastName());
        newUser.setRoleCode("USER"); // Default role
        
        // Set a random password (user will use Auth0 to login)
        String randomPassword = generateRandomPassword();
        newUser.setPassword(randomPassword);
        
        // Save profile picture if available
        if (auth0User.getPicture() != null && !auth0User.getPicture().isEmpty()) {
            // Download and save profile picture
            // This would require additional implementation
        }
        
        // Save user to database
        serviceUser.add(newUser);
        
        // Reload user to get ID
        return serviceUser.findByEmail(auth0User.getEmail());
    }
    
    /**
     * Update existing user with Auth0 information
     * @param user Existing user entity
     * @param auth0User User information from Auth0
     * @throws SQLException if database operation fails
     */
    private void updateUserWithAuth0Info(User user, Auth0User auth0User) throws SQLException {
        // Update user information if needed
        boolean needsUpdate = false;
        
        if (user.getPrenom() == null || user.getPrenom().isEmpty()) {
            user.setPrenom(auth0User.getFirstName());
            needsUpdate = true;
        }
        
        if (user.getNom() == null || user.getNom().isEmpty()) {
            user.setNom(auth0User.getLastName());
            needsUpdate = true;
        }
        
        if (needsUpdate) {
            serviceUser.update(user);
        }
    }
    
    /**
     * Navigate to user dashboard
     * @param user Authenticated user
     * @throws IOException if navigation fails
     */
    private void navigateToDashboard(User user) throws IOException {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Users/DashboardUser.fxml"));
                Parent root = loader.load();
                
                DashboardUserController controller = loader.getController();
                controller.setCurrentUser(user);
                
                Stage stage = (Stage) btnAuth0Login.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger le tableau de bord: " + e.getMessage());
            }
        });
    }
    
    /**
     * Handle login errors
     * @param e Exception that occurred
     */
    private void handleLoginError(Exception e) {
        Platform.runLater(() -> {
            progressIndicator.setVisible(false);
            btnAuth0Login.setDisable(false);
            lblStatus.setText("Échec de la connexion");
            showAlert(Alert.AlertType.ERROR, "Erreur de connexion", 
                    "Impossible de se connecter avec Auth0: " + e.getMessage());
            e.printStackTrace();
        });
    }
    
    /**
     * Generate a random password for new users
     * @return Random password
     */
    private String generateRandomPassword() {
        // Generate a secure random password
        byte[] randomBytes = new byte[16];
        java.security.SecureRandom secureRandom = new java.security.SecureRandom();
        secureRandom.nextBytes(randomBytes);
        return java.util.Base64.getEncoder().encodeToString(randomBytes);
    }
    
    /**
     * Show an alert dialog
     * @param type Alert type
     * @param title Alert title
     * @param content Alert content
     */
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 