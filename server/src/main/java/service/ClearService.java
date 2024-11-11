package service;


import dataaccess.*;
import result.ClearResult;

public class ClearService {

    private AuthDAO authDAOInstance;
    private GameDAO gameDAOInstance;
    private UserDAO userDAOInstance;


    public ClearService(AuthDAO authDAOInstance, GameDAO gameDAOInstance, UserDAO userDAOInstance) {
        this.authDAOInstance = authDAOInstance;
        this.gameDAOInstance = gameDAOInstance;
        this.userDAOInstance = userDAOInstance;
    }

    public ClearResult clear() throws DataAccessException {
        authDAOInstance.clear();
        gameDAOInstance.clear();
        userDAOInstance.clear();
        return new ClearResult();
    }
}
