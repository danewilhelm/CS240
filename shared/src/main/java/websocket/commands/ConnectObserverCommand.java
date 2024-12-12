package websocket.commands;

public class ConnectObserverCommand extends UserGameCommand {

    public ConnectObserverCommand(String authToken, Integer gameID) {
        super(CommandType.CONNECT, authToken, gameID);
    }
}
