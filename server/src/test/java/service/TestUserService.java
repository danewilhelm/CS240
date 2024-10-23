package service;

import org.junit.jupiter.api.Test;
import request.LoginRequest;
import request.LogoutRequest;
import request.RegisterRequest;
import result.LoginResult;
import result.RegisterResult;

public class TestUserService {
    static final String felixUsername = "FelixTheCat";
    static final String felixPassword = "123456";
    static final String felixEmail = "rando@gmail.com";

    public static RegisterResult exampleRegisterFelix() {
        RegisterRequest request = new RegisterRequest(felixUsername, felixPassword, felixEmail);
        return UserService.register(request);
    }

    @Test
    public void goodRegister() {
        RegisterResult actual = exampleRegisterFelix();
        assert actual.authToken() != null;
        assert actual.username().equals(felixUsername);
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

        LoginRequest request = new LoginRequest(felixUsername, felixPassword);
        LoginResult actual = UserService.login(request);

        assert actual.authToken() != null;
        assert actual.username().equals(felixUsername);
        ClearService.clear();
    }

    @Test
    public void badLogin() {
        exampleRegisterFelix();
        try {
            UserService.login(new LoginRequest(felixUsername, "wrong password"));
        } catch (UnauthorizedException e) {
            // test passed
        }

        try {
            UserService.login(new LoginRequest("wrong username", felixPassword));
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
