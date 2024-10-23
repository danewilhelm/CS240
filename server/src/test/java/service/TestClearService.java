package service;

import dataaccess.*;
import org.junit.jupiter.api.Test;


public class TestClearService {


    @Test
    public void testClear() throws DataAccessException {
        FakeDataGenerator.createDataInMemory();
        ClearService.clear();

        assert UserMemoryDAO.INSTANCE.getUser("FelixTheCat") == null;
        assert GameMemoryDAO.INSTANCE.listGames().isEmpty();
        assert AuthMemoryDAO.INSTANCE.getAuth("coolest authToken ever") == null;
    }
}
