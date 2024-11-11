package handler;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import request.LoginRequest;
import request.LogoutRequest;
import request.RegisterRequest;
import result.ClearResult;
import result.LoginResult;
import result.LogoutResult;
import result.RegisterResult;
import service.*;
import spark.Request;
import spark.Response;

public class UserHandler {

    /*
    Handles 3 endpoints:
        Register
        Login
        Logout
     */

    private final UserService userServiceInstance;

    public UserHandler (AuthDAO authDAOInstance, GameDAO gameDAOInstance, UserDAO userDAOInstance) {
        userServiceInstance = new UserService(authDAOInstance, gameDAOInstance, userDAOInstance);
    }

    public Object handleRegister(Request request, Response response) {
        Gson serializer = new Gson();
        RegisterRequest registerRequest = serializer.fromJson(request.body(), RegisterRequest.class);

        try {
            RegisterResult registerResult = userServiceInstance.register(registerRequest);
            response.status(200);
            return serializer.toJson(registerResult);
        } catch (BadRequestException e) {
            response.status(400);
            return "{ \"message\": \"" + e.getMessage() + "\" }";
        } catch (AlreadyTakenException e) {
            response.status(403);
            return "{ \"message\": \"" + e.getMessage() + "\" }";
        } catch (DataAccessException e) {
            response.status(500);
            return "{ \"message\": \"" + e.getMessage() + "\" }";
        }
    }

    public Object handleLogin(Request request, Response response) {
        Gson serializer = new Gson();
        LoginRequest loginRequest = serializer.fromJson(request.body(), LoginRequest.class);
        try {
            LoginResult loginResult = userServiceInstance.login(loginRequest);
            response.status(200);
            return serializer.toJson(loginResult);
        } catch (BadRequestException e) {
            response.status(400);
            return "{ \"message\": \"" + e.getMessage() + "\" }";
        } catch (UnauthorizedException e) {
            response.status(401);
            return "{ \"message\": \"" + e.getMessage() + "\" }";
        } catch (DataAccessException e) {
            response.status(500);
            return "{ \"message\": \"" + e.getMessage() + "\" }";
        }
    }


    public Object handleLogout(Request request, Response response) {
        Gson serializer = new Gson();
        LogoutRequest logoutRequest = new LogoutRequest(request.headers("authorization"));

        try {
            LogoutResult logoutResult = userServiceInstance.logout(logoutRequest);
            response.status(200);
            return "{}";
        } catch (BadRequestException e) {
            response.status(400);
            return "{ \"message\": \"" + e.getMessage() + "\" }";
        } catch (UnauthorizedException e) {
            response.status(401);
            return "{ \"message\": \"" + e.getMessage() + "\" }";
        } catch (DataAccessException e) {
            response.status(500);
            return "{ \"message\": \"" + e.getMessage() + "\" }";
        }
    }
}
