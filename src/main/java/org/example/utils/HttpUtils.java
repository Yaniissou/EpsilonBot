package org.example.utils;

import com.google.gson.Gson;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.List;

public class HttpUtils {

    private static final String API_URL = "http://localhost:8080";
    public static int createUser(String minecraftUsername, long discordId) {
        OkHttpClient client = new OkHttpClient();

        final FormBody body = new FormBody.Builder()
                .add("discordID", String.valueOf(discordId))
                .add("minecraftUsername", minecraftUsername)
                .build();

        final Request request = new Request.Builder()
                .url(API_URL + "/users")
                .post(body)
                .build();

        try {
            client.newCall(request).execute();
            return 201;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static int createGameUser(long discordId, long gameId) {
        OkHttpClient client = new OkHttpClient();

        final FormBody body = new FormBody.Builder()
                .add("discordID", String.valueOf(discordId))
                .add("gameID", String.valueOf(gameId))
                .build();

        final Request request = new Request.Builder()
                .url(API_URL + "/gameusers")
                .post(body)
                .build();


        try {
            Response execute = client.newCall(request).execute();
            return execute.code();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static int removeGameUser(long discordId, long gameId) {
        OkHttpClient client = new OkHttpClient();

        final FormBody body = new FormBody.Builder()
                .add("discordID", String.valueOf(discordId))
                .add("gameID", String.valueOf(gameId))
                .build();

        final Request request = new Request.Builder()
                .url(API_URL + "/gameusers")
                .delete(body)
                .build();

        try {
            client.newCall(request).execute();
            return 204;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static int createGame(String host, String gamemode, int maxPlayers, String date) {
        OkHttpClient client = new OkHttpClient();

        final FormBody body = new FormBody.Builder()
                .add("host", host)
                .add("gamemode", gamemode)
                .add("maxPlayers", String.valueOf(maxPlayers))
                .add("date", date)
                .build();

        final Request request = new Request.Builder()
                .url(API_URL + "/games")
                .post(body)
                .build();

        try {
            client.newCall(request).execute();
            return 201;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static long getLastGame() {
        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url(API_URL + "/games/last")
                .get()
                .build();

        try {
            String response = client.newCall(request).execute().body().string();
            return Long.parseLong(response);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Envoie une requête POST à l'API pour récupérer les pseudos en fonction d'une liste d'IDs.
     *
     * @param ids Liste des IDs pour lesquels récupérer les pseudos
     * @return Liste des pseudos récupérés depuis l'API
     */
    public static List<String> getLinkedUsernames(List<Long> ids) {
        OkHttpClient client = new OkHttpClient();

        final FormBody.Builder bodyBuilder = new FormBody.Builder();
        for (Long id : ids) {
            bodyBuilder.add("ids", String.valueOf(id));
        }

        final Request request = new Request.Builder()
                .url(API_URL + "/users/usernames")
                .post(bodyBuilder.build())
                .build();

        try {
            String response = client.newCall(request).execute().body().string();
            return new Gson().fromJson(response, List.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<String> getParticipants(){
        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url(API_URL + "/gameusers/" + getLastGame())
                .get()
                .build();

        try {
            String response = client.newCall(request).execute().body().string();
            return new Gson().fromJson(response, List.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
