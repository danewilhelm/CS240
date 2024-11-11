package service;

import dataaccess.*;
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
    private AuthDAO authDAOInstance;
    private GameDAO gameDAOInstance;
    private UserDAO userDAOInstance;

    public UserService(AuthDAO authDAOInstance, GameDAO gameDAOInstance, UserDAO userDAOInstance) {
        this.authDAOInstance = authDAOInstance;
        this.gameDAOInstance = gameDAOInstance;
        this.userDAOInstance = userDAOInstance;
    }

    public RegisterResult register(RegisterRequest request) throws BadRequestException, AlreadyTakenException, DataAccessException {
        if (request.username() == null || request.email() == null || request.password() == null) {
            throw new BadRequestException("Error: bad request");
        }

        if (userDAOInstance.getUser(request.username()) != null) {
            throw new AlreadyTakenException("Error: already taken");
        }

        UserData user = new UserData(request.username(), request.password(), request.email());
        userDAOInstance.createUser(user);

        String authToken = UUID.randomUUID().toString();
        AuthData auth = new AuthData(request.username(), authToken);
        authDAOInstance.createAuth(auth);

        return new RegisterResult(request.username(), authToken);
    }

    public LoginResult login(LoginRequest request) throws DataAccessException {
        UserData attemptedUser = userDAOInstance.getUser(request.username());
        if (attemptedUser == null || ! attemptedUser.password().equals(request.password())) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        String authToken = UUID.randomUUID().toString();
        AuthData auth = new AuthData(request.username(), authToken);
        authDAOInstance.createAuth(auth);

        return new LoginResult(request.username(), authToken);
    }

    public LogoutResult logout(LogoutRequest request) throws DataAccessException {
        if (authDAOInstance.getAuth(request.authToken()) == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        try {
            authDAOInstance.deleteAuth(request.authToken());
        } catch (dataaccess.DataAccessException e) {
            throw new RuntimeException(e);
        }
        return new LogoutResult();
    }
}