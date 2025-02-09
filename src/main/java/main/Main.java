package main;

import java.sql.Date;

import Services.TrajetService;
import entities.MyConnection;
import entities.Trajet;


public class Main {
    public static void main(String[] args) {
        MyConnection mc = MyConnection.getInstance();
        MyConnection mc2 =MyConnection.getInstance();;
        System.out.println(mc);
        System.out.println(mc2);






        TrajetService ts = new TrajetService();
        try{
            Trajet trajet = new Trajet(2, 3, 15.5, new Date(System.currentTimeMillis()));

            ts.ajouter(trajet);
            

        }catch(Exception e){
            e.printStackTrace();
        }




    }
}