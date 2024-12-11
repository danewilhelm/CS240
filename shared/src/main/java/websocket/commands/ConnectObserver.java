package websocket.commands;

public class ConnectObserver extends UserGameCommand {

    public ConnectObserver(String authToken, Integer gameID) {
        super(CommandType.CONNECT, authToken, gameID);
    }
}
