package entities;
import Services.ServiceReclamation;
import utils.MyConnection;

public class Main {
    public static void main(String[] args) {
        MyConnection mc = MyConnection.getInstance();
        System.out.println("Testing database connection...");

        // Create a new reclamation
        Reclamation reclamation = new Reclamation(
            "Problème de connexion",  // description
            "EN_COURS",               // status
            1                         // userId
        );

        ServiceReclamation rs = new ServiceReclamation();
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