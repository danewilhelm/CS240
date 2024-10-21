package dataaccess;

import model.UserData;

public interface UserDAO {
    /*
    INTERFACE CLASS

    clear: A method for clearing all data from the database. This is used during testing.
    createUser: Create a new user.
    getUser: Retrieve a user with the given username.
     */

    public void clear();
    public void createUser(UserData newUser);
    public UserData getUser(String username);
}
