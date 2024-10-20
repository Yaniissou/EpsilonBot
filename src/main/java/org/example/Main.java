package org.example;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class Main {


    public static long SERVER_ID = 1294431282842959967L;
    //TESTING :
    //public static long SERVER_ID =1209564843775098991L;

    public static List<Long> ANNOUNCE_CHANNEL_IDS = Arrays.asList(1294452095369478224L);
    //TESTING :
    //public static List<Long> ANNOUNCE_CHANNEL_IDS = Arrays.asList(1209564850142056531L);

   public static long LOGS_CHANNEL_ID = 1294670254387105883L;
    //TESTING :
    //public static long LOGS_CHANNEL_ID = 1209580226024317008L;
    public static List<Long> WHITELISTED_IDS = Arrays.asList(262674203931574273L, 191213751746166785L);
    public static long HOST_ROLE_ID = 1294433557636448336L;

    //TESTING :
    //public static long BOT_ID = 1211273034686791741L;
public static long BOT_ID = 1209564326902366249L;

   public static List<Long> ROLES_TO_PING = List.of(1294436629083328594L);

    //TESTING :
    // public static List<Long> ROLES_TO_PING = Arrays.asList(1209593119675387974L);

    public static List<Long> PARTICIPANTS = new ArrayList<>();
    public static void main(String[] args) {
        // Chemin absolu vers la base de données SQLite
        String dbPath = "/app/db/botDB.db";
        String token = System.getenv("TOKEN");

        // Connexion à la base de données SQLite
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath)) {
            // Effectuer des opérations sur la base de données
            System.out.println("Connexion à la base de données SQLite réussie.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la connexion à la base de données SQLite : " + e.getMessage());
        }

        JDABuilder builder = JDABuilder.createDefault(token);
        builder.enableIntents(GatewayIntent.GUILD_MESSAGE_REACTIONS);
        builder.enableIntents(GatewayIntent.MESSAGE_CONTENT);


        JDA jda = builder.build();
        jda.updateCommands().addCommands(
                Commands.slash("annonce", "Annoncer une partie d'UHC")
                        .setGuildOnly(true)
                        .addOption(OptionType.STRING, "date", "La date de l'UHC : LA DATE DOIT ÊTRE FORMATÉE DE LA SORTE : dd-MM-yyyy HH:mm", true)
                        .addOption(OptionType.STRING, "slots", "Le nombre de slots maximum", true)
                        .addOption(OptionType.STRING, "mdj", "Le mode de jeu", true)
                        .addOption(OptionType.STRING, "description", "Informations sur la partie", true),

                Commands.slash("link", "Lier votre compte Minecraft à votre compte Discord")
                        .addOption(OptionType.STRING, "pseudo", "Votre pseudo Minecraft", true),

                Commands.slash("participants", "Afficher les participants à l'UHC"),
                Commands.slash("me", "Afficher votre pseudo Minecraft lié à votre compte Discord")


        ).queue();

        jda.addEventListener(new AnnonceCommand(jda), new LinkCommand(), new ParticipantsCommand(jda), new ReactionListener(jda), new MessageListener());


    }


}