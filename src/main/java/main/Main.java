package main;

import java.sql.Date;
<<<<<<< HEAD

import Services.TrajetService;
import entities.MyConnection;
import entities.Trajet;
=======
import Services.ServiceUser;
import entities.User;
>>>>>>> gestion_user


public class Main {
    public static void main(String[] args) {
        MyConnection mc = MyConnection.getInstance();
        MyConnection mc2 =MyConnection.getInstance();;
        System.out.println(mc);
        System.out.println(mc2);



<<<<<<< HEAD



        TrajetService ts = new TrajetService();
        try{
            Trajet trajet = new Trajet(2, 3, 15.5, new Date(System.currentTimeMillis()));

            ts.ajouter(trajet);
=======
        User user = new User(2, "Smith", "John", "123", "john.smith@example.com", "password123", "conducteur", "ABC123");


        ServiceUser s = new ServiceUser();

        try{

            s.ajouter(user);

>>>>>>> gestion_user
            

        }catch(Exception e){
            e.printStackTrace();
        }




    }
}