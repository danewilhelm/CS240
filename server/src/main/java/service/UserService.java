package service;

import dataaccess.AuthMemoryDAO;
import dataaccess.UserMemoryDAO;
import model.AuthData;
import model.UserData;
import request.LoginRequest;
import request.LogoutRequest;
import request.RegisterRequest;
import result.LoginResult;
import result.LogoutResult;
import result.RegisterResult;

import java.util.UUID;


public class UserService {
//    Services the logic for 3 endpoints
    public static RegisterResult register(RegisterRequest request) throws BadRequestException, AlreadyTakenException {
        if (request.username() == null || request.email() == null || request.password() == null) {
            throw new BadRequestException("Error: bad request");
        }

        if (UserMemoryDAO.instance.getUser(request.username()) != null) {
            throw new AlreadyTakenException("Error: already taken");
        }

        UserData user = new UserData(request.username(), request.password(), request.email());
        UserMemoryDAO.instance.createUser(user);

        String authToken = UUID.randomUUID().toString();
        AuthData auth = new AuthData(request.username(), authToken);
        AuthMemoryDAO.instance.createAuth(auth);

        return new RegisterResult(request.username(), authToken);
    }

    public static LoginResult login(LoginRequest request) {
        UserData attemptedUser = UserMemoryDAO.instance.getUser(request.username());
        if (attemptedUser == null || ! attemptedUser.password().equals(request.password())) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        String authToken = UUID.randomUUID().toString();
        AuthData auth = new AuthData(request.username(), authToken);
        AuthMemoryDAO.instance.createAuth(auth);

        return new LoginResult(request.username(), authToken);
    }

    public static LogoutResult logout(LogoutRequest request) {
        if (AuthMemoryDAO.instance.getAuth(request.authToken()) == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        AuthMemoryDAO.instance.deleteAuth(request.authToken());
        return new LogoutResult();
    }
}