package client;

import com.google.gson.Gson;
import model.GameData;
import websocket.commands.ConnectPlayerCommand;
import websocket.commands.LeaveCommand;
import websocket.commands.UserGameCommand;

import java.util.Collection;

public class ServerFacade {

    private final String serverURL;
    ClientCommunicator http;
    WebsocketCommunicator websocket;

    public ServerFacade(String serverURL) {
        http = new ClientCommunicator(this, serverURL);
        this.serverURL = serverURL;
    }

    // -------------------------------------Websocket connection methods-----------------------------------------
    private boolean connectWebsocket() {
        try {
            websocket = new WebsocketCommunicator(serverURL);
            return true;
        } catch (Exception e) {
            System.out.println("failed to connect websocket");
            return false;
        }
    }

    public void sendWSCommand(UserGameCommand command) {
        String commandGSON = new Gson().toJson(command);
        websocket.send(commandGSON);
    }

    public boolean connectPlayer(String joinColor, int gameID) {
        if (! connectWebsocket()) {
            return false;
        }
        sendWSCommand(new ConnectPlayerCommand(http.getAuthToken(), gameID, joinColor));
        return true;
    }

    public boolean leaveGame(int gameID) {
        if (! connectWebsocket()) {
            return false;
        }
        sendWSCommand(new LeaveCommand(http.getAuthToken(), gameID));
        return true;
    }




    // -------------------------------------HTTP connection methods----------------------------------------------------

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
