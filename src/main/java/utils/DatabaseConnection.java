package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    
    private DatabaseConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/covoituni";
            String user = "root";         // Remplacé par votre utilisateur MySQL
            String password = "";         // Remplacé par votre mot de passe MySQL
            
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connexion à la base de données établie avec succès!");
        } catch (SQLException e) {
            throw new RuntimeException("Erreur de connexion à la base de données: " + e.getMessage(), e);
        }
    }
    
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    public Connection getConnection() {
        return connection;
    }
} 