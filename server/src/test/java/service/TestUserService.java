package service;

import org.junit.jupiter.api.Test;
import request.LoginRequest;
import request.LogoutRequest;
import request.RegisterRequest;
import result.LoginResult;
import result.RegisterResult;

public class TestUserService {
    static final String FELIX_THE_CAT = "FelixTheCat";
    static final String FELIX_PASSWORD = "123456";
    static final String FELIX_EMAIL = "rando@gmail.com";

    public static RegisterResult exampleRegisterFelix() {
        RegisterRequest request = new RegisterRequest(FELIX_THE_CAT, FELIX_PASSWORD, FELIX_EMAIL);
        return UserService.register(request);
    }

    @Test
    public void goodRegister() {
        RegisterResult actual = exampleRegisterFelix();
        assert actual.authToken() != null;
        assert actual.username().equals(FELIX_THE_CAT);
        ClearService.clear();
    }

    @Test
    public void badRegisterWhenAlreadyRegistered() {
        exampleRegisterFelix();
        try {
            exampleRegisterFelix();
        } catch (AlreadyTakenException e) {
            // test passed
        }
        ClearService.clear();
    }

    @Test
    public void goodLogin() {
        exampleRegisterFelix();

        LoginRequest request = new LoginRequest(FELIX_THE_CAT, FELIX_PASSWORD);
        LoginResult actual = UserService.login(request);

        assert actual.authToken() != null;
        assert actual.username().equals(FELIX_THE_CAT);
        ClearService.clear();
    }

    @Test
    public void badLogin() {
        exampleRegisterFelix();
        try {
            UserService.login(new LoginRequest(FELIX_THE_CAT, "wrong password"));
        } catch (UnauthorizedException e) {
            // test passed
        }

        try {
            UserService.login(new LoginRequest("wrong username", FELIX_PASSWORD));
        } catch (UnauthorizedException e) {
            // test passed
        }

        try {
            UserService.login(new LoginRequest("wrong username", "wrong password"));
        } catch (UnauthorizedException e) {
            // test passed
        }
    }

    @Test
    public void goodLogout() {
        String authToken = exampleRegisterFelix().authToken();
        LogoutRequest request = new LogoutRequest(authToken);
        UserService.logout(request);
        ClearService.clear();
    }

    @Test
    public void badLogout() throws UnauthorizedException{
        exampleRegisterFelix();
        LogoutRequest request = new LogoutRequest("bad auth");
        try {
            UserService.logout(request);
        } catch (UnauthorizedException e) {
            // test passed
        }
        ClearService.clear();
    }


}
