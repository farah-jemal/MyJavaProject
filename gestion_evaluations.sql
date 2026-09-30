DROP DATABASE IF EXISTS gestion_evaluations;

CREATE DATABASE gestion_evaluations
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE gestion_evaluations;

-- Table des comptes utilisateurs
CREATE TABLE IF NOT EXISTS comptes (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    email        VARCHAR(100) NOT NULL UNIQUE,
    mot_de_passe VARCHAR(255) NOT NULL,
    type         ENUM('ETUDIANT', 'PROF') NOT NULL
);

-- Table des matières
CREATE TABLE IF NOT EXISTS matieres (
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom     VARCHAR(100) NOT NULL,
    niveau  VARCHAR(20)  NOT NULL,
    filiere VARCHAR(100) NOT NULL
);

-- Table des évaluations (DS et Examens)
CREATE TABLE IF NOT EXISTS evaluations (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    type_eval      VARCHAR(20)  NOT NULL,
    titre          VARCHAR(200) NOT NULL,
    chemin_fichier VARCHAR(500),
    matiere_id     BIGINT NOT NULL,
    FOREIGN KEY (matiere_id) REFERENCES matieres(id) ON DELETE CASCADE
);

-- Données de test
INSERT INTO comptes (email, mot_de_passe, type) VALUES
    ('etudiant@ihec.tn', 'etudiant123', 'ETUDIANT'),
    ('prof@ihec.tn', 'prof123', 'PROF')
ON DUPLICATE KEY UPDATE email = email;

INSERT INTO matieres (nom, niveau, filiere) VALUES
    ('POO', '2LIG', 'BI'),
    ('Base de données', '2LIG', 'BI'),
    ('Algorithmes', '1LIG', 'BI'),
    ('Comptabilité', '1LIG', 'BI');

INSERT INTO evaluations (type_eval, titre, matiere_id) VALUES
    ('DS', 'DS1 POO ', 1),
    ('DS', 'DS1 Base de données', 2),
    ('Examen', 'Examen final POO', 1),
    ('Examen', 'Examen final Algorithmes', 3);