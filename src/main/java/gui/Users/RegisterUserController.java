package gui.Users;

import entities.Role;
import entities.User;
import User.ServiceUser;
import User.ServiceRole;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import utils.PasswordUtil;

public class RegisterUserController implements Initializable {
    
    @FXML private TextField tfUsername;
    @FXML private TextField tfNom;
    @FXML private TextField tfPrenom;
    @FXML private TextField tfTel;
    @FXML private TextField tfEmail;
    @FXML private PasswordField tfMdp;
    @FXML private PasswordField tfConfirmMdp;
    @FXML private ComboBox<String> cbRole;
    
    private ServiceUser serviceUser;
    private ServiceRole serviceRole;
    
    // Regex patterns pour la validation
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{3,20}$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z\\s\\-']{2,30}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{8}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$");
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        serviceUser = new ServiceUser();
        serviceRole = new ServiceRole();
        
        try {
            // Get roles from database
            ArrayList<Role> roles = serviceRole.readAll();
            ArrayList<String> roleDisplayNames = new ArrayList<>();
            for (Role role : roles) {
                // Only show driver and passenger roles for registration
                if (!role.getCode().equals(Role.ADMIN_CODE)) {
                    roleDisplayNames.add(role.getDisplayName());
                }
            }
            
            cbRole.setItems(FXCollections.observableArrayList(roleDisplayNames));
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Erreur lors du chargement des types de compte: " + e.getMessage());
        }
        
