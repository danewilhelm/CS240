package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class GameDatabaseDAO implements GameDAO {
    @Override
    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE gameTable")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("DataAccessException while clearing game table: " + e.getMessage());
        }
    }
    // var resultSet = preparedStatement.getGeneratedKeys();
    //var ID = 0;
    //if (resultSet.next()) {
    //    ID = resultSet.getInt(1);
    //}
    @Override
    public int createGame(String gameName) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String sqlStatement = "INSERT INTO gameTable (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)";
            try (var preparedStatement = conn.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, null);
                preparedStatement.setString(2, null);
                preparedStatement.setString(3, gameName);

                var jsonChessBoard = new Gson().toJson(new ChessGame());
                preparedStatement.setString(4, jsonChessBoard);

                preparedStatement.executeUpdate();
                var resultSet = preparedStatement.getGeneratedKeys();
                var ID = 0;
                if (resultSet.next()) {
                    ID = resultSet.getInt(1);
                }

                return ID;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("DataAccessException while creating game: " + e);
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String sqlStatement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM gameTable WHERE gameID=?";
            try (var preparedStatement = conn.prepareStatement(sqlStatement)) {
                preparedStatement.setInt(1, gameID);
                var rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    return extractGame(rs);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("DataAccessException while getting game: " + e.getMessage());
        }
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String sqlStatement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM gameTable";
            try (var preparedStatement = conn.prepareStatement(sqlStatement)) {
                var rs = preparedStatement.executeQuery();
                return extractGamesList(rs);
            }
        } catch (SQLException e) {
        throw new DataAccessException("DataAccessException while getting list of games: " + e.getMessage());
        }
    }

    private Collection<GameData> extractGamesList(ResultSet rs) throws SQLException {
        Collection<GameData> gameDataList = new ArrayList<>();
        while (rs.next()) {
             gameDataList.add(extractGame(rs));
        }
        return gameDataList;
    }

    private GameData extractGame(ResultSet rs) throws SQLException {
        int gameID = rs.getInt(1);
        String whiteUsername = rs.getString(2);
        String blackUsername = rs.getString(3);
        String gameName = rs.getString(4);
        String stringChessGame = rs.getString(5);
        ChessGame normalChessGame = new Gson().fromJson(stringChessGame, ChessGame.class);
        return new GameData(gameID, whiteUsername, blackUsername, gameName, normalChessGame);

    }

    @Override
    public void updateGame(GameData updatedGame) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String sqlStatement = "UPDATE gameTable SET whiteUsername=?, blackUsername=?, gameName=?, game=? WHERE gameID=?";
            try (var preparedStatement = conn.prepareStatement(sqlStatement)) {
                preparedStatement.setString(1, updatedGame.whiteUsername());
                preparedStatement.setString(2, updatedGame.blackUsername());
                preparedStatement.setString(3, updatedGame.gameName());

                var jsonChessBoard = new Gson().toJson(new ChessGame());
                preparedStatement.setString(4, jsonChessBoard);

                preparedStatement.setInt(5, updatedGame.gameID());

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
//            throw new DataAccessException("DataAccessException while creating game: " + e.getMessage());
            System.out.println("DataAccessException while updating game: " + e.getMessage());
        }
    }
}
