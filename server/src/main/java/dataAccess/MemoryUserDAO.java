package dataAccess;
import model.UserData;
import java.util.HashMap;
import java.util.Map;


public class MemoryUserDAO implements UserDAO{
    // Hash map of each username as a string to a UserData object
    private static final Map<String, UserData> users = new HashMap<>();

    @Override
    public void clear() throws DataAccessException{
        users.clear();
    }

    @Override
    public void createUser(String username, String password, String email) throws DataAccessException {
        UserData user = new UserData(username, password, email);
        users.put(username, user);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        // If it has the username, return the UserData Object, else return null
        return users.getOrDefault(username, null);
    }
}
