package service;


import dataaccess.*;

public class ClearService {
    /*
    Services the logic for 1 endpoint: clear
     */
    public void clear() {
        AuthDAO authAccess = new AuthMemoryDAO();
        authAccess.clear();

        GameDAO gameAccess = new GameMemoryDAO();
        gameAccess.clear();

        UserDAO userAccess = new UserMemoryDAO();
        userAccess.clear();
    }
}
