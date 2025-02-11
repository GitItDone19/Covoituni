package main;

import interfaces.IDepartement;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class DepartementHashSet implements IDepartement<IDepartement> {
    private Set<Departement> departements;

    public DepartementHashSet() {
        this.departements = new HashSet<>();
    }

    @Override
    public void ajouterDepartement(Departement t) {
        departements.add(t);
    }

    @Override
    public void ajouterDepartement(IDepartement iDepartement) {

    }

    @Override
    public boolean rechercherDepartement(String nom) {
        for (Departement dept : departements) {
            if (dept.getNom().equalsIgnoreCase(nom)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean rechercherDepartement(IDepartement iDepartement) {
        return false;
    }

    @Override
    public void supprimerDepartement(IDepartement iDepartement) {

    }

    @Override
    public boolean rechercherDepartement(Departement t) {
        return departements.contains(t);
    }

    @Override
    public void supprimerDepartement(Departement t) {
        departements.remove(t);
    }

    @Override
    public void displayDepartement() {
        if (departements.isEmpty()) {
            System.out.println("Aucun département à afficher.");
        } else {
            System.out.println("Liste des départements :");
            for (Departement dept : departements) {
                System.out.println(dept);
            }
        }
    }

    @Override
    public TreeSet<IDepartement> trierDepartementById() {
        return new TreeSet (departements);
    }
}

