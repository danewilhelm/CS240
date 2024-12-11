package websocket.commands;

import chess.ChessGame;

public class ConnectPlayer extends UserGameCommand {

    private final ChessGame.TeamColor TEAM_COLOR;

    public ConnectPlayer(String authToken, Integer gameID, ChessGame.TeamColor teamColor) {
        super(CommandType.CONNECT, authToken, gameID);
        this.TEAM_COLOR = teamColor;
    }

    public ChessGame.TeamColor getTeamColor() {
        return this.TEAM_COLOR;
    }
}
