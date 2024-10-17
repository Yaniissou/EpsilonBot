# Étape 1 : Construire le projet
FROM maven:3.8.4-openjdk-17 AS build

# Définir le répertoire de travail
WORKDIR /app

# Copier le fichier pom.xml et télécharger les dépendances
COPY pom.xml .
RUN mvn dependency:go-offline

# Copier le reste des fichiers du projet
COPY src ./src

# Construire l'application
RUN mvn package -DskipTests

# Étape 2 : Créer une image d'exécution légère
FROM openjdk:17-jdk-slim

# Créer un répertoire pour l'application
WORKDIR /app

# Copier le fichier JAR depuis l'étape de build
COPY --from=build /app/target/EpsilonBot-1.0-SNAPSHOT.jar /app/epsilonbot.jar

# Définir la commande pour exécuter le bot
CMD ["java", "-jar", "/app/epsilonbot.jar"]
