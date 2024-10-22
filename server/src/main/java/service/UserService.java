package service;

import dataaccess.UserMemoryDAO;
import model.AuthData;
import model.UserData;
import request.LoginRequest;
import request.RegisterRequest;
import result.LoginResult;
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

        return new RegisterResult(request.username(), authToken);
    }

    public LoginResult login(LoginRequest request) {
        return null;
    }

    public void logout(AuthData auth) {

    }
}