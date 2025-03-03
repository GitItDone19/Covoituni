package gui.Users;

import Services.ServiceUser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.commons.mail.EmailException;
import utils.PasswordUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Random;

public class ForgotPasswordController {

    @FXML private TextField tfEmail;
    private ServiceUser serviceUser;

    public void initialize() {
        serviceUser = new ServiceUser();
    }

    @FXML
    private void handleResetPassword() throws SQLException {
        String email = tfEmail.getText().trim();

        if (email.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Email invalide", "Veuillez entrer une adresse email valide.");
            return;
        }

        if (!serviceUser.emailExists(email)) {
            showAlert(Alert.AlertType.ERROR, "Email introuvable", "Cette adresse email n'est pas enregistrée.");
            return;
        }

        // Générer un mot de passe aléatoire
        String newPassword = generateRandomPassword(10);
        
        // Hasher le mot de passe pour le stockage
        String hashedPassword = PasswordUtil.hashPassword(newPassword);
        
        // Mettre à jour le mot de passe dans la base de données
        if (updateUserPassword(email, hashedPassword)) {
            // Envoyer le nouveau mot de passe par email
            try {
                sendPasswordEmail(email, newPassword);
                showAlert(Alert.AlertType.INFORMATION, "Mot de passe envoyé", 
                          "Un nouveau mot de passe a été envoyé à votre adresse email.");
            } catch (EmailException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur d'envoi", 
                          "Impossible d'envoyer l'email. Veuillez réessayer.");
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur de mise à jour", 
                      "Impossible de mettre à jour votre mot de passe. Veuillez réessayer.");
        }
    }
    
    /**
     * Génère un mot de passe aléatoire avec des lettres et des chiffres
     * @param length Longueur du mot de passe
     * @return Mot de passe généré
     */
    private String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        
        return sb.toString();
    }
    
    /**
     * Met à jour le mot de passe de l'utilisateur dans la base de données
     * @param email Email de l'utilisateur
     * @param hashedPassword Mot de passe hashé
     * @return true si la mise à jour a réussi, false sinon
     */
    private boolean updateUserPassword(String email, String hashedPassword) {
        try {
            // Utiliser une requête SQL pour mettre à jour directement le mot de passe
            String sql = "UPDATE utilisateur SET mdp = ? WHERE email = ?";
            java.sql.Connection connection = utils.MyConnection.getInstance().getCnx();
            java.sql.PreparedStatement pst = connection.prepareStatement(sql);
            pst.setString(1, hashedPassword);
            pst.setString(2, email);
            
            int rowsUpdated = pst.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void sendPasswordEmail(String email, String newPassword) throws EmailException {
        org.apache.commons.mail.HtmlEmail mail = new org.apache.commons.mail.HtmlEmail();
        mail.setHostName("smtp.gmail.com");
        mail.setSmtpPort(587);
        
        // Remplacer par vos identifiants Gmail
        mail.setAuthentication("khairibouzid95@gmail.com", "ohdv puqy vwqg hoou");
        mail.setStartTLSRequired(true);
        
        mail.setFrom("khairibouzid95@gmail.com", "Covoituni - Réinitialisation de mot de passe");
        mail.setSubject("Votre nouveau mot de passe Covoituni");

        // Corps de l'email
        String htmlMsg = "<html><body style='font-family: Arial, sans-serif; color: #333;'>"
                + "<div style='max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 5px;'>"
                + "<h2 style='color: #28444c;'>Réinitialisation de mot de passe</h2>"
                + "<p>Bonjour,</p>"
                + "<p>Vous avez demandé la réinitialisation de votre mot de passe pour votre compte Covoituni.</p>"
                + "<p>Voici votre nouveau mot de passe temporaire :</p>"
                + "<div style='background-color: #f5f5f5; padding: 15px; border-radius: 5px; margin: 15px 0; font-family: monospace; font-size: 18px;'>"
                + newPassword
                + "</div>"
                + "<p>Nous vous recommandons de changer ce mot de passe dès votre prochaine connexion.</p>"
                + "<p>Si vous n'avez pas demandé cette réinitialisation, veuillez contacter notre support immédiatement.</p>"
                + "<p style='margin-top: 30px;'>Cordialement,<br>L'équipe Covoituni</p>"
                + "</div></body></html>";

        mail.setHtmlMsg(htmlMsg);
        mail.addTo(email);
        mail.send();

        System.out.println("Email avec nouveau mot de passe envoyé à : " + email);
    }

    @FXML
    private void handleBackToLogin() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Users/LoginUser.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) tfEmail.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de navigation", 
                      "Erreur lors de la navigation vers la page de connexion : " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
