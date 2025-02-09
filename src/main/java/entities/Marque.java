package entities;

public class Marque {
    private int id;
    private String logo;
    private String nom;

    public Marque(int id, String logo, String nom) {
        this.id = id;
        this.logo = logo;
        this.nom = nom;
    }

    public Marque(String logo, String nom) {
        this.logo = logo;
        this.nom = nom;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getLogo() { return logo; }
    public void setLogo(String logo) { this.logo = logo; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    @Override
    public String toString() {
        return "Marque{id=" + id + ", logo='" + logo + "', nom='" + nom + "'}";
    }
}
