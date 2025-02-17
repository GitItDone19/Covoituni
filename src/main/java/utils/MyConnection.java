   package utils;

   import java.sql.Connection;
   import java.sql.DriverManager;
   import java.sql.SQLException;

   public class MyConnection {
       private static MyConnection instance;
       private Connection cnx;

       private MyConnection() {
           try {
               String url = "jdbc:mysql://localhost:3306/pidev?useSSL=false";
               String username = "root";
               String password = "";
               cnx = DriverManager.getConnection(url, username, password);
               System.out.println("Connected to database successfully!");
           } catch (SQLException e) {
               System.err.println("Database Connection Error: " + e.getMessage());
               e.printStackTrace();
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
   }