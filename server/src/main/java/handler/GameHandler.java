package handler;

import com.google.gson.Gson;
import request.ListGamesRequest;
import request.RegisterRequest;
import result.ListGamesResult;
import result.RegisterResult;
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

    public static Object handleListGames(Request request, Response response) {
        Gson serializer = new Gson();
        ListGamesRequest listGamesRequest = serializer.fromJson(request.body(), ListGamesRequest.class);

        try {
            ListGamesResult listGamesResult = GameService.listGames(listGamesRequest);
            response.status(200);
            return serializer.toJson(listGamesResult);
        } catch (UnauthorizedException e) {
            response.status(400);
            return serializer.toJson("{ \"message\": \"" + e.getMessage() + "\" }");
        }
    }
}
