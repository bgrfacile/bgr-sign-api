# Student Attendance Tracker Backend

## Description

Ce projet constitue le backend d'une application de suivi de présence des étudiants. Il permet de gérer l'authentification, l'autorisation (avec plusieurs rôles et permissions) ainsi que l'ensemble des fonctionnalités de suivi et de gestion de l'assiduité étudiante via une API REST.

## Configuration de la base de données

Le projet utilise PostgreSQL comme base de données. Un fichier `docker-compose.yml` est fourni pour lancer une instance de PostgreSQL rapidement.

## Migrations Flyway

Les migrations de la base de données sont gérées par Flyway. Les scripts de migration se trouvent dans le dossier `src/main/resources/db/migration`.

### Commandes Flyway utiles

- **`mvn flyway:info`**  
  Affiche l'état actuel des migrations (version appliquées, en attente, ou en erreur).

- **`mvn flyway:migrate`**  
  Exécute les migrations non encore appliquées sur la base de données.

- **`mvn flyway:validate`**  
  Vérifie la cohérence entre les scripts de migration et l'état actuel de la base de données.

- **`mvn flyway:clean`**  
  Supprime toutes les tables du schéma de base de données. **Attention :** commande destructive, à utiliser uniquement en environnement de développement.

- **`mvn flyway:baseline`**  
  Définit un point de départ pour une base de données existante non encore gérée par Flyway.

- **`mvn flyway:repair`**  
  Corrige la table de métadonnées de Flyway en supprimant ou en réparant les enregistrements erronés.

*Note :* La commande `flyway:undo` est réservée aux éditions Teams/Enterprise de Flyway.


   