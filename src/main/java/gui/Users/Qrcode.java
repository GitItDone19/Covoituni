package gui.Users;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import entities.User;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import io.github.cdimascio.dotenv.Dotenv;

public class Qrcode {
    
    private static final String QR_CODE_DIRECTORY = "qr_codes";
    
    /**
     * Generates a QR code for a user and sends it via email
     * @param user The user to generate the QR code for
     * @return true if successful, false otherwise
     */
    public static boolean generateAndSendQRCode(User user) {
        try {
            // Create QR code directory if it doesn't exist
            Path qrCodeDirPath = Paths.get(QR_CODE_DIRECTORY);
            if (!Files.exists(qrCodeDirPath)) {
                Files.createDirectories(qrCodeDirPath);
            }
            
            // Generate QR code with user information
            String qrCodeData = createUserQRData(user);
            
            // Use email as part of filename if ID is 0 (not set yet)
            String filename = user.getId() > 0 ? "user_" + user.getId() : "user_" + user.getEmail().replaceAll("[^a-zA-Z0-9]", "_");
            String qrCodePath = QR_CODE_DIRECTORY + "/" + filename + ".png";
            
            // Generate the QR code image
            boolean qrGenerated = generateQRCode(qrCodeData, qrCodePath);
            if (!qrGenerated) {
                return false;
            }
            
            // Send the QR code via email
            boolean emailSent = sendQRCodeEmail(user, qrCodePath);
            return emailSent;
            
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                    "Impossible de générer et envoyer le QR Code.", e.getMessage());
            return false;
        }
    }
    
    /**
     * Creates a string with user data to be encoded in the QR code
     * @param user The user
     * @return A formatted string with user data
     */
    private static String createUserQRData(User user) {
        return "ID: " + user.getId() + 
               "\nNom: " + user.getNom() + 
               "\nPrénom: " + user.getPrenom() + 
               "\nEmail: " + user.getEmail() + 
               "\nTéléphone: " + user.getTel() + 
               "\nType de compte: " + user.getRole().getDisplayName() +
               "\nCode de vérification: " + user.getVerificationCode();
    }
    
    /**
     * Generates a QR code image
     * @param data The data to encode
     * @param filePath The path to save the QR code image
     * @return true if successful, false otherwise
     */
    public static boolean generateQRCode(String data, String filePath) {
        try {
            // Set QR code parameters
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.MARGIN, 2);
            
            // Create QR code bit matrix
            BitMatrix matrix = new MultiFormatWriter().encode(
                    data, BarcodeFormat.QR_CODE, 300, 300, hints);
            
            // Write to file
            File qrFile = new File(filePath);
            MatrixToImageWriter.writeToPath(matrix, "PNG", qrFile.toPath());
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                    "Impossible de générer le QR Code.", e.getMessage());
            return false;
        }
    }
    
    /**
     * Sends an email with the QR code attached
     * @param user The user to send the email to
     * @param qrCodePath The path to the QR code image
     * @return true if successful, false otherwise
     */
    public static boolean sendQRCodeEmail(User user, String qrCodePath) {
        try {
            // Load email configuration from .env file
            Dotenv dotenv = Dotenv.load();
            final String username = dotenv.get("EMAIL_USERNAME");
            final String password = dotenv.get("EMAIL_PASSWORD");
            final String host = dotenv.get("EMAIL_HOST", "smtp.gmail.com");
            final String port = dotenv.get("EMAIL_PORT", "587");
            
            // Set mail properties
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", port);
            
            // Create session with authenticator
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });
            
            // Create message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
            message.setSubject("Bienvenue sur Covoituni - Votre QR Code d'inscription");
            
            // Create multipart message
            Multipart multipart = new MimeMultipart();
            
            // Text part
            BodyPart messageBodyPart = new MimeBodyPart();
            String htmlContent = "<html><body>"
                    + "<h2>Bienvenue sur Covoituni, " + user.getPrenom() + " " + user.getNom() + "!</h2>"
                    + "<p>Merci de vous être inscrit sur notre plateforme de covoiturage universitaire.</p>"
                    + "<p>Votre compte a été créé avec succès. Voici votre QR code d'inscription qui contient vos informations.</p>"
                    + "<p>Vous pouvez présenter ce QR code lors de vos trajets pour faciliter la vérification.</p>"
                    + "<p>Votre code de vérification est: <strong>" + user.getVerificationCode() + "</strong></p>"
                    + "<p>À bientôt sur Covoituni!</p>"
                    + "<p>L'équipe Covoituni</p>"
                    + "</body></html>";
            messageBodyPart.setContent(htmlContent, "text/html; charset=utf-8");
            multipart.addBodyPart(messageBodyPart);
            
            // Attachment part
            messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(qrCodePath);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName("Covoituni_QRCode.png");
            multipart.addBodyPart(messageBodyPart);
            
            // Set content
            message.setContent(multipart);
            
            // Send message
            Transport.send(message);
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                    "Impossible d'envoyer l'email avec le QR Code.", e.getMessage());
            return false;
        }
    }
    
    /**
     * Displays a QR code in a dialog
     * @param filePath The path to the QR code image
     */
    public static void displayQRCode(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                showAlert(Alert.AlertType.ERROR, "Erreur", 
                        "Le fichier QR Code n'existe pas.", "");
                return;
            }
            
            Image image = new Image(file.toURI().toString());
            ImageView qrImageView = new ImageView(image);
            qrImageView.setFitWidth(250);
            qrImageView.setFitHeight(250);
            
            Alert qrAlert = new Alert(Alert.AlertType.INFORMATION);
            qrAlert.setTitle("QR Code Utilisateur");
            qrAlert.setHeaderText("Votre QR code d'inscription");
            qrAlert.setGraphic(qrImageView);
            qrAlert.showAndWait();
            
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                    "Impossible d'afficher le QR Code.", e.getMessage());
        }
    }
    
    /**
     * Shows an alert dialog
     * @param type The alert type
     * @param title The alert title
     * @param header The alert header
     * @param content The alert content
     */
    private static void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
