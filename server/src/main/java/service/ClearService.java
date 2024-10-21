package service;


import dataaccess.AuthMemoryDAO;

public class ClearService {
    /*
    Services the logic for 1 endpoint: clear
     */
    public void clear() {
        AuthMemoryDAO authAccess = new AuthMemoryDAO();
        authAccess.clear();
    }
}
