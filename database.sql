-- Création de la base de données
CREATE DATABASE IF NOT EXISTS gestion_voitures;
USE gestion_voitures;

-- Création de la table categorie
CREATE TABLE IF NOT EXISTS categorie (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    description TEXT
);

-- Création de la table car
CREATE TABLE IF NOT EXISTS car (
    id INT PRIMARY KEY AUTO_INCREMENT,
    plaqueImatriculation VARCHAR(20) NOT NULL UNIQUE,
    description TEXT,
    dateImatriculation DATE NOT NULL,
    couleur VARCHAR(50) NOT NULL,
    marque VARCHAR(50) NOT NULL,
    modele VARCHAR(50) NOT NULL,
    categorie_id INT NOT NULL,
    FOREIGN KEY (categorie_id) REFERENCES categorie(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- Insertion de quelques catégories de test
INSERT INTO categorie (nom, description) VALUES
('Berline', 'Voiture à coffre fermé, traditionnellement à 3 volumes'),
('SUV', 'Sport Utility Vehicle, véhicule utilitaire sport'),
('Citadine', 'Petite voiture adaptée à la ville'),
('Break', 'Voiture familiale avec un grand coffre'),
('Coupé', 'Voiture sportive à 2 portes');

-- Insertion de quelques voitures de test
INSERT INTO car (plaqueImatriculation, description, dateImatriculation, couleur, marque, modele, categorie_id) VALUES
('123ABC456', 'Voiture en très bon état', '2020-01-15', 'Noir', 'Peugeot', '308', 1),
('789XYZ012', 'Première main', '2021-06-22', 'Blanc', 'Renault', 'Captur', 2),
('456DEF789', 'Faible kilométrage', '2019-11-30', 'Rouge', 'Citroën', 'C3', 3); 