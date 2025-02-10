package entities;

import java.sql.Connection;
import java.sql.DriverManager;

public class MyConnection {
    private static MyConnection connection;
    private Connection cnx;

    private MyConnection(){
        String url="jdbc:mysql://localhost:3306/PI";
        String user = "root";
        String password = "";

        try {
            cnx = DriverManager.getConnection(url,user,password);
            System.out.println("connexion etabli :)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static MyConnection getInstance(){
        if (connection == null){
            connection = new MyConnection();
        }
        return connection;
    }

    public static MyConnection getConnection() {
        return connection;
    }

    public static void setConnection(MyConnection connection) {
        MyConnection.connection = connection;
    }

    public Connection getCnx() {
        return cnx;
    }

    public void setCnx(Connection cnx) {
        this.cnx = cnx;
    }
}