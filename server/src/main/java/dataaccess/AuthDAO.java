package dataaccess;

import model.AuthData;

public interface AuthDAO {
    /*
    INTERFACE CLASS

    clear: A method for clearing all data from the database. This is used during testing.
    createAuth: Create a new authorization.
    getAuth: Retrieve an authorization given an authToken.
    deleteAuth: Delete an authorization so that it is no longer valid.
     */

    public void clear() throws DataAccessException;
    public void createAuth(AuthData authData) throws DataAccessException;
    public AuthData getAuth(String authToken) throws DataAccessException;
    public void deleteAuth(String authToken) throws DataAccessException;
}
