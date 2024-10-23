package service;

import dataaccess.AuthMemoryDAO;
import dataaccess.GameMemoryDAO;
import model.AuthData;
import model.GameData;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.ListGamesRequest;
import result.CreateGameResult;
import result.JoinGameResult;
import result.ListGamesResult;

import java.util.Collection;


public class GameService {
    /*
    Services the logic for 3 endpoints:
        listGames
        CreateGame
        Join Game
     */

    public static CreateGameResult createGame(CreateGameRequest request) {
        if (request.gameName() == null) {
            throw new BadRequestException("Error: bad request");
        }

        if (AuthMemoryDAO.instance.getAuth(request.authToken()) == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        int gameID = GameMemoryDAO.instance.createGame(request.gameName());
        return new CreateGameResult(gameID);
    }

    public static JoinGameResult joinGame(JoinGameRequest request) {

        if (request.playerColor() == null || !(request.playerColor().equals("WHITE") || request.playerColor().equals("BLACK"))) {
            throw new BadRequestException("Error: invalid teamColor given");
        }

        AuthData auth = AuthMemoryDAO.instance.getAuth(request.authToken());
        if (auth == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        GameData oldGame = GameMemoryDAO.instance.getGame(request.gameID());
        if (oldGame == null) {
            throw new BadRequestException("Error: game does not exist");
        }



        GameData updatedGame;
        if (request.playerColor().equals("WHITE")) {
            if (oldGame.whiteUsername() != null) {
                throw new AlreadyTakenException("Error: this team is already chosen");
            }
            updatedGame = new GameData(oldGame.gameID(), auth.username(), oldGame.blackUsername(), oldGame.gameName(), oldGame.game());
        } else {
            if (oldGame.blackUsername() != null) {
                throw new AlreadyTakenException("Error: this team is already chosen");
            }
            updatedGame = new GameData(oldGame.gameID(), oldGame.whiteUsername(), auth.username(), oldGame.gameName(), oldGame.game());
        }
        GameMemoryDAO.instance.updateGame(updatedGame);

        return new JoinGameResult();
    }

    public static ListGamesResult listGames(ListGamesRequest request) {
        if (AuthMemoryDAO.instance.getAuth(request.authToken()) == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        return new ListGamesResult(GameMemoryDAO.instance.listGames());
    }


}
