package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//Singleton Design Pattern
public class MyConnection {

    private static MyConnection instance;
    private Connection cnx;

    private MyConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/PI";
            String user = "root";
            String password = "";
            cnx = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to database successfully!");
        } catch (SQLException e) {
            System.err.println("Error connecting to database: " + e.getMessage());
        }
    }

    public static MyConnection getInstance() {
        if (instance == null) {
            instance = new MyConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return cnx;
    }
}
