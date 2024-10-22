package request;
import chess.ChessGame;

import static chess.ChessGame.TeamColor.WHITE;
import static chess.ChessGame.TeamColor.BLACK;


public record JoinGameRequest(
        String authToken,
        ChessGame.TeamColor playerColor,
        int gameID

) {
}
