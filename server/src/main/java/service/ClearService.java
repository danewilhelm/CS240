package service;


import dataaccess.*;
import result.ClearResult;

public class ClearService {
    public static ClearResult clear() {
        UserMemoryDAO.INSTANCE.clear();
        GameMemoryDAO.INSTANCE.clear();
        AuthMemoryDAO.INSTANCE.clear();

        // if the database was not accessible, throw a DataAccessException
        // else, return nothing
        return new ClearResult();
    }
}
