package handler;

import com.google.gson.Gson;
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

    public static Object handleRegister(Request request, Response response) {
        Gson serializer = new Gson();
        RegisterRequest registerRequest = serializer.fromJson(request.body(), RegisterRequest.class);

        try {
            RegisterResult registerResult = UserService.register(registerRequest);
            response.status(200);
            return serializer.toJson(registerResult);
        } catch (BadRequestException e) {
            response.status(400);
            return serializer.toJson("{ \"message\": \"" + e.getMessage() + "\" }");
        } catch (AlreadyTakenException e) {
            response.status(403);
            return serializer.toJson("{ \"message\": \"" + e.getMessage() + "\" }");
        }
    }

    public static Object handleLogin(Request request, Response response) {
        Gson serializer = new Gson();
        LoginRequest loginRequest = serializer.fromJson(request.body(), LoginRequest.class);
        try {
            LoginResult loginResult = UserService.login(loginRequest);
            response.status(200);
            return serializer.toJson(loginResult);
        } catch (BadRequestException e) {
            response.status(400);
            return serializer.toJson("{ \"message\": \"" + e.getMessage() + "\" }");
        } catch (UnauthorizedException e) {
            response.status(401);
            return serializer.toJson("{ \"message\": \"" + e.getMessage() + "\" }");
        }
    }


    public static Object handleLogout(Request request, Response response) {
        Gson serializer = new Gson();
        LogoutRequest logoutRequest = new LogoutRequest(request.headers("authorization"));

        try {
            LogoutResult logoutResult = UserService.logout(logoutRequest);
            response.status(200);
            return serializer.toJson("{}");
        } catch (BadRequestException e) {
            response.status(400);
            return serializer.toJson("{ \"message\": \"" + e.getMessage() + "\" }");
        } catch (UnauthorizedException e) {
            response.status(401);
            return serializer.toJson("{ \"message\": \"" + e.getMessage() + "\" }");
        }
    }
}
