package service;

import dataaccess.AuthMemoryDAO;
import dataaccess.GameMemoryDAO;
import dataaccess.UserMemoryDAO;
import model.AuthData;
import model.UserData;

public class FakeDataGenerator {

    public static void createDataInMemory() {
        UserMemoryDAO.INSTANCE.createUser(generateCatUserData());
        GameMemoryDAO.INSTANCE.createGame("1st chess match ever");
        GameMemoryDAO.INSTANCE.createGame("2nd chess match ever");
        AuthMemoryDAO.INSTANCE.createAuth(generateCatAuth());
    }

    public static UserData generateCatUserData() {
        return new UserData("FelixTheCat", "123456", "rando@gmail.com");
    }

    public static AuthData generateCatAuth() {
        return new AuthData("FelixTheCat", "coolest authToken ever");
    }
}
