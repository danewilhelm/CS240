package model;

import chess.ChessGame;

import java.util.Objects;

public record GameData(
        int gameID,
        String whiteUsername,
        String blackUsername,
        String gameName,
        ChessGame game
) {


    public GameData withWhiteUsername(String whiteUsername) {
        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
    }

    public GameData withBlackUsername(String blackUsername) {
        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
    }

    public ChessGame.TeamColor getTeamColorByUsername(String username) {
        if (Objects.equals(whiteUsername(), username)) return ChessGame.TeamColor.WHITE;
        if (Objects.equals(blackUsername(), username)) return ChessGame.TeamColor.BLACK;
        return null;
    }
}