package dataaccess;

import model.UserData;

import java.sql.SQLException;

public class UserDatabaseDAO implements UserDAO {
    @Override
    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE user")) {
                var rs = preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("DataAccess Error while clearing user table: " + e.getMessage());
        }
    }

    @Override
    public void createUser(UserData newUser) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO user (username, password, email) VALUES (?, ?, ?)")) {
                preparedStatement.setString(1, newUser.username());
                preparedStatement.setString(2, newUser.password());
                preparedStatement.setString(3, newUser.email());
                var rs = preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("DataAccessException while creating user: " + e.getMessage());
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT username, password, email FROM user WHERE username=?")) {
                preparedStatement.setString(1, username);
                var rs = preparedStatement.executeQuery();
                return new UserData(rs.getString("username"), rs.getString("password"), rs.getString("email"));
            }
        } catch (SQLException e) {
            throw new DataAccessException("DataAccess Error while getting user: " + e.getMessage());
        }
    }


}
