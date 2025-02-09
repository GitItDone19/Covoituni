package entities;

import Services.ServiceUser;

public class Main {
    public static void main(String[] args) {
        MyConnection mc = MyConnection.getInstance();
        MyConnection mc2 =MyConnection.getInstance();;
        System.out.println(mc);
        System.out.println(mc2);

        User user = new User(1, "Smith", "John", "123", "john.smith@example.com", "password123", "conducteur", "ABC123");
        ServiceUser s = new ServiceUser();
        try{
            s.ajouter(user);
            
            System.out.println(s.afficherAll());
        }catch(Exception e){
            e.printStackTrace();
        }




    }
}