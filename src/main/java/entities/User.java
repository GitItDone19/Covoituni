package entities;

public class User {
    private int id;
    private String nom;
    private String prenom;
    private String tel;
    private String email;
    private String mdp;
    private Role role;
    private String verificationCode;
    private double rating;
    private int tripsCount;

    // Default constructor
    public User() {
        this.rating = 5.0;
        this.tripsCount = 0;
    }

    // Constructor for creating new user
    public User(String nom, String prenom, String tel, String email, String mdp, Role role) {
        this.nom = nom;
        this.prenom = prenom;
        this.tel = tel;
        this.email = email;
        this.mdp = mdp;
        this.role = role;
        this.rating = 5.0;
        this.tripsCount = 0;
    }

    // Constructor for database retrieval
    public User(int id, String nom, String prenom, String tel, String email, String mdp, Role role, String verificationCode, double rating, int tripsCount) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.tel = tel;
        this.email = email;
        this.mdp = mdp;
        this.role = role;
        this.verificationCode = verificationCode;
        this.rating = rating;
        this.tripsCount = tripsCount;
    }

    // Getters
    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getTel() { return tel; }
    public String getEmail() { return email; }
    public String getMdp() { return mdp; }
    public Role getRole() { return role; }
    public String getVerificationCode() { return verificationCode; }
    public double getRating() { return rating; }
    public int getTripsCount() { return tripsCount; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setTel(String tel) { this.tel = tel; }
    public void setEmail(String email) { this.email = email; }
    public void setMdp(String mdp) { this.mdp = mdp; }
    public void setRole(Role role) { this.role = role; }
    public void setVerificationCode(String verificationCode) { this.verificationCode = verificationCode; }
    public void setRating(double rating) { this.rating = rating; }
    public void setTripsCount(int tripsCount) { this.tripsCount = tripsCount; }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", tel='" + tel + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", rating=" + rating +
                ", tripsCount=" + tripsCount +
                '}';
    }
} 