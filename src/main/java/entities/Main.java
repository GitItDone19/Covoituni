package entities;
import utils.MyConnection;
import Services.Reclamation.ReclamationService;

public class Main {
    public static void main(String[] args) {
        MyConnection mc = MyConnection.getInstance();
        System.out.println("Testing database connection...");

        // Create a test user first
        User user = new User();
        user.setId(1);
        user.setNom("Test");
        user.setPrenom("User");
        user.setEmail("test@example.com");

        // Create a new reclamation with User object
        Reclamation reclamation = new Reclamation(
            "Problème de connexion",  // description
            "EN_COURS",               // status
            user                      // user object instead of userId
        );

        ReclamationService rs = new ReclamationService();
        try {
            rs.create(reclamation);
            System.out.println("Reclamation created successfully!");
            System.out.println("All reclamations:");
            System.out.println(rs.readAll());
        } catch(Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}