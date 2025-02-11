package entity;

import java.util.Objects;

public class Employe {
    private int identifiant;
    private String nom;
    private String prenom;
    private String Nom_dep;
    private int grade;

    public Employe() {
    }

    public Employe(int identifiant, String nom, String prenom, String nom_dep, int grade) {
        this.identifiant = identifiant;
        this.nom = nom;
        this.prenom = prenom;
        this.Nom_dep = nom_dep;
        this.grade = grade;
    }

    public Employe(String nom, int identifiant) {
    }

    public int getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(int identifiant) {
        this.identifiant = identifiant;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom_dep() {
        return Nom_dep;
    }

    public void setNom_dep(String nom_dep) {
        Nom_dep = nom_dep;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employe employe = (Employe) o;
        return identifiant == employe.identifiant && Objects.equals(nom, employe.nom);
    }


    @Override
    public String toString() {
        return "Employe{" +
                "identifiant=" + identifiant +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", Nom_dep='" + Nom_dep + '\'' +
                ", grade=" + grade +
                '}';
    }

    public int getId() {
        return identifiant;
    }

    public <U> U getDepartement() {
        return (U) Nom_dep;
    }
}


