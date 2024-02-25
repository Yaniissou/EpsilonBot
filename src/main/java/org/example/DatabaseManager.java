package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

public class DatabaseManager {
    private static Connection conn;

    // Méthode pour établir la connexion à la base de données
    public static void connect(String url) throws SQLException {
        conn = DriverManager.getConnection(url);
    }

    // Méthode pour insérer des données dans la base de données
    public static void insertData(String tableName, String[] columnNames, String[] values) throws SQLException {
        // Vérifiez que la connexion est établie
        if (conn == null || conn.isClosed()) {
            throw new SQLException("La connexion à la base de données n'est pas établie.");
        }

        // Vérifiez que les tableaux de colonnes et de valeurs ont la même longueur
        if (columnNames.length != values.length) {
            throw new IllegalArgumentException("Les tableaux de colonnes et de valeurs doivent avoir la même longueur.");
        }

        // Construire la requête SQL d'insertion de données
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("INSERT INTO ").append(tableName).append(" (");
        for (int i = 0; i < columnNames.length; i++) {
            sqlBuilder.append(columnNames[i]);
            if (i < columnNames.length - 1) {
                sqlBuilder.append(", ");
            }
        }
        sqlBuilder.append(") VALUES (");
        for (int i = 0; i < values.length; i++) {
            sqlBuilder.append("?");
            if (i < values.length - 1) {
                sqlBuilder.append(", ");
            }
        }
        sqlBuilder.append(")");

        // Création de l'objet PreparedStatement
        try (PreparedStatement pstmt = conn.prepareStatement(sqlBuilder.toString())) {
            // Remplacement des ? par les valeurs à insérer
            for (int i = 0; i < values.length; i++) {
                pstmt.setString(i + 1, values[i]);
            }

            // Exécution de la requête d'insertion
            pstmt.executeUpdate();
        }
    }

    // Méthode pour fermer la connexion à la base de données
    public static void close() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }

    public static void insertMessage(String messageId, String authorId, String game_mode) throws SQLException{
        String[] columns = {"discord_message_id","game_mode","author_discord_id"};
        String[] values = {messageId,game_mode,authorId};
        DatabaseManager.insertData("messages", columns, values);

    }
}

