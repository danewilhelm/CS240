package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Map;

public class AuthMemoryDAO implements AuthDAO {
    private final Map<String, AuthData> authDataMap = new HashMap<>();


    @Override
    public void clear() {
        authDataMap.clear();
    }

    @Override
    public void createAuth(AuthData authData) {
        authDataMap.put(authData.authToken(), authData);
    }

    @Override
    public AuthData getAuth(String authToken) {
        return authDataMap.get(authToken);
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        authDataMap.remove(authToken);
    }
}
