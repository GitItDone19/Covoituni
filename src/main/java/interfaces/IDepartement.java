package interfaces;

import main.Departement;

import java.util.TreeSet;

public interface IDepartement<T>{
    void ajouterDepartement(Departement t);

    public void ajouterDepartement(T t);
    public boolean rechercherDepartement(String nom);
    public boolean rechercherDepartement(T t);
    public void supprimerDepartement(T t);

    boolean rechercherDepartement(Departement t);

    void supprimerDepartement(Departement t);

    public void displayDepartement();
    public TreeSet<T> trierDepartementById();
}
