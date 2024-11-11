package dataaccess;

import model.UserData;

import java.sql.SQLException;

public class UserDatabaseDAO implements UserDAO {

    public UserDatabaseDAO () {

    }

    @Override
    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE userTable")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("DataAccessException while clearing user table: " + e.getMessage());
        }
    }

    @Override
    public void createUser(UserData newUser) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO userTable (username, password, email) VALUES (?, ?, ?)")) {
                preparedStatement.setString(1, newUser.username());
                preparedStatement.setString(2, newUser.password());
                preparedStatement.setString(3, newUser.email());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("DataAccessException while creating user: " + e.getMessage());
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT username, password, email FROM userTable WHERE username=?")) {
                preparedStatement.setString(1, username);
                var rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    return new UserData(rs.getString(1), rs.getString(2), rs.getString(3));
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("DataAccessException while getting user: " + e.getMessage());
        }
    }


}
