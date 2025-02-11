

package main;
import entity.Employe;
import interfaces.IGestion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class SocieteArrayList implements IGestion<Employe> {
    private ArrayList<Employe> employes;

    public SocieteArrayList() {
        employes = new ArrayList<>();
    }

    @Override
    public void ajouterEmploye(Employe employe) {
        employes.add(employe);
        System.out.println("Employé ajouté : " + employe);
    }

    @Override
    public boolean rechercherEmploye(String nom) {
        for (Employe employe : employes) {
            if (employe.getNom().equalsIgnoreCase(nom)) {
                return true;
            }
        }
        return false;
    }
    @Override
    public boolean rechercherEmploye(Employe employe) {
        return employes.contains(employe);
    }

    @Override
    public void supprimerEmploye(Employe employe) {
        if (employes.remove(employe)) {
            System.out.println("Employé supprimé : " + employe);
        } else {
            System.out.println("Employé introuvable : " + employe);
        }
    }

    @Override
    public void displayEmploye() {

    }

    @Override
    public void displayEmploye(int id) {
        for (Employe employe : employes) {
            if (employe.getId() == id) {
                System.out.println("Détails de l'employé : " + employe);
                return;
            }
        }
        System.out.println("Employé avec l'ID " + id + " introuvable.");
    }

    @Override
    public void trierEmployeParId() {
        Collections.sort(employes, Comparator.comparingInt(Employe::getId));
        System.out.println("Liste des employés triée par ID :");
        afficherEmployes();
    }

    @Override
    public void trierEmployeParNomDépartementEtGrade() {
        Collections.sort(employes, Comparator
                .comparing(Employe::getNom)
                .thenComparing(Employe::getDepartement)
                .thenComparing(Employe::getGrade)
        );

        System.out.println("Liste des employés triée par nom, département et grade :");
        afficherEmployes();
    }

    @Override
    public void afficherEmployes() {
        System.out.println("Liste des employés :");
        for (Employe employe : employes) {
            System.out.println(employe);
        }
    }
}
