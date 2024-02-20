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
    public void createUser(String username) throws DataAccessException {

    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }
}
