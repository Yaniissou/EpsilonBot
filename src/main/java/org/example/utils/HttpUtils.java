package org.example.utils;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class HttpUtils {

    public static int createUser(String pseudo, long discordId) {
        String jsonInputString = String.format("{\"discordID\":%d, \"minecraftUsername\":\"%s\"}", discordId, pseudo);
        try {
            int responseCode = sendPostRequest("http://localhost:8080/users", jsonInputString);
            return responseCode;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private static int sendPostRequest(String url, String jsonInputString) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // Ajouter les headers de la requête
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");

        // Activer le mode output
        con.setDoOutput(true);

        // Envoyer les données de la requête
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // Lire la réponse
        int responseCode = con.getResponseCode();
        System.out.println("Response Code : " + responseCode);
        return responseCode;
    }

    public static String sendGetRequest(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // Définir la méthode de requête
        con.setRequestMethod("GET");

        // Lire la réponse
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // Retourner la réponse JSON en tant que chaîne
        return response.toString();
    }

    public static void processJsonResponse(String jsonResponse) {
        // Conversion manuelle du JSON en tableau
        String[] jsonArray = jsonResponse.replaceAll("[\\[\\]{}]", "").split("(?<=\\}),(?=\\{)");

        for (String jsonObjectStr : jsonArray) {
            String minecraftUsername = extractValue(jsonObjectStr, "minecraftUsername");
            System.out.println("Minecraft Username: " + minecraftUsername);
        }
    }

    private static String extractValue(String jsonObjectStr, String key) {
        String searchPattern = "\"" + key + "\":\"";
        int startIndex = jsonObjectStr.indexOf(searchPattern) + searchPattern.length();
        int endIndex = jsonObjectStr.indexOf("\"", startIndex);

        if (startIndex != -1 && endIndex != -1) {
            return jsonObjectStr.substring(startIndex, endIndex);
        }
        return "";
    }


    /**
     * Envoie une requête POST à l'API pour récupérer les pseudos en fonction d'une liste d'IDs.
     *
     * @param ids Liste des IDs pour lesquels récupérer les pseudos
     * @return Liste des pseudos récupérés depuis l'API
     */
    public static List<String> fetchUsernames(List<Long> ids) {
        Gson gson = new Gson();
        // Convertir la liste d'IDs en JSON
        String jsonInputString = gson.toJson(ids);

        try {
            // Créer la connexion HTTP
            URL url = new URL("http://localhost:8080/users/usernames");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            // Envoyer la requête
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Lire la réponse
            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }

            // Convertir la réponse JSON en liste de pseudos
            return gson.fromJson(response.toString(), List.class);

        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // Retourne une liste vide en cas d'erreur
        }
    }

}
