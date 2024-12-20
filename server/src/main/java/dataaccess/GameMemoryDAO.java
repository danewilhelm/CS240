package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.*;

public class GameMemoryDAO implements GameDAO {

    private final Map<Integer, GameData> gameDataMap = new HashMap<>();


    @Override
    public void clear() {
        gameDataMap.clear();
    }

    @Override
    public int createGame(String gameName) {
        int newGameID = generateGameID(gameName);
        GameData newGame = new GameData(newGameID, null, null, gameName, new ChessGame());
        gameDataMap.put(newGameID, newGame);
        return newGameID;
    }

    private int generateGameID (String gameName) {
        int newGameID = gameName.hashCode();
        Random rando = new Random();
        while (gameDataMap.containsKey(newGameID)) {
            newGameID += rando.nextInt(1000);
        }
        return newGameID;
    }

    @Override
    public GameData getGame(int gameID) {
        return gameDataMap.get(gameID);
    }

    @Override
    public Collection<GameData> listGames() {
        return new ArrayList<>(gameDataMap.values());
    }

    @Override
    public void updateGame(GameData updatedGame) {
        gameDataMap.remove(updatedGame.gameID());
        gameDataMap.put(updatedGame.gameID(), updatedGame);
    }
}
