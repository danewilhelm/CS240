package dataaccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class UserMemoryDAO implements UserDAO {
    private final Map<String, UserData> userDataMap = new HashMap<>();

    @Override
    public void clear() {
        userDataMap.clear();
    }

    @Override
    public void createUser(UserData newUser) {
        userDataMap.put(newUser.username(), newUser);
    }

    @Override
    public UserData getUser(String username) {
        return userDataMap.get(username);
    }
}
