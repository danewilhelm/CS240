package service;

import org.junit.jupiter.api.Test;
import request.LoginRequest;
import request.RegisterRequest;
import result.RegisterResult;

public class TestUserService {


    public RegisterResult registerExample() {
        RegisterRequest request = new RegisterRequest("FelixTheCat", "123456", "rando@gmail.com");
        return UserService.register(request);
    }

    @Test
    public void normalRegister() {
        RegisterResult actual = registerExample();
        assert actual.username().equals("FelixTheCat");
        assert actual.authToken() != null;
    }

    @Test
    public void registerWhenAlreadyRegistered() {
        registerExample();
        try {
            registerExample();
        } catch (AlreadyTakenException e) {
            // test passed
        }
    }

    public void normalLogin() {
        LoginRequest request = new LoginRequest("FelixTheCat", "123456");
        LoginRequest actualResult = UserService.login(request);
        RegisterResult registerResult = UserService.register(request);
        RegisterResult  = UserService.register(request);
    }


}
