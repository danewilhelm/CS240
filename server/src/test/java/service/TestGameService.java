package service;

import model.GameData;
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

    public void authLoaf() {
        RegisterRequest request = new RegisterRequest("Loaf", "meow", "whiteLoaf@cat.net");
        loafAuthToken = UserService.register(request).authToken();
    }

    public void authFelix() {
        felixAuthToken = TestUserService.exampleRegisterFelix().authToken();
    }

    public CreateGameResult exampleAuthAndCreateCatGame() {
        authLoaf();
        authFelix();
        CreateGameRequest createGameRequest = new CreateGameRequest(felixAuthToken, catGameName);
        return GameService.createGame(createGameRequest);
    }

    public void exampleCreateKittyGame() {
        CreateGameRequest createGameRequest = new CreateGameRequest(loafAuthToken, "Cat Pat");
        GameService.createGame(createGameRequest);
    }



    @Test
    public void goodCreateGame() {
        CreateGameResult createGameResult = exampleAuthAndCreateCatGame();
        assert createGameResult.gameID() == catGameName.hashCode();
        ClearService.clear();
    }

    @Test
    public void goodCreateTwoGames() {
        exampleAuthAndCreateCatGame();
        exampleCreateKittyGame();
        ClearService.clear();
    }

    @Test
    public void badCreateGame() {
        try {
            CreateGameRequest createGameRequest = new CreateGameRequest(felixAuthToken, catGameName);
            GameService.createGame(createGameRequest);
        } catch (UnauthorizedException e) {
            // test passed
        }

        try {
            CreateGameRequest createGameRequest = new CreateGameRequest(felixAuthToken, null);
            GameService.createGame(createGameRequest);
        } catch (BadRequestException e) {
            // test passed
        }
        ClearService.clear();
    }

    @Test
    public void goodWhiteJoinGame() {
        CreateGameResult createGameResult = exampleAuthAndCreateCatGame();
        JoinGameRequest request = new JoinGameRequest(loafAuthToken, "WHITE", createGameResult.gameID());
        ClearService.clear();
    }

    @Test
    public void goodBlackJoinGame() {
        CreateGameResult createGameResult = exampleAuthAndCreateCatGame();
        JoinGameRequest request = new JoinGameRequest(felixAuthToken, "BLACK", createGameResult.gameID());
        ClearService.clear();
    }

    @Test
    public void badDoubleWhiteJoin() {
        goodWhiteJoinGame();
        try {
            goodWhiteJoinGame();
        } catch (AlreadyTakenException e) {
            // passed test
        }
        ClearService.clear();
    }

    @Test
    public void badDoubleBlackJoin() {
        goodBlackJoinGame();
        try {
            goodBlackJoinGame();
        } catch (AlreadyTakenException e) {
            // passed test
        }
        ClearService.clear();
    }

    @Test
    public void goodListGames() {
        exampleAuthAndCreateCatGame();
        ListGamesRequest request = new ListGamesRequest(loafAuthToken);
        ListGamesResult result = GameService.listGames(request);
        assert result.games().size() == 1;
        for (GameData game : result.games()) {
            System.out.println(game);
        }
        ClearService.clear();
    }

    @Test
    public void badListGames() {
        exampleAuthAndCreateCatGame();
        ListGamesRequest request = new ListGamesRequest("wrong auth");
        try {
            ListGamesResult result = GameService.listGames(request);
        } catch (UnauthorizedException e) {
            // test passed
        }
        ClearService.clear();
    }


}
