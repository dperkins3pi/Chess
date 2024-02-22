package dataAccess;
import model.UserData;

import java.util.Map;

public interface UserDAO {
    void clear() throws DataAccessException;
    void createUser(String username, String password, String email) throws DataAccessException;
    UserData getUser(String username) throws DataAccessException; // Get a user

    Map<String, UserData> getUsers();
}
