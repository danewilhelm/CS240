package client;

import chess.ChessMove;
import com.google.gson.Gson;
import model.GameData;
import ui.ClientUI;
import websocket.commands.*;

import java.util.Collection;

public class ServerFacade {

    private final String serverURL;
    ClientCommunicator http;
    WebsocketCommunicator websocket;
    private String teamColor;

    public ServerFacade(String serverURL) {
        http = new ClientCommunicator(this, serverURL);
        this.serverURL = serverURL;
        teamColor = null;
    }

    // -------------------------------------Websocket connection methods-----------------------------------------
    private boolean connectWebsocket() {
        try {
            websocket = new WebsocketCommunicator(serverURL, this.teamColor);
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
        this.teamColor = joinColor;
        return true;
    }

    public boolean leaveGame(int gameID) {
        if (! connectWebsocket()) {
            return false;
        }
        sendWSCommand(new LeaveCommand(http.getAuthToken(), gameID));
        return true;
    }

    public boolean makeMove(int gameID, ChessMove makeMoveCommand) {
        if (! connectWebsocket()) {
            return false;
        }
        sendWSCommand(new MakeMoveCommand(http.getAuthToken(), gameID, makeMoveCommand));
        return true;
    }


    public boolean resignGame(int gameID) {
        if (! connectWebsocket()) {
            return false;
        }
        sendWSCommand(new ResignCommand(http.getAuthToken(), gameID));
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
