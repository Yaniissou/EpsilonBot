# Utilisez une image Maven officielle pour compiler votre projet
FROM maven:3.8.4 AS build

# Copiez le code source de votre application dans le conteneur
RUN git clone https://github.com/Yaniissou/EpsilonBot /usr/src/app

# Définissez le répertoire de travail
WORKDIR /usr/src/app

# Exécutez le processus de build Maven pour compiler votre projet
RUN mvn clean compile assembly:single

# Utilisez une image OpenJDK 17 officielle en tant qu'image de base
FROM amazoncorretto:17

# Définissez le répertoire de travail
WORKDIR /app

# Utilisez ARG pour récupérer l'argument de construction
ARG DISCORD_TOKEN

# Définissez-le comme une variable d'environnement si nécessaire
ENV DISCORD_TOKEN=${DISCORD_TOKEN}

# Copiez votre fichier JAR dans le conteneur
COPY --from=build /usr/src/app/target/EpsilonBot-1.0-SNAPSHOT-jar-with-dependencies.jar /app/EpsilonBot-1.0-SNAPSHOT-jar-with-dependencies.jar
# Commande d'exécution pour démarrer votre application
CMD ["java", "-jar", "EpsilonBot-1.0-SNAPSHOT-jar-with-dependencies.jar", "$DISCORD_TOKEN"]
