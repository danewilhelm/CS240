package service;

import dataaccess.*;

import model.AuthData;
import model.UserData;
import request.RegisterRequest;
import result.RegisterResult;

public class FakeServer {

    public static final UserDAO USER_DAO = new UserDatabaseDAO();
    public static final AuthDAO AUTH_DAO = new AuthDatabaseDAO();
    public static final GameDAO GAME_DAO = new GameDatabaseDAO();

    public static final UserService USER_SERVICE = new UserService(AUTH_DAO, GAME_DAO, USER_DAO);
    public static final GameService GAME_SERVICE = new GameService(AUTH_DAO, GAME_DAO, USER_DAO);
    public static final ClearService CLEAR_SERVICE = new ClearService(AUTH_DAO, GAME_DAO, USER_DAO);

    public static final String FELIX_THE_CAT = "FelixTheCat";
    public static final String FELIX_PASSWORD = "123456";
    public static final String FELIX_EMAIL = "rando@gmail.com";

    public static final String GAME_NAME_ONE = "1st chess match ever";
    public static final String GAME_NAME_TWO = "2nd chess match ever";

    public static void createFakeData() throws DataAccessException {
            USER_DAO.createUser(generateFelixUserData());
            GAME_DAO.createGame(GAME_NAME_ONE);
            GAME_DAO.createGame(GAME_NAME_TWO);
            AUTH_DAO.createAuth(generateFelixAuth());

    }

    public static UserData generateFelixUserData() {
        return new UserData(FELIX_THE_CAT, FELIX_PASSWORD, FELIX_EMAIL);
    }

    public static AuthData generateFelixAuth() {
        return new AuthData(FELIX_THE_CAT, "coolest authToken ever");
    }

    public static RegisterResult exampleRegisterFelix() throws DataAccessException {
        RegisterRequest request = new RegisterRequest(FELIX_THE_CAT, FELIX_PASSWORD, FELIX_EMAIL);
        return USER_SERVICE.register(request);
    }
}
