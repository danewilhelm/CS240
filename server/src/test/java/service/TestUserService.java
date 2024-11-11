package service;

import dataaccess.DataAccessException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import request.LoginRequest;
import request.LogoutRequest;
import result.LoginResult;
import result.RegisterResult;

public class TestUserService {

    @AfterEach
    public void clearAllData() throws DataAccessException{
        FakeServer.CLEAR_SERVICE.clear();
    }

    @Test
    public void goodRegister() throws DataAccessException {
        RegisterResult actual = FakeServer.exampleRegisterFelix();
        assert actual.authToken() != null;
        assert actual.username().equals(FakeServer.FELIX_THE_CAT);
    }

    @Test
    public void badRegisterWhenAlreadyRegistered() throws DataAccessException {
        FakeServer.exampleRegisterFelix();
        try {
            FakeServer.exampleRegisterFelix();
        } catch (AlreadyTakenException e) {
            // test passed
        }
    }

    @Test
    public void goodLogin() throws DataAccessException {
        RegisterResult registerResult = FakeServer.exampleRegisterFelix();
        FakeServer.AUTH_DAO.deleteAuth(registerResult.authToken());
        LoginRequest request = new LoginRequest(FakeServer.FELIX_THE_CAT, FakeServer.FELIX_PASSWORD);
        LoginResult actual = FakeServer.USER_SERVICE.login(request);

        assert actual.authToken() != null;
        assert actual.username().equals(FakeServer.FELIX_THE_CAT);
    }

    @Test
    public void badLogin() throws DataAccessException {
        FakeServer.exampleRegisterFelix();
        try {
            FakeServer.USER_SERVICE.login(new LoginRequest(FakeServer.FELIX_THE_CAT, "wrong password"));
        } catch (UnauthorizedException e) {
            // test passed
        }

        try {
            FakeServer.USER_SERVICE.login(new LoginRequest("wrong username", FakeServer.FELIX_PASSWORD));
        } catch (UnauthorizedException e) {
            // test passed
        }

        try {
            FakeServer.USER_SERVICE.login(new LoginRequest("wrong username", "wrong password"));
        } catch (UnauthorizedException e) {
            // test passed
        }
    }

    @Test
    public void goodLogout() throws DataAccessException {
        String authToken = FakeServer.exampleRegisterFelix().authToken();
        LogoutRequest request = new LogoutRequest(authToken);
        FakeServer.USER_SERVICE.logout(request);
    }

    @Test
    public void badLogout() throws UnauthorizedException, DataAccessException {
        FakeServer.exampleRegisterFelix();
        LogoutRequest request = new LogoutRequest("bad auth");
        try {
            FakeServer.USER_SERVICE.logout(request);
        } catch (UnauthorizedException e) {
            // test passed
        }
    }


}
