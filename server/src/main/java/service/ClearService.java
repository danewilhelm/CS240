package service;


import dataaccess.*;

public class ClearService {
    public static void clear() {
        UserMemoryDAO.instance.clear();
        GameMemoryDAO.instance.clear();
        AuthMemoryDAO.instance.clear();

        // if the database was not accessible, throw a DataAccessException
        // else, return nothing
    }
}
