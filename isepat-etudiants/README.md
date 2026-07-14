# API Gestion des Etudiants - ISEP-AT

API REST CRUD developpee avec Spring Boot pour la gestion des etudiants de l'ISEP-AT (TP - Dr Samba SIDIBE).

## Prerequis

- Java 17
- MySQL (serveur demarre en local, ecoutant sur le port 3306 par defaut)
- Aucune installation de Maven necessaire : le wrapper (`mvnw` / `mvnw.cmd`) est fourni

## Variables d'environnement

Le profil `dev` lit les identifiants MySQL depuis des variables d'environnement (aucun mot de passe en clair dans le code) :

| Variable | Description | Valeur par defaut |
|---|---|---|
| `DB_USERNAME` | Utilisateur MySQL | `root` |
| `DB_PASSWORD` | Mot de passe MySQL | (vide) |
| `SERVER_PORT` | Port d'ecoute de l'application | `8080` (reconnu nativement par Spring Boot) |
| `SPRING_PROFILES_ACTIVE` | Profil actif (`dev` ou `prod`) | `dev` (definit par defaut dans `application.properties`) |

Le profil `prod` utilise en plus `DB_HOST` et `DB_PORT` (voir `application-prod.properties`).

### Definir les variables (PowerShell)

```powershell
$env:DB_USERNAME = "root"
$env:DB_PASSWORD = "votre_mot_de_passe"
```

## Configuration locale

1. Copier le fichier modele et l'adapter si besoin :
   ```
   src/main/resources/application-dev.properties.example
   -> src/main/resources/application-dev.properties
   ```
2. Ce fichier `application-dev.properties` est ignore par Git (`.gitignore`) car specifique a chaque machine.

La base `isepat_dev` est creee automatiquement au demarrage si elle n'existe pas (`createDatabaseIfNotExist=true`), et le schema est genere/mis a jour par Hibernate (`ddl-auto=update`).

## Lancer l'application

Depuis la racine du projet (`isepat-etudiants/`) :

```bash
./mvnw spring-boot:run
```

Sur Windows (PowerShell/CMD) :

```powershell
.\mvnw.cmd spring-boot:run
```

L'application demarre sur `http://localhost:8080` (ou le port defini par `SERVER_PORT`).

## Documentation et tests de l'API

- Swagger UI : http://localhost:8080/swagger-ui.html
- Specification OpenAPI (JSON) : http://localhost:8080/v3/api-docs

### Endpoints disponibles

| Methode | URL | Description |
|---|---|---|
| POST | `/etudiants` | Ajouter un etudiant |
| GET | `/etudiants` | Lister les etudiants (`?tri=nom` pour trier par nom) |
| GET | `/etudiants/{id}` | Rechercher un etudiant par id |
| GET | `/etudiants/matricule/{matricule}` | Rechercher un etudiant par matricule |
| PUT | `/etudiants/{id}` | Modifier un etudiant |
| DELETE | `/etudiants/{id}` | Supprimer un etudiant |

### Codes HTTP retournes

| Situation | Code |
|---|---|
| Creation reussie | 201 Created |
| Modification reussie | 200 OK |
| Suppression reussie | 204 No Content |
| Champ obligatoire manquant | 400 Bad Request |
| Matricule deja existant | 409 Conflict |
| Email deja existant | 409 Conflict |
| Etudiant introuvable | 404 Not Found |

## Compiler / tester

```bash
./mvnw compile
./mvnw test
```
