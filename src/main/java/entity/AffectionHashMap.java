package entity;

import main.Departement;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class AffectionHashMap {
    Map<Employe,Departement> myHashMap = new HashMap();
    public void ajouteEmployerDepartement(Employe e , Departement d){
        myHashMap.put(e,d);
    }
    public void afficherEmployerEtDepartement(){
   //       affichage 1  //System.out.println(myHashMap);
        for(Employe e : myHashMap.keySet()){//afichage 2
            System.out.println(e + " - " +myHashMap.get(e));

        }
        for(Map.Entry<Employe,Departement> line : myHashMap.entrySet())  //affichage 3
     System.out.println(line.getKey()+ "-"+line.getValue());
    }
    public void supprimerEmployee(Employe  e){
        myHashMap.remove(e);

    }

    public void supprimerEmployeeEtdepartement(Employe  e,Departement d) {
        myHashMap.remove(e, d);
    }
    public boolean rechercherEmployee(Employe  e){ // methode  1 fil recherche
        return  myHashMap.containsKey(e);

    }
    public boolean rechercherDepartement(Departement d){ // methode  1 fil recherche
        return  myHashMap.containsValue(d);

    }
    public TreeMap<Employe, Departement> trierMap() {
        TreeMap<Employe,Departement> myTreeMap = new TreeMap();// comparable
        //TreeMap<Employe,Departement> myTreeMap = new TreeMap(new Employe());// comapartor
      myTreeMap.putAll(myHashMap);
        return myTreeMap;
    }



    }



