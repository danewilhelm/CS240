package service;

import dataaccess.*;
import model.AuthData;
import model.GameData;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.ListGamesRequest;
import result.CreateGameResult;
import result.JoinGameResult;
import result.ListGamesResult;

import java.util.Objects;


public class GameService {
    /*
    Services the logic for 3 endpoints:
        listGames
        CreateGame
        Join Game
     */

    private AuthDAO authDAOInstance;
    private GameDAO gameDAOInstance;
    private UserDAO userDAOInstance;


    public GameService(AuthDAO authDAOInstance, GameDAO gameDAOInstance, UserDAO userDAOInstance) {
        this.authDAOInstance = authDAOInstance;
        this.gameDAOInstance = gameDAOInstance;
        this.userDAOInstance = userDAOInstance;
    }

    public CreateGameResult createGame(CreateGameRequest request) throws DataAccessException {
        if (request.gameName() == null) {
            throw new BadRequestException("Error: bad request");
        }
        System.out.println(request.authToken());
        if (authDAOInstance.getAuth(request.authToken()) == null) {
            throw new UnauthorizedException("Error: unauthorized create game");
        }

        int gameID = gameDAOInstance.createGame(request.gameName());
        return new CreateGameResult(gameID);
    }

    public JoinGameResult joinGame(JoinGameRequest request) throws DataAccessException {

        if (request.playerColor() == null || !(request.playerColor().equals("WHITE") || request.playerColor().equals("BLACK"))) {
            throw new BadRequestException("Error: invalid teamColor given");
        }

        AuthData auth = authDAOInstance.getAuth(request.authToken());
        if (auth == null) {
            throw new UnauthorizedException("Error: unauthorized join game");
        }

        GameData oldGame = gameDAOInstance.getGame(request.gameID());
        if (oldGame == null) {
            throw new BadRequestException("Error: game does not exist");
        }



        GameData updatedGame;
        if (request.playerColor().equals("WHITE")) {
            if (oldGame.whiteUsername() != null) {
                throw new AlreadyTakenException("Error: white team is already chosen");
            }
            updatedGame = new GameData(oldGame.gameID(), auth.username(), oldGame.blackUsername(), oldGame.gameName(), oldGame.game());
        } else {
            if (oldGame.blackUsername() != null) {
                throw new AlreadyTakenException("Error: black team is already chosen");
            }
            updatedGame = new GameData(oldGame.gameID(), oldGame.whiteUsername(), auth.username(), oldGame.gameName(), oldGame.game());
        }
        gameDAOInstance.updateGame(updatedGame);

        return new JoinGameResult();
    }

    public ListGamesResult listGames(ListGamesRequest request) throws DataAccessException {
        if (authDAOInstance.getAuth(request.authToken()) == null) {
            throw new UnauthorizedException("Error: unauthorized list games");
        }

        return new ListGamesResult(gameDAOInstance.listGames());
    }


}