        // Ajouter des écouteurs pour la validation en temps réel
        setupValidationListeners();
    }
    
    private void setupValidationListeners() {
        // Validation du nom d'utilisateur
        tfUsername.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                if (!USERNAME_PATTERN.matcher(newValue).matches()) {
                    tfUsername.setStyle("-fx-border-color: red;");
                    showTooltip(tfUsername, "Le nom d'utilisateur doit contenir entre 3 et 20 caractères alphanumériques ou underscore (_)");
                } else {
                    tfUsername.setStyle("-fx-border-color: green;");
                    hideTooltip(tfUsername);
                }
            } else {
                tfUsername.setStyle("");
                hideTooltip(tfUsername);
            }
        });
        
        // Validation du nom
        tfNom.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                if (!NAME_PATTERN.matcher(newValue).matches()) {
                    tfNom.setStyle("-fx-border-color: red;");
                    showTooltip(tfNom, "Le nom doit contenir entre 2 et 30 caractères alphabétiques, espaces, tirets ou apostrophes");
                } else {
                    tfNom.setStyle("-fx-border-color: green;");
                    hideTooltip(tfNom);
                }
            } else {
                tfNom.setStyle("");
                hideTooltip(tfNom);
            }
        });
        
        // Validation du prénom
        tfPrenom.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                if (!NAME_PATTERN.matcher(newValue).matches()) {
                    tfPrenom.setStyle("-fx-border-color: red;");
                    showTooltip(tfPrenom, "Le prénom doit contenir entre 2 et 30 caractères alphabétiques, espaces, tirets ou apostrophes");
                } else {
                    tfPrenom.setStyle("-fx-border-color: green;");
                    hideTooltip(tfPrenom);
                }
            } else {
                tfPrenom.setStyle("");
                hideTooltip(tfPrenom);
            }
        });
        
        // Validation du téléphone
        tfTel.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                if (!PHONE_PATTERN.matcher(newValue).matches()) {
                    tfTel.setStyle("-fx-border-color: red;");
                    showTooltip(tfTel, "Le numéro de téléphone doit contenir exactement 8 chiffres");
                } else {
                    tfTel.setStyle("-fx-border-color: green;");
                    hideTooltip(tfTel);
                }
            } else {
                tfTel.setStyle("");
                hideTooltip(tfTel);
            }
        });
        
        // Validation de l'email
        tfEmail.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                if (!EMAIL_PATTERN.matcher(newValue).matches()) {
                    tfEmail.setStyle("-fx-border-color: red;");
                    showTooltip(tfEmail, "Format d'email invalide (exemple: nom@domaine.com)");
                } else {
                    tfEmail.setStyle("-fx-border-color: green;");
                    hideTooltip(tfEmail);
                }
            } else {
                tfEmail.setStyle("");
                hideTooltip(tfEmail);
            }
        });
        
        // Validation du mot de passe
        tfMdp.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                if (!PASSWORD_PATTERN.matcher(newValue).matches()) {
                    tfMdp.setStyle("-fx-border-color: red;");
                    showTooltip(tfMdp, "Le mot de passe doit contenir au moins 8 caractères, incluant une majuscule, une minuscule et un chiffre");
                } else {
                    tfMdp.setStyle("-fx-border-color: green;");
                    hideTooltip(tfMdp);
                }
                
                // Vérifier si les mots de passe correspondent
                if (!tfConfirmMdp.getText().isEmpty() && !tfConfirmMdp.getText().equals(newValue)) {
                    tfConfirmMdp.setStyle("-fx-border-color: red;");
                    showTooltip(tfConfirmMdp, "Les mots de passe ne correspondent pas");
                } else if (!tfConfirmMdp.getText().isEmpty()) {
                    tfConfirmMdp.setStyle("-fx-border-color: green;");
                    hideTooltip(tfConfirmMdp);
                }
            } else {
                tfMdp.setStyle("");
                hideTooltip(tfMdp);
            }
        });
        
        // Validation de la confirmation du mot de passe
        tfConfirmMdp.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                if (!newValue.equals(tfMdp.getText())) {
                    tfConfirmMdp.setStyle("-fx-border-color: red;");
                    showTooltip(tfConfirmMdp, "Les mots de passe ne correspondent pas");
                } else {
                    tfConfirmMdp.setStyle("-fx-border-color: green;");
                    hideTooltip(tfConfirmMdp);
                }
            } else {
                tfConfirmMdp.setStyle("");
                hideTooltip(tfConfirmMdp);
            }
        });
        
        // Validation du rôle
        cbRole.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                cbRole.setStyle("-fx-border-color: green;");
            } else {
                cbRole.setStyle("");
            }
        });
    }
    
    private void showTooltip(Control control, String message) {
        Tooltip tooltip = new Tooltip(message);
        tooltip.setStyle("-fx-background-color: #FFF0F0; -fx-text-fill: #D32F2F;");
        Tooltip.install(control, tooltip);
    }
    
    private void hideTooltip(Control control) {
        Tooltip.uninstall(control, null);
    }
    
    @FXML
    private void handleRegister() {
        // Réinitialiser les styles
        tfUsername.setStyle("");
        tfNom.setStyle("");
        tfPrenom.setStyle("");
        tfTel.setStyle("");
        tfEmail.setStyle("");
        tfMdp.setStyle("");
        tfConfirmMdp.setStyle("");
        cbRole.setStyle("");
        
        StringBuilder errors = new StringBuilder();
        
        // Username validation
        String username = tfUsername.getText().trim();
        if (username.isEmpty()) {
            errors.append("Le nom d'utilisateur est requis\n");
            tfUsername.setStyle("-fx-border-color: red;");
        } else if (!USERNAME_PATTERN.matcher(username).matches()) {
            errors.append("Le nom d'utilisateur doit contenir entre 3 et 20 caractères alphanumériques ou underscore (_)\n");
            tfUsername.setStyle("-fx-border-color: red;");
        }
        
        // Nom validation
        String nom = tfNom.getText().trim();
        if (nom.isEmpty()) {
            errors.append("Le nom est requis\n");
            tfNom.setStyle("-fx-border-color: red;");
        } else if (!NAME_PATTERN.matcher(nom).matches()) {
            errors.append("Le nom doit contenir entre 2 et 30 caractères alphabétiques, espaces, tirets ou apostrophes\n");
            tfNom.setStyle("-fx-border-color: red;");
        }
        
        // Prénom validation
        String prenom = tfPrenom.getText().trim();
        if (prenom.isEmpty()) {
            errors.append("Le prénom est requis\n");
            tfPrenom.setStyle("-fx-border-color: red;");
        } else if (!NAME_PATTERN.matcher(prenom).matches()) {
            errors.append("Le prénom doit contenir entre 2 et 30 caractères alphabétiques, espaces, tirets ou apostrophes\n");
            tfPrenom.setStyle("-fx-border-color: red;");
        }
        
        // Téléphone validation
        String tel = tfTel.getText().trim();
        if (tel.isEmpty()) {
            errors.append("Le numéro de téléphone est requis\n");
            tfTel.setStyle("-fx-border-color: red;");
        } else if (!PHONE_PATTERN.matcher(tel).matches()) {
            errors.append("Le numéro de téléphone doit contenir exactement 8 chiffres\n");
            tfTel.setStyle("-fx-border-color: red;");
        }
        
        // Email validation
        String email = tfEmail.getText().trim();
        if (email.isEmpty()) {
            errors.append("L'email est requis\n");
            tfEmail.setStyle("-fx-border-color: red;");
        } else if (!EMAIL_PATTERN.matcher(email).matches()) {
            errors.append("Format d'email invalide (exemple: nom@domaine.com)\n");
            tfEmail.setStyle("-fx-border-color: red;");
        }
        
        // Password validation
        String password = tfMdp.getText();
        if (password.isEmpty()) {
            errors.append("Le mot de passe est requis\n");
            tfMdp.setStyle("-fx-border-color: red;");
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            errors.append("Le mot de passe doit contenir au moins 8 caractères, incluant une majuscule, une minuscule et un chiffre\n");
            tfMdp.setStyle("-fx-border-color: red;");
        }
        
        // Confirm password validation
        String confirmPassword = tfConfirmMdp.getText();
        if (confirmPassword.isEmpty()) {
            errors.append("La confirmation du mot de passe est requise\n");
            tfConfirmMdp.setStyle("-fx-border-color: red;");
        } else if (!confirmPassword.equals(password)) {
            errors.append("Les mots de passe ne correspondent pas\n");
            tfConfirmMdp.setStyle("-fx-border-color: red;");
        }
        
        // Role validation
        if (cbRole.getValue() == null) {
            errors.append("Veuillez sélectionner un type de compte\n");
            cbRole.setStyle("-fx-border-color: red;");
        }
        
        // Check if username already exists
        try {
            if (serviceUser.usernameExists(username)) {
                errors.append("Ce nom d'utilisateur est déjà utilisé\n");
                tfUsername.setStyle("-fx-border-color: red;");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Erreur lors de la vérification du nom d'utilisateur: " + e.getMessage());
            return;
        }
        
        // Check if email already exists
        try {
            if (serviceUser.emailExists(email)) {
                errors.append("Cet email est déjà utilisé\n");
                tfEmail.setStyle("-fx-border-color: red;");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Erreur lors de la vérification de l'email: " + e.getMessage());
            return;
        }
        
        if (errors.length() > 0) {
            showAlert(Alert.AlertType.WARNING, "Validation", errors.toString());
            return;
        }
        
        // Continue with registration process
        try {
            // Get role from database based on selection
            String roleCode = cbRole.getValue().equals("Conducteur") ? Role.DRIVER_CODE : Role.PASSENGER_CODE;
            Role selectedRole = null;
            
            // Find the role in database
            for (Role role : serviceRole.readAll()) {
                if (role.getCode().equals(roleCode)) {
                    selectedRole = role;
                    break;
                }
            }
            
            if (selectedRole == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Type de compte invalide");
                return;
            }
            
            // Hash the password before saving
            String hashedPassword = PasswordUtil.hashPassword(password);
            
            // Generate verification code
            String verificationCode = generateVerificationCode();

            User newUser = new User(
                0,  // ID will be generated by database
                nom,
                prenom,
                tel,
                email,
                hashedPassword, // Use the hashed password
                selectedRole,
                verificationCode
            );
            
            // Set username
            newUser.setUsername(username);
            
            // Create the user in the database
            serviceUser.create(newUser);
            
            // Generate and send QR code
            boolean qrCodeSent = Qrcode.generateAndSendQRCode(newUser);
            
            // Show success message
            String successMessage = "Inscription réussie! ";
            if (qrCodeSent) {
                successMessage += "Un code QR a été envoyé à votre adresse email.";
            } else {
                successMessage += "Mais l'envoi du code QR a échoué.";
            }
            
            showAlert(Alert.AlertType.INFORMATION, "Succès", successMessage);
            
            // Navigate to login page
            handleBackToLogin();
            
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Erreur lors de l'inscription: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleBackToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Users/LoginUser.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) tfNom.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur de navigation: " + e.getMessage());
        }
    }
    
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    private String generateVerificationCode() {
        return String.format("%06d", (int)(Math.random() * 1000000));
    }
} 