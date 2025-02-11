package main;

import java.util.Objects;

public class Departement  implements Comparable<Departement> {
    private int id;
    private String dep;
    private int employes;
    private String Nom;


    public Departement() {
    }

    public Departement(int id, String dep, int employes) {
        this.id = id;
        this.dep = dep;
        this.employes = employes;
    }

    public Departement(int i, String informatique) {
    }

    public static void displayDepartement() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDep() {
        return dep;
    }

    public void setDep(String dep) {
        this.dep = dep;
    }

    public int getEmployes() {
        return employes;
    }

    public void setEmployes(int employes) {
        this.employes = employes;
    }

    @Override
    public int compareTo(Departement other) {
        return Integer.compare(this.id, other.id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Departement that = (Departement) o;
        return id == that.id && Objects.equals(dep, that.dep);
    }

    @Override
    public String toString() {
        return "Departement{" +
                "id=" + id +
                ", dep='" + dep + '\'' +
                ", employes=" + employes +
                '}';
    }

    public String getNom() {
        return Nom;
    }

    public void setNom(String nom) {
        Nom = nom;
    }
}