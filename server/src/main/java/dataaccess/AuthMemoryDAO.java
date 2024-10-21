package dataaccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AuthMemoryDAO implements AuthDAO {

    Map<String, AuthData> authMap = new HashMap<>();


    @Override
    public void clear() {
        authMap.clear();
    }

    @Override
    public void createAuth(AuthData authData) {
        authMap.put(authData.authToken(), authData);
    }

    @Override
    public AuthData getAuth(String authToken) {
        return authMap.get(authToken);
    }

    @Override
    public void deleteAuth(String authToken) {
        authMap.remove(authToken);
    }
}
