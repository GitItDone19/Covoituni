package entities;
import Services.ServiceReclamation;
import Services.ServiceReclamation;
import Services.ServiceReclamation;

public class Main {
    public static void main(String[] args) {
        MyConnection mc = MyConnection.getInstance();
        MyConnection mc2 =MyConnection.getInstance();;
        System.out.println(mc);
        System.out.println(mc2);

        Avis avis1 = new Avis("Excellent service !", 5, "Client123");
        System.out.println(avis1);

        Reclamation reclamation = new Reclamation("Problème de connexion",
                "Je n'arrive pas à me connecter à mon compte.",
                "user123");
        ServiceReclamation rs =new ServiceReclamation();
        try{

            rs.create(reclamation);
            System.out.println(rs.readAll());
        }catch(Exception e){
            e.printStackTrace();
        }




    }
}