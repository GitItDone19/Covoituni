package entities;

import java.sql.Timestamp;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author user
 */
public class User {

    private int id;
    private String username;
    private String nom;
    private String prenom;
    private String tel;
    private String email;
    private String mdp;
    private String roleCode;
    private String verificationcode;
    private double rating;
    private int tripsCount;
    private Timestamp createdAt;

    public User() {
        this.rating = 5.0;
        this.tripsCount = 0;
    }

    public User(String username, String nom, String prenom, String tel, String email, 
                String mdp, String roleCode, String verificationcode) {
        this.username = username;
        this.nom = nom;
        this.prenom = prenom;
        this.tel = tel;
        this.email = email;
        this.mdp = mdp;
        this.roleCode = roleCode;
        this.verificationcode = verificationcode;
        this.rating = 5.0;
        this.tripsCount = 0;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getTel() {
        return tel;
    }

    public String getEmail() {
        return email;
    }

    public String getMdp() {
        return mdp;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public String getVerificationcode() {
        return verificationcode;
    }

    public double getRating() {
        return rating;
    }

    public int getTripsCount() {
        return tripsCount;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public void setVerificationcode(String verificationcode) {
        this.verificationcode = verificationcode;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setTripsCount(int tripsCount) {
        this.tripsCount = tripsCount;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", tel='" + tel + '\'' +
                ", email='" + email + '\'' +
                ", roleCode='" + roleCode + '\'' +
                ", rating=" + rating +
                ", tripsCount=" + tripsCount +
                '}';
    }
}
