# === Étape 1 : Build de l'application avec Maven ===
FROM maven:amazoncorretto AS builder
WORKDIR /build

# Copier le fichier pom.xml et télécharger les dépendances Maven
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copier le reste du code source
COPY src ./src
COPY configuration ./configuration

# Compiler l'application et créer le jar (sans tests)
RUN mvn clean package -DskipTests

# === Étape 2 : Exécution de l'application ===
FROM eclipse-temurin:23.0.2_7-jre-alpine-3.21
WORKDIR /app

# Créer un groupe et un utilisateur non-root
RUN addgroup --system appgroup && adduser --system --ingroup appgroup appuser

# Copier le jar généré depuis le builder
COPY --from=builder /build/target/*.jar app.jar

# Copier le dossier de configuration
COPY --from=builder /build/configuration ./configuration

# Définir une variable d'environnement pour le profil de production (si nécessaire)
ENV SPRING_PROFILES_ACTIVE=prod

# Exposer le port de l'application (ici 8080)
EXPOSE 8080

# Ajouter un HEALTHCHECK (assurez-vous que l'endpoint /actuator/health est activé)
HEALTHCHECK --interval=30s --timeout=3s CMD curl -f http://localhost:8080/actuator/health || exit 1

# Utiliser l'utilisateur non-root pour exécuter l'application
USER appuser

# Lancer l'application en mode « exec »
ENTRYPOINT ["java", "-jar", "app.jar"]
