package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import service.FakeServer;

public class TestAuthDAO {

    private final AuthData FELIX_AUTH = FakeServer.generateFelixAuth();


    @AfterEach
    public void clearAllData() throws DataAccessException {
        FakeServer.CLEAR_SERVICE.clear();
    }


    @Test
    public void goodClear() throws DataAccessException {
        FakeServer.AUTH_DAO.createAuth(FELIX_AUTH);
        FakeServer.AUTH_DAO.clear();
        assert FakeServer.AUTH_DAO.getAuth(FELIX_AUTH.authToken()) == null;
    }

    @Test
    public void goodCreateAuth() throws DataAccessException {
        FakeServer.AUTH_DAO.createAuth(FELIX_AUTH);
    }

    @Test
    public void badCreateAuth() throws DataAccessException {
        // idk what do to do here, test doesn't test anything lol
        AuthData emptyAuth = new AuthData("", "");
        FakeServer.AUTH_DAO.createAuth(emptyAuth);
    }

    @Test
    public void goodGetAuth() throws DataAccessException {
        FakeServer.AUTH_DAO.createAuth(FELIX_AUTH);
        AuthData retrievedAuth = FakeServer.AUTH_DAO.getAuth(FELIX_AUTH.authToken());
        assert retrievedAuth.equals(FELIX_AUTH);
    }

    @Test
    void badGetAuth() throws DataAccessException {
        assert FakeServer.AUTH_DAO.getAuth(FELIX_AUTH.authToken()) == null;
    }

    @Test
    void goodDeleteAuth() throws DataAccessException {
        FakeServer.AUTH_DAO.createAuth(FELIX_AUTH);
        FakeServer.AUTH_DAO.deleteAuth(FELIX_AUTH.authToken());
        assert FakeServer.AUTH_DAO.getAuth(FELIX_AUTH.authToken()) == null;
    }

    @Test
    void badDeleteAuth() throws DataAccessException {
        FakeServer.AUTH_DAO.createAuth(FELIX_AUTH);
        FakeServer.AUTH_DAO.deleteAuth("");
    }


}
