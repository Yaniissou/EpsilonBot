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
import java.util.Arrays;
import java.util.List;

public class Main {


    public static long SERVER_ID = 1196782142739447858L;
    //TESTING :
    //public static long SERVER_ID =1209564843775098991L;

    public static List<Long> ANNOUNCE_CHANNEL_IDS = Arrays.asList(1196782376269918299L,1277779555196862551L);
    //TESTING :
    //public static long ANNOUNCE_CHANNEL_ID = 1209564850142056531L;

    public static long LOGS_CHANNEL_ID = 1201545255024087161L;
    //TESTING :
    //public static long LOGS_CHANNEL_ID = 1209580226024317008L;
    public static List<Long> WHITELISTED_IDS = Arrays.asList(262674203931574273L, 191213751746166785L);
    public static long BOT_ID = 1209564326902366249L;
    //TESTING :
    //public static long BOT_ID = 1209564326902366249L;

    public static List<Long> ROLES_TO_PING = List.of(1209253560445960243L);

    //TESTING :
    //public static List<Long> ROLES_TO_PING = Arrays.asList(1209593119675387974L);
    public static void main(String[] args) {
        // Chemin absolu vers la base de données SQLite
        String dbPath = "/app/db/botDB.db";

        // Connexion à la base de données SQLite
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath)) {
            // Effectuer des opérations sur la base de données
            System.out.println("Connexion à la base de données SQLite réussie.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la connexion à la base de données SQLite : " + e.getMessage());
        }

        JDABuilder builder = JDABuilder.createDefault(args[0]);
        builder.enableIntents(GatewayIntent.GUILD_MESSAGE_REACTIONS);
        builder.enableIntents(GatewayIntent.MESSAGE_CONTENT);


        JDA jda = builder.build();
        jda.updateCommands().addCommands(
                Commands.slash("annonce", "Annoncer une partie d'UHC")
                        .setGuildOnly(true)
                        .addOption(OptionType.STRING, "date", "La date de l'UHC", true)
                        .addOption(OptionType.STRING, "horaire", "L'heure de l'UHC", true)
                        .addOption(OptionType.STRING, "slots", "Le nombre de slots maximum", true)
                        .addOption(OptionType.STRING, "mdj", "Le mode de jeu", true)
                        .addOption(OptionType.STRING, "description", "Informations sur la partie", true)


        ).queue();

        jda.addEventListener(new AnnonceCommand(jda), new ReactionListener(jda), new MessageListener());


    }

    public static void insertData(Connection conn, String valeurColonne1, String valeurColonne2) {
        try {
            // Requête SQL d'insertion de données
            String sql = "INSERT INTO nom_de_la_table (colonne1, colonne2) VALUES (?, ?)";

            // Création de l'objet PreparedStatement
            PreparedStatement pstmt = conn.prepareStatement(sql);

            // Remplacement des ? par les valeurs à insérer
            pstmt.setString(1, valeurColonne1);
            pstmt.setString(2, valeurColonne2);

            // Exécution de la requête d'insertion
            pstmt.executeUpdate();

            // Fermeture des ressources
            pstmt.close();

            System.out.println("Données insérées avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'insertion des données : " + e.getMessage());
        }
    }
}