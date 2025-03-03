package gui.Users;

import entities.User;
import User.ServiceUser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.embed.swing.SwingFXUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.ByteArrayOutputStream;

public class ViewProfileController implements Initializable {
    @FXML private javafx.scene.control.TextField tfUsername;
    @FXML private javafx.scene.control.TextField tfEmail;
    @FXML private javafx.scene.control.TextField tfPrenom;
    @FXML private javafx.scene.control.TextField tfNom;
    @FXML private javafx.scene.control.TextField tfTel;
    @FXML private PasswordField tfOldPassword;
    @FXML private PasswordField tfNewPassword;
    @FXML private PasswordField tfConfirmPassword;
    @FXML private Label lblFullName;
    @FXML private Label lblRole;
    @FXML private Label lblTripsCount;
    @FXML private Label lblRating;
    @FXML private Label lblReservationsCount;
    @FXML private ImageView imgProfile;
    @FXML private Button btnUploadImage;
    @FXML private Button btnExportPDF;

    private User currentUser;
    private ServiceUser serviceUser;
    private String imagePath; // Stores the path of the new profile picture

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        serviceUser = new ServiceUser();
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        updateProfileInfo();
    }

    private void updateProfileInfo() {
        if (currentUser != null) {
            tfUsername.setText(currentUser.getUsername());
            tfEmail.setText(currentUser.getEmail());
            tfPrenom.setText(currentUser.getPrenom());
            tfNom.setText(currentUser.getNom());
            tfTel.setText(currentUser.getTel());

            lblFullName.setText(currentUser.getPrenom() + " " + currentUser.getNom());
            lblRole.setText(currentUser.getRoleCode());

            lblTripsCount.setText("0");
            lblRating.setText("0.0");
            lblReservationsCount.setText("0");

            // Load profile image from database
            if (currentUser.getImagePath() != null && !currentUser.getImagePath().isEmpty()) {
                File file = new File(currentUser.getImagePath());
                if (file.exists()) {
                    imgProfile.setImage(new Image(file.toURI().toString()));
                } else {
                    System.out.println("Image file not found: " + currentUser.getImagePath());
                    setDefaultProfileImage(); // Set default image if file is missing
                }
            } else {
                System.out.println("No profile image found for user.");
                setDefaultProfileImage(); // Set default image if none exists
            }
        }
    }
    
    /**
     * Sets a default profile image when no custom image is found
     */
    private void setDefaultProfileImage() {
        try {
            // Create a simple default image programmatically
            int width = 100;
            int height = 100;
            javafx.scene.canvas.Canvas canvas = new javafx.scene.canvas.Canvas(width, height);
            javafx.scene.canvas.GraphicsContext gc = canvas.getGraphicsContext2D();
            
            // Draw a circle with user's initials
            gc.setFill(javafx.scene.paint.Color.LIGHTBLUE);
            gc.fillOval(0, 0, width, height);
            
            gc.setFill(javafx.scene.paint.Color.WHITE);
            gc.setTextAlign(javafx.scene.text.TextAlignment.CENTER);

            gc.setFont(new javafx.scene.text.Font(40));
            
            String initials = "";
            if (currentUser != null) {
                if (currentUser.getPrenom() != null && !currentUser.getPrenom().isEmpty()) {
                    initials += currentUser.getPrenom().charAt(0);
                }
                if (currentUser.getNom() != null && !currentUser.getNom().isEmpty()) {
                    initials += currentUser.getNom().charAt(0);
                }
            }
            
            if (initials.isEmpty()) {
                initials = "U"; // Default if no name is available
            }
            
            gc.fillText(initials, width / 2, height / 2);
            
            // Convert canvas to image
            javafx.scene.SnapshotParameters params = new javafx.scene.SnapshotParameters();
            params.setFill(javafx.scene.paint.Color.TRANSPARENT);
            javafx.scene.image.Image image = canvas.snapshot(params, null);
            
            imgProfile.setImage(image);
        } catch (Exception e) {
            System.out.println("Could not create default profile image: " + e.getMessage());
            imgProfile.setImage(null);
        }
    }

    @FXML
    private void handleUploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Picture");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                // Define the destination directory
                File destDir = new File("profile_images/");
                if (!destDir.exists()) {
                    destDir.mkdir();
                }

                // Copy the file to the destination directory
                File destFile = new File(destDir, selectedFile.getName());
                Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                // Update UI and store image path
                imagePath = destFile.getAbsolutePath();
                imgProfile.setImage(new Image(destFile.toURI().toString()));

                // ✅ Save the new image path to the database
                currentUser.setImagePath(imagePath);
                serviceUser.updateUserImage(currentUser.getId(), imagePath);

            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to upload image: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleSave() throws SQLException {
        StringBuilder errors = new StringBuilder();

        if (tfNewPassword.getText().length() > 0) {
            if (!tfOldPassword.getText().equals(currentUser.getMdp())) {
                errors.append("L'ancien mot de passe est incorrect\n");
            }
            if (!tfNewPassword.getText().equals(tfConfirmPassword.getText())) {
                errors.append("Les nouveaux mots de passe ne correspondent pas\n");
            }
            if (tfNewPassword.getText().length() < 6) {
                errors.append("Le nouveau mot de passe doit contenir au moins 6 caractères\n");
            }
        }

        if (errors.length() > 0) {
            showAlert(Alert.AlertType.WARNING, "Validation", errors.toString());
            return;
        }

        // Update user details
        currentUser.setUsername(tfUsername.getText());
        currentUser.setEmail(tfEmail.getText());
        currentUser.setPrenom(tfPrenom.getText());
        currentUser.setNom(tfNom.getText());
        currentUser.setTel(tfTel.getText());

        // Update password if changed
        if (tfNewPassword.getText().length() > 0) {
            currentUser.setMdp(tfNewPassword.getText());
        }

        // ✅ Ensure the new image path is saved if it has changed
        if (imagePath != null && !imagePath.isEmpty()) {
            currentUser.setImagePath(imagePath);
            serviceUser.updateUserImage(currentUser.getId(), imagePath);
        }

        serviceUser.update(currentUser); // Ensure user details are saved
        showAlert(Alert.AlertType.INFORMATION, "Succès", "Profil mis à jour avec succès!");
    }

    @FXML
    private void handleCancel() {
        updateProfileInfo();
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Users/DashboardUser.fxml"));
            Parent root = loader.load();

            DashboardUserController controller = loader.getController();
            controller.setCurrentUser(currentUser);

            Scene scene = new Scene(root);
            Stage stage = (Stage) tfUsername.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur de navigation: " + e.getMessage());
        }
    }

    @FXML
    private void handleExportPDF() {
        try {
            // Create file chooser for saving the PDF
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Enregistrer le profil en PDF");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            
            // Set default file name with user name and current date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            String defaultFileName = currentUser.getPrenom() + "_" + currentUser.getNom() + "_Profil_" + 
                                    dateFormat.format(new Date()) + ".pdf";
            fileChooser.setInitialFileName(defaultFileName);
            
            // Show save dialog
            File file = fileChooser.showSaveDialog(btnExportPDF.getScene().getWindow());
            
            if (file != null) {
                // Create PDF document
                Document document = new Document(PageSize.A4);
                PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
                document.open();
                
                // Add title
                Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.DARK_GRAY);
                Paragraph title = new Paragraph("Profil Utilisateur - Covoituni", titleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                document.add(title);
                document.add(new Paragraph(" ")); // Add space
                
                // Add profile image if available
                if (imgProfile.getImage() != null) {
                    try {
                        // Convert JavaFX image to iText image
                        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(imgProfile.getImage(), null);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ImageIO.write(bufferedImage, "png", baos);
                        com.itextpdf.text.Image pdfImage = com.itextpdf.text.Image.getInstance(baos.toByteArray());
                        
                        // Scale image
                        pdfImage.scaleToFit(100, 100);
                        pdfImage.setAlignment(Element.ALIGN_CENTER);
                        document.add(pdfImage);
                        document.add(new Paragraph(" ")); // Add space
                    } catch (Exception e) {
                        System.out.println("Erreur lors de l'ajout de l'image: " + e.getMessage());
                        // Fallback to text if image conversion fails
                        Paragraph imagePlaceholder = new Paragraph("Photo de profil de " + 
                                currentUser.getPrenom() + " " + currentUser.getNom(), 
                                new Font(Font.FontFamily.HELVETICA, 12, Font.ITALIC));
                        imagePlaceholder.setAlignment(Element.ALIGN_CENTER);
                        document.add(imagePlaceholder);
                        document.add(new Paragraph(" ")); // Add space
                    }
                } else {
                    // No image available, add placeholder text
                    Paragraph imagePlaceholder = new Paragraph("Aucune photo de profil disponible", 
                            new Font(Font.FontFamily.HELVETICA, 12, Font.ITALIC));
                    imagePlaceholder.setAlignment(Element.ALIGN_CENTER);
                    document.add(imagePlaceholder);
                    document.add(new Paragraph(" ")); // Add space
                }
                
                // Add user information
                PdfPTable table = new PdfPTable(2);
                table.setWidthPercentage(90);
                table.setSpacingBefore(10f);
                table.setSpacingAfter(10f);
                
                // Set column widths
                float[] columnWidths = {1f, 2f};
                table.setWidths(columnWidths);
                
                // Add table headers
                Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
                Font contentFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
                
                // Add user data rows
                addTableRow(table, "Nom complet", currentUser.getPrenom() + " " + currentUser.getNom(), headerFont, contentFont);
                addTableRow(table, "Nom d'utilisateur", currentUser.getUsername(), headerFont, contentFont);
                addTableRow(table, "Email", currentUser.getEmail(), headerFont, contentFont);
                addTableRow(table, "Téléphone", currentUser.getTel(), headerFont, contentFont);
                addTableRow(table, "Rôle", currentUser.getRoleCode(), headerFont, contentFont);
                addTableRow(table, "Voyages", lblTripsCount.getText(), headerFont, contentFont);
                addTableRow(table, "Évaluation", lblRating.getText(), headerFont, contentFont);
                addTableRow(table, "Réservations", lblReservationsCount.getText(), headerFont, contentFont);
                
                document.add(table);
                
                // Add footer with date
                Paragraph footer = new Paragraph("Document généré le " + 
                        new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()),
                        new Font(Font.FontFamily.HELVETICA, 8, Font.ITALIC));
                footer.setAlignment(Element.ALIGN_RIGHT);
                document.add(footer);
                
                document.close();
                
                showAlert(Alert.AlertType.INFORMATION, "Succès", 
                        "Profil exporté avec succès en PDF!\nFichier: " + file.getAbsolutePath());
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                    "Erreur lors de l'exportation du profil en PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void addTableRow(PdfPTable table, String label, String value, Font headerFont, Font contentFont) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, headerFont));
        labelCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        labelCell.setPadding(5);
        
        PdfPCell valueCell = new PdfPCell(new Phrase(value, contentFont));
        valueCell.setPadding(5);
        
        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
