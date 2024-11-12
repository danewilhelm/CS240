package dataaccess;

import model.AuthData;

import java.sql.SQLException;
import java.util.Arrays;

public class AuthDatabaseDAO implements AuthDAO {

    @Override
    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE authTable")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("DataAccessException while clearing auth table: " + e.getMessage());
        }
    }

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO authTable (username, authToken) VALUES (?, ?)")) {
                preparedStatement.setString(1, authData.username());
                preparedStatement.setString(2, authData.authToken());

                System.out.println("DEBUG: " + authData);
                System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
//            e.printStackTrace();
            throw new DataAccessException("DataAccessException while creating auth: " + e.getMessage());
        }
    }

//    @Override
//    public String getExistingAuthToken(String username) throws DataAccessException {
//        try (var conn = DatabaseManager.getConnection()) {
//            try (var preparedStatement = conn.prepareStatement("SELECT authToken FROM authTable WHERE username=?")) {
//                preparedStatement.setString(1, username);
//
//                var rs = preparedStatement.executeQuery();
//                if (rs.next()) {
//                    return rs.getString(1);
//                } else {
//                    return null;
//                }
//
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new DataAccessException("DataAccessException while creating auth: " + e.getMessage());
//        }
//    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT username, authToken FROM authTable WHERE authToken=?")) {
                preparedStatement.setString(1, authToken);
                var rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    return new AuthData(rs.getString(1), rs.getString(2));
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("DataAccessException while getting auth: " + e.getMessage());
        }
    }


    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DELETE FROM authTable WHERE authToken=?")) {
                preparedStatement.setString(1, authToken);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
                throw new DataAccessException("DataAccessException while deleting auth: " + e.getMessage());
            }
        }
}
