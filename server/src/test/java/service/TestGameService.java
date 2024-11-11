package service;

import dataaccess.DataAccessException;
import model.GameData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.ListGamesRequest;
import request.RegisterRequest;
import result.CreateGameResult;
import result.ListGamesResult;


public class TestGameService {

    private final String catGameName = "Kitty King";
    private String felixAuthToken;
    private String loafAuthToken;

    public void authLoaf() throws DataAccessException {

        RegisterRequest request = new RegisterRequest("Loaf", "meow", "whiteLoaf@cat.net");
        loafAuthToken = FakeServer.USER_SERVICE.register(request).authToken();
    }

    public void authFelix() throws DataAccessException {
        felixAuthToken = FakeServer.exampleRegisterFelix().authToken();
    }

    public CreateGameResult exampleAuthAndCreateCatGame() throws DataAccessException {
        authLoaf();
        authFelix();
        CreateGameRequest createGameRequest = new CreateGameRequest(felixAuthToken, catGameName);
        return FakeServer.GAME_SERVICE.createGame(createGameRequest);
    }

    public void exampleCreateKittyGame() throws DataAccessException {
        CreateGameRequest createGameRequest = new CreateGameRequest(loafAuthToken, "Cat Pat");
        FakeServer.GAME_SERVICE.createGame(createGameRequest);
    }

    @AfterEach
    public void clearAllData() throws DataAccessException{
        FakeServer.CLEAR_SERVICE.clear();
    }

    @Test
    public void goodCreateGame() throws DataAccessException {
        CreateGameResult createGameResult = exampleAuthAndCreateCatGame();
        assert createGameResult.gameID() == catGameName.hashCode();
    }

    @Test
    public void goodCreateTwoGames() throws DataAccessException {
        exampleAuthAndCreateCatGame();
        exampleCreateKittyGame();
    }

    @Test
    public void badCreateGame() throws DataAccessException {
        try {
            CreateGameRequest createGameRequest = new CreateGameRequest(felixAuthToken, catGameName);
            FakeServer.GAME_SERVICE.createGame(createGameRequest);
        } catch (UnauthorizedException e) {
            // test passed
        }

        try {
            CreateGameRequest createGameRequest = new CreateGameRequest(felixAuthToken, null);
            FakeServer.GAME_SERVICE.createGame(createGameRequest);
        } catch (BadRequestException e) {
            // test passed
        }
    }

    @Test
    public void goodWhiteJoinGame() throws DataAccessException {
        CreateGameResult createGameResult = exampleAuthAndCreateCatGame();
        JoinGameRequest request = new JoinGameRequest(loafAuthToken, "WHITE", createGameResult.gameID());
    }

    @Test
    public void goodBlackJoinGame() throws DataAccessException {
        CreateGameResult createGameResult = exampleAuthAndCreateCatGame();
        JoinGameRequest request = new JoinGameRequest(felixAuthToken, "BLACK", createGameResult.gameID());
    }

    @Test
    public void badDoubleWhiteJoin() throws DataAccessException {
        goodWhiteJoinGame();
        try {
            goodWhiteJoinGame();
        } catch (AlreadyTakenException e) {
            // passed test
        }
    }

    @Test
    public void badDoubleBlackJoin() throws DataAccessException {
        goodBlackJoinGame();
        try {
            goodBlackJoinGame();
        } catch (AlreadyTakenException e) {
            // passed test
        }
    }

    @Test
    public void goodListGames() throws DataAccessException {
        exampleAuthAndCreateCatGame();
        ListGamesRequest request = new ListGamesRequest(loafAuthToken);
        ListGamesResult result = FakeServer.GAME_SERVICE.listGames(request);
        assert result.games().size() == 1;
        for (GameData game : result.games()) {
            System.out.println(game);
        }
    }

    @Test
    public void badListGames() throws DataAccessException {
        exampleAuthAndCreateCatGame();
        ListGamesRequest request = new ListGamesRequest("wrong auth");
        try {
            ListGamesResult result = FakeServer.GAME_SERVICE.listGames(request);
        } catch (UnauthorizedException e) {
            // test passed
        }
    }


}
