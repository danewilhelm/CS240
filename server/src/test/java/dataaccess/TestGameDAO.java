package dataaccess;

import chess.ChessGame;
import model.GameData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import service.FakeServer;

import java.util.Collection;

public class TestGameDAO {
//    private final GameData emptyGame = new GameData(1, null, null, ga)

    @AfterEach
    public void clearAllData() throws DataAccessException {
        FakeServer.CLEAR_SERVICE.clear();
    }

    @Test
    public void goodClear() throws DataAccessException {
        FakeServer.GAME_DAO.createGame(FakeServer.GAME_NAME_ONE);
        FakeServer.GAME_DAO.createGame(FakeServer.GAME_NAME_TWO);
        FakeServer.GAME_DAO.clear();
        assert FakeServer.GAME_DAO.getGame(1) == null;
        assert FakeServer.GAME_DAO.getGame(2) == null;
    }

    @Test
    public void goodCreateGame() throws DataAccessException {
        FakeServer.GAME_DAO.createGame(FakeServer.GAME_NAME_ONE);
    }

    @Test
    public void badCreateGame() throws DataAccessException {
        FakeServer.GAME_DAO.createGame("");
    }

    @Test
    public void goodGetGame() throws DataAccessException {
        FakeServer.GAME_DAO.createGame(FakeServer.GAME_NAME_ONE);
        assert FakeServer.GAME_DAO.getGame(1).gameName().equals(FakeServer.GAME_NAME_ONE);
    }

    @Test
    public void badGetGame() throws DataAccessException {
        assert FakeServer.GAME_DAO.getGame(1) == null;
    }

    @Test
    public void goodListGames() throws DataAccessException {
        FakeServer.GAME_DAO.createGame(FakeServer.GAME_NAME_ONE);
        FakeServer.GAME_DAO.createGame(FakeServer.GAME_NAME_TWO);
        Collection<GameData> listOfGames = FakeServer.GAME_DAO.listGames();
        assert listOfGames.size() == 2;
//        assert listOfGames.contains()
    }

    @Test
    public void badListGames() throws DataAccessException {
        Collection<GameData> listOfGames = FakeServer.GAME_DAO.listGames();
        assert listOfGames.isEmpty();
    }

    @Test
    public void goodUpdateGame() throws DataAccessException {
        int GameID = FakeServer.GAME_DAO.createGame(FakeServer.GAME_NAME_ONE);
        GameData oldGame = FakeServer.GAME_DAO.getGame(GameID);
        GameData updatedGame = new GameData(oldGame.gameID(), FakeServer.FELIX_THE_CAT, oldGame.blackUsername(), oldGame.gameName(), oldGame.game());
        FakeServer.GAME_DAO.updateGame(updatedGame);
    }

    @Test
    public void badUpdateGame() throws DataAccessException {
        FakeServer.GAME_DAO.createGame(FakeServer.GAME_NAME_ONE);
        GameData differentGame = new GameData(420, null, null, "what you smokin'", new ChessGame());
        FakeServer.GAME_DAO.updateGame(differentGame);
    }
}
