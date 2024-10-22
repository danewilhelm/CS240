package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Test;


public class TestClearService {


    public void generate_data() throws DataAccessException {
        UserData catUser = new UserData("FelixTheCat", "123456", "rando@gmail.com");
        UserMemoryDAO.instance.createUser(catUser);

        GameMemoryDAO.instance.createGame("1st chess match ever");
        GameMemoryDAO.instance.createGame("2nd chess match ever");

        AuthData authDataObj = new AuthData("coolest authToken ever", "FelixTheCat");
        AuthMemoryDAO.instance.createAuth(authDataObj);
    }

    @Test
    public void test_clear() throws DataAccessException {
        generate_data();
        ClearService.clear();

        assert UserMemoryDAO.instance.getUser("FelixTheCat") == null;
        assert GameMemoryDAO.instance.listGames().isEmpty();
        assert AuthMemoryDAO.instance.getAuth("coolest authToken ever") == null;
    }


}
