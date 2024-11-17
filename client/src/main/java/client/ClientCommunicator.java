package client;

import com.google.gson.Gson;
import model.GameData;
import result.ListGamesResult;
import result.LoginResult;
import result.RegisterResult;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Map;

public class ClientCommunicator {

    String serverUrl;
    ServerFacade serverFacade;
    String authToken;

    public ClientCommunicator(ServerFacade serverFacade, String serverURL) {
        this.serverUrl = serverURL;
        this.serverFacade = serverFacade;
    }



    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.addRequestProperty("authorization", authToken);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, "failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

    public boolean register(String username, String password, String email) {
        Map<String, String> body = Map.of(
                "username", username,
                "password", password,
                "email", email);

        try {
            RegisterResult result = makeRequest("POST", "/user", body, RegisterResult.class);
            authToken = result.authToken();
            return true;
        } catch (ResponseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean login(String username, String password) {
        Map<String, String> body = Map.of(
                "username", username,
                "password", password);

        String json = new Gson().toJson(body);
        try {
            LoginResult result = makeRequest("POST","/session", body, LoginResult.class);
            authToken = result.authToken();
            return true;
        } catch (ResponseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean joinGame(String playerColor, int gameID) {
        var body = Map.of(
                "gameID", gameID,
                "playerColor", playerColor.toUpperCase());

        String json = new Gson().toJson(body);
        try {
            makeRequest("PUT","/game", body, null);
            return true;
        } catch (ResponseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean logout() {
        try {
            makeRequest("DELETE","/session", null, null);
            return true;
        } catch (ResponseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean listGames() {
        try {
            var result = makeRequest("GET","/game", null, ListGamesResult.class);
            int index = 1;
            for (GameData game: result.games()) {
                System.out.println(index + ". " + game.gameName());
                System.out.println("    whitePlayer: " + game.whiteUsername());
                System.out.println("    blackPlayer: " + game.blackUsername());
            }
            return true;
        } catch (ResponseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean clear() {
        try {
            makeRequest("DELETE","/db", null, null);
            return true;
        } catch (ResponseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean createGame(String gameName) {
        try {
            var body = Map.of("gameName", gameName);
            makeRequest("POST","/game", body, null);
            return true;
        } catch (ResponseException e) {
            e.printStackTrace();
            return false;
        }
    }
}
