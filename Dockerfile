# Utiliser une image Java officielle
FROM openjdk:17-jdk-slim

# Définir le répertoire de travail
WORKDIR /app

# Copier le fichier JAR
COPY target/EpsilonBot-1.0-SNAPSHOT.jar /app/EpsilonBot-1.0-SNAPSHOT.jar

# Définir la commande pour exécuter le bot
CMD ["java", "-jar", "/app/EpsilonBot-1.0-SNAPSHOT.jar"]
