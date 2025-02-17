package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe singleton gérant la connexion à la base de données
 * Utilise le pattern Singleton pour assurer une seule instance de connexion
 */
public class MyConnection {
    // Paramètres de connexion à la base de données
    private static final String URL = "jdbc:mysql://localhost:3306/covoituni";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    
    private Connection connection;  // Instance de connexion
    private static MyConnection instance;  // Instance unique de la classe

    /**
     * Constructeur privé qui initialise la connexion
     * Appelé une seule fois lors de la première utilisation
     */
    private MyConnection() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connexion établie!");
        } catch (SQLException e) {
            System.err.println("Erreur de connexion : " + e.getMessage());
        }
    }

    /**
     * Méthode pour obtenir l'instance unique de la connexion
     * @return L'instance unique de MyConnection
     */
    public static MyConnection getInstance() {
        if (instance == null) {
            instance = new MyConnection();
        }
        return instance;
    }

    /**
     * Récupère la connexion à la base de données
     * @return L'objet Connection actif
     */
    public Connection getConnection() {
        return connection;
    }
}
