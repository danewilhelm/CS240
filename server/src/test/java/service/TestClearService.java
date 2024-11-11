package service;

import dataaccess.*;
import org.junit.jupiter.api.Test;

public class TestClearService {



    @Test
    public void testClear() throws DataAccessException {
        FakeServer.createFakeData();
        FakeServer.CLEAR_SERVICE.clear();

        assert FakeServer.USER_DAO.getUser("FelixTheCat") == null;
        assert FakeServer.GAME_DAO.listGames().isEmpty();
        assert FakeServer.AUTH_DAO.getAuth("coolest authToken ever") == null;
    }
}
