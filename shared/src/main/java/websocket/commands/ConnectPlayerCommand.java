package websocket.commands;

public class ConnectPlayerCommand extends UserGameCommand {

    private final String TEAM_COLOR;

    public ConnectPlayerCommand(String authToken, Integer gameID, String teamColor) {
        super(CommandType.CONNECT, authToken, gameID);
        this.TEAM_COLOR = teamColor;
    }

    public String getTeamColor() {
        return this.TEAM_COLOR;
    }
}
