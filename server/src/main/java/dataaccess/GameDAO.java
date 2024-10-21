package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {
    /*
    INTERFACE CLASS

    clear: A method for clearing all data from the database. This is used during testing.
    createGame: Create a new game.
    getGame: Retrieve a specified game with the given game ID.
    listGames: Retrieve all games.
    updateGame: Updates a chess game. It should replace the chess game string corresponding to a given gameID. This is used when players join a game or when a move is made.
     */

    public void clear();
    public void createGame(String gameName);
    public GameData getGame(int gameID);
    public Collection<GameData> listGames();
    public void updateGame(GameData updatedGame);
}
