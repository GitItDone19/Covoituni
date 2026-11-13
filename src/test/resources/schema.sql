CREATE TABLE role (
    id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    display_name VARCHAR(100)
);

CREATE TABLE utilisateur (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100),
    prenom VARCHAR(100),
    tel VARCHAR(20),
    email VARCHAR(150) NOT NULL UNIQUE,
    mdp VARCHAR(255),
    role_code VARCHAR(50),
    verificationcode VARCHAR(50),
    rating DOUBLE DEFAULT 5.0,
    trips_count INT DEFAULT 0,
    username VARCHAR(100)
);

CREATE TABLE avis (
    id INT AUTO_INCREMENT PRIMARY KEY,
    commentaire VARCHAR(500),
    rating INT,
    reponse_avis VARCHAR(500),
    date_avis TIMESTAMP,
    user_id INT
);

CREATE TABLE reclamation (
    id INT AUTO_INCREMENT PRIMARY KEY,
    description VARCHAR(500),
    status VARCHAR(20),
    `date` DATE,
    user_id INT,
    admin_reply VARCHAR(500)
);
