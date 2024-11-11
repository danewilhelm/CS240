package handler;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import request.*;
import result.CreateGameResult;
import result.JoinGameResult;
import result.ListGamesResult;
import service.*;
import spark.Request;
import spark.Response;

public class GameHandler {
        /*
    Handles 3 endpoints:
        listGames
        CreateGame
        Join Game
     */

    private final GameService gameServiceInstance;

    public GameHandler (AuthDAO authDAOInstance, GameDAO gameDAOInstance, UserDAO userDAOInstance) {
        gameServiceInstance = new GameService(authDAOInstance, gameDAOInstance, userDAOInstance);
    }

    public Object handleListGames(Request request, Response response) {
        Gson serializer = new Gson();
        ListGamesRequest listGamesRequest = new ListGamesRequest(request.headers("authorization"));

        try {
            ListGamesResult listGamesResult = gameServiceInstance.listGames(listGamesRequest);
            response.status(200);
            return serializer.toJson(listGamesResult);
        } catch (UnauthorizedException e) {
            response.status(401);
            return "{ \"message\": \"" + e.getMessage() + "\" }";
        } catch (DataAccessException e) {
            response.status(500);
            return "{ \"message\": \"" + e.getMessage() + "\" }";
        }
    }

    public Object handleCreateGame(Request request, Response response) {
        Gson serializer = new Gson();
        String gameName = request.body();
        CreateGameRequest createGameRequest = new CreateGameRequest(request.headers("authorization"), gameName);

        try {
            CreateGameResult createGameResult = gameServiceInstance.createGame(createGameRequest);
            response.status(200);
            return serializer.toJson(createGameResult);
        } catch (BadRequestException e) {
            response.status(400);
            return "{ \"message\": \"" + e.getMessage() + "\" }";
        } catch (UnauthorizedException e) {
            response.status(401);
            return "{ \"message\": \"" + e.getMessage() + "\" }";
        } catch (DataAccessException e) {
            response.status(500);
            return "{ \"message\": \"" + e.getMessage() + "\" }";
        }
    }

    public Object handleJoinGame(Request request, Response response) {
        Gson serializer = new Gson();
        record RequestBodyData(String playerColor, int gameID) {}
        RequestBodyData bodyData = serializer.fromJson(request.body(),  RequestBodyData.class);

        JoinGameRequest joinGameRequest = new JoinGameRequest(request.headers("authorization"), bodyData.playerColor(), bodyData.gameID());

        try {
            JoinGameResult joinGameResult = gameServiceInstance.joinGame(joinGameRequest);
            response.status(200);
            return serializer.toJson(joinGameResult);
        } catch (BadRequestException e) {
            response.status(400);
            return "{ \"message\": \"" + e.getMessage() + "\" }";
        } catch (UnauthorizedException e) {
            response.status(401);
            return "{ \"message\": \"" + e.getMessage() + "\" }";
        } catch (AlreadyTakenException e) {
            response.status(403);
            return "{ \"message\": \"" + e.getMessage() + "\" }";
        } catch (DataAccessException e) {
            response.status(500);
            return "{ \"message\": \"" + e.getMessage() + "\" }";
        }
    }
}
