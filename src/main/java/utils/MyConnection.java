package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {
    private static MyConnection instance;
    private Connection cnx;

    private MyConnection() {
        String url = "jdbc:mysql://localhost:3307/jdbcdemo";
        String user = "root";
        String password = "";

        try {
            // Register JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Create connection
            cnx = DriverManager.getConnection(url, user, password);
            System.out.println("Database connection established successfully!");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static MyConnection getInstance() {
        if (instance == null) {
            instance = new MyConnection();
        }
        return instance;
    }

    public Connection getCnx() {
        return cnx;
    }

    public void setCnx(Connection cnx) {
        this.cnx = cnx;
    }
}
