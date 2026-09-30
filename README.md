# SGE-IHEC — Système de Gestion des Évaluations

Application web Spring Boot de gestion et de centralisation des évaluations (DS et examens) et de leurs corrigés à l'**IHEC Carthage**.

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.6-brightgreen)
![MySQL](https://img.shields.io/badge/MySQL-8-blue)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-MVC-green)

## Contexte

Les sujets d'examens et les corrigés sont souvent dispersés et difficiles d'accès pour les étudiants. SGE-IHEC offre un espace unique où les professeurs déposent et gèrent les évaluations, et où les étudiants les consultent et les téléchargent, avec une séparation stricte des rôles.

## Fonctionnalités

| Rôle | Fonctionnalités |
|---|---|
| **Professeur** | Ajouter, modifier et supprimer un DS ou un examen · joindre un corrigé (PDF/Word) · saisir un corrigé textuel · créer une matière à la volée |
| **Étudiant** | Consulter les évaluations · filtrer par filière, niveau et matière · télécharger les fichiers · lire le corrigé textuel |
| **Système** | Inscription et connexion par rôle · sessions HTTP · redirection selon le rôle · suppression du fichier associé à la suppression d'une évaluation |

## Technologies

- **Java 21**, **Spring Boot 4.0.6** (Web MVC)
- **Spring Data JPA / Hibernate** (héritage `SINGLE_TABLE`)
- **MySQL 8**
- **Thymeleaf**, HTML/CSS/JavaScript
- **Jakarta Validation**, **Maven**

## Principes POO appliqués

- **Encapsulation** : attributs privés/protégés avec getters et setters
- **Héritage** : `DS` et `Examen` héritent de la classe abstraite `Evaluation`
- **Polymorphisme** : méthode abstraite `getType()` redéfinie dans chaque sous-classe
- **Abstraction** : `Evaluation` ne peut pas être instanciée directement

## Architecture

```
src/main/java/com/ihec/gestion/
├── model/        Entités JPA (Evaluation, DS, Examen, Compte, Matiere, TypeUtilisateur)
├── repository/   Interfaces JpaRepository
├── service/      Logique métier
├── controller/   Contrôleurs Spring MVC
├── DataInitializer.java
└── GestionEvaluationsApplication.java
src/main/resources/
├── templates/    Pages Thymeleaf (login, inscrire, index, prof, étudiant)
├── static/       CSS et JavaScript
└── application.properties
```

Architecture en couches : **Vue → Controller → Service → Repository → Modèle**.

## Installation

### Prérequis

- JDK 21
- MySQL 8 (WAMP, MAMP ou installation standalone)
- Maven (ou le wrapper `mvnw` fourni)

### Lancer l'application

1. Cloner le dépôt :
   ```bash
   git clone https://github.com/farah-jemal/MyJavaProject.git
   cd MyJavaProject
   ```
2. Démarrer MySQL. La base `gestion_evaluations` est créée automatiquement au premier lancement (`createDatabaseIfNotExist=true`).
3. Adapter si besoin `src/main/resources/application.properties` (utilisateur et mot de passe MySQL, par défaut `root` sans mot de passe).
4. Lancer :
   ```bash
   ./mvnw spring-boot:run
   ```
   Sous Windows : `mvnw.cmd spring-boot:run`
5. Ouvrir http://localhost:8081

> Le fichier `gestion_evaluations.sql` est fourni à titre optionnel pour recréer la base manuellement. **Attention : il commence par `DROP DATABASE`**, ce qui supprime la base existante.

## Comptes de démonstration

Créés automatiquement au premier démarrage si la base est vide :

| Rôle | Email | Mot de passe |
|---|---|---|
| Étudiant | `etudiant@ihec.tn` | `etudiant123` |
| Professeur | `prof@ihec.tn` | `prof123` |

Ces comptes sont réservés à la démonstration.

## Routes principales

| Méthode | Route | Description |
|---|---|---|
| GET/POST | `/comptes/login` | Connexion |
| GET/POST | `/comptes/inscrire` | Inscription |
| GET | `/comptes/logout` | Déconnexion |
| GET | `/evaluations/etudiant` | Vue étudiant |
| GET | `/evaluations/prof` | Vue professeur |
| POST | `/evaluations/ajouter` | Ajouter une évaluation |
| GET/POST | `/evaluations/modifier/{id}` | Modifier une évaluation |
| GET | `/evaluations/supprimer/{id}` | Supprimer une évaluation |
| GET | `/evaluations/fichier/{id}` | Télécharger le fichier joint |

Les fichiers téléversés sont stockés dans le dossier `corriges/` (20 Mo maximum par fichier).

## Perspectives

- Spring Security (JWT, OAuth2) et hachage des mots de passe
- Notifications par e-mail
- API REST documentée (Swagger/OpenAPI)
- Statistiques sur le tableau de bord
- Stockage cloud des fichiers

## Équipe DevCore

Projet de POO — IHEC Carthage, 2025/2026.

- Islem Gatri
- Farah Jemal
- Sabrine Masmoudi
- Sarra Naimi
- Nadine Messai
