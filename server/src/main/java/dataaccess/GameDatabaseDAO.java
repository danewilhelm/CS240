package dataaccess;

import model.GameData;

import java.util.Collection;
import java.util.List;

public class GameDatabaseDAO implements GameDAO {
    @Override
    public void clear() {

    }

    @Override
    public int createGame(String gameName) {
        return 0;
    }

    @Override
    public GameData getGame(int gameID) {
        return null;
    }

    @Override
    public Collection<GameData> listGames() {
        return List.of();
    }

    @Override
    public void updateGame(GameData updatedGame) {

    }
}
