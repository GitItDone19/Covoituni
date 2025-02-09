package main;

import Services.ServiceUser;
import Services.TrajetService;
import entities.MyConnection;
import entities.Trajet;
import entities.User;

import java.sql.Date;

public class Main {
    public Main() {
    }

    public static void main(String[] args) {
        MyConnection mc = MyConnection.getInstance();
        MyConnection mc2 = MyConnection.getInstance();
        System.out.println(mc);
        System.out.println(mc2);
        User user = new User(1, "Smith", "John", "123", "john.smith@example.com", "password123", "conducteur", "ABC123");
        ServiceUser s = new ServiceUser();
        TrajetService ts = new TrajetService();

        try {
            Trajet trajet = new Trajet(1, 3, (double)15.5F, new Date(System.currentTimeMillis()));
            s.ajouter(user);
            ts.ajouter(trajet);
            System.out.println(s.afficherAll());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
