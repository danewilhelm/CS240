package service;

import model.GameData;
import org.junit.jupiter.api.Test;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.ListGamesRequest;
import request.RegisterRequest;
import result.CreateGameResult;
import result.ListGamesResult;
import result.RegisterResult;



public class testGameService {

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

    public CreateGameResult exampleCreateCatGame() {
        authLoaf();
        authFelix();
        CreateGameRequest createGameRequest = new CreateGameRequest(felixAuthToken, catGameName);
        return GameService.createGame(createGameRequest);
    }



    @Test
    public void normalCreateGame() {
        CreateGameResult createGameResult = exampleCreateCatGame();
        assert createGameResult.gameID() == catGameName.hashCode();
    }

    @Test
    public void createTwoGames() {
        exampleCreateCatGame();
        CreateGameRequest createGameRequest = new CreateGameRequest(loafAuthToken, "Cat Pat");
        GameService.createGame(createGameRequest);

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
    }

    @Test
    public void normalWhiteJoinGame() {
        CreateGameResult createGameResult = exampleCreateCatGame();
        JoinGameRequest request = new JoinGameRequest(loafAuthToken, "WHITE", createGameResult.gameID());
    }

    @Test
    public void normalBlackJoinGame() {
        CreateGameResult createGameResult = exampleCreateCatGame();
        JoinGameRequest request = new JoinGameRequest(felixAuthToken, "BLACK", createGameResult.gameID());
    }

    @Test
    public void doubleWhiteJoin() {
        normalWhiteJoinGame();
        try {
            normalWhiteJoinGame();
        } catch (AlreadyTakenException e) {
            // passed test
        }
    }

    @Test
    public void doubleBlackJoin() {
        normalBlackJoinGame();
        try {
            normalBlackJoinGame();
        } catch (AlreadyTakenException e) {
            // passed test
        }
    }

    @Test
    public void listGames() {
        createTwoGames();
        ListGamesRequest request = new ListGamesRequest(loafAuthToken);
        ListGamesResult result = GameService.listGames(request);
        assert result.games().size() == 2;
        for (GameData game : result.games()) {
            System.out.println(game);
        }
    }


}
