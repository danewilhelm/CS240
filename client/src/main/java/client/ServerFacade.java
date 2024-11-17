package client;

import model.GameData;

import java.util.Collection;

public class ServerFacade {

    ClientCommunicator http;

    public ServerFacade(String serverURL) {
        http = new ClientCommunicator(this, serverURL);
    }

    public boolean register(String username, String password, String email) {
        return http.register(username, password, email);
    }

    public boolean login(String username, String password) {
        return http.login(username, password);
    }

    public boolean joinGame(String teamColor, int gameID) {
        return http.joinGame(teamColor, gameID);
    }

    public boolean logout() {
        return http.logout();
    }

    public Collection<GameData> listGames() {
        return http.listGames();
    }

    public boolean createGame(String gameName) {
        return http.createGame(gameName);
    }
}
