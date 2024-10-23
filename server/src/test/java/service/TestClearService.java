package service;

import dataaccess.*;
import org.junit.jupiter.api.Test;


public class TestClearService {


    @Test
    public void test_clear() throws DataAccessException {
        FakeDataGenerator.createDataInMemory();
        ClearService.clear();

        assert UserMemoryDAO.instance.getUser("FelixTheCat") == null;
        assert GameMemoryDAO.instance.listGames().isEmpty();
        assert AuthMemoryDAO.instance.getAuth("coolest authToken ever") == null;
    }
}
