package dataaccess;

import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import service.FakeServer;

public class TestUserDAO {

    private final UserData FELIX_USER = FakeServer.generateFelixUserData();

    @AfterEach
    public void clearAllData() throws DataAccessException {
        FakeServer.CLEAR_SERVICE.clear();
    }

    @Test
    public void goodClear() throws DataAccessException {
        FakeServer.USER_DAO.createUser(FELIX_USER);
        FakeServer.USER_DAO.clear();
        assert FakeServer.USER_DAO.getUser(FakeServer.FELIX_THE_CAT) == null;
    }

    @Test
    public void goodCreateUser() throws DataAccessException {
        FakeServer.USER_DAO.createUser(FELIX_USER);
    }

    @Test
    public void badCreateUser() {
        boolean exceptionThrown = false;
        try {
            FakeServer.USER_DAO.createUser(FELIX_USER);
            FakeServer.USER_DAO.createUser(FELIX_USER);
        } catch (DataAccessException e) {
            exceptionThrown = true;
        }
        assert exceptionThrown;
    }

    @Test
    public void goodGetUser() throws DataAccessException {
        FakeServer.USER_DAO.createUser(FELIX_USER);
        assert FakeServer.USER_DAO.getUser(FakeServer.FELIX_THE_CAT).equals(FELIX_USER);
    }



    @Test
    public void badGetUser() throws DataAccessException {
        FakeServer.USER_DAO.createUser(FELIX_USER);
        assert FakeServer.USER_DAO.getUser("wrong username") == null;
    }
}
