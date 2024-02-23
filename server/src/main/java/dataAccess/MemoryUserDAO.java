package dataAccess;
import model.UserData;
import java.util.HashMap;
import java.util.Map;


public class MemoryUserDAO implements UserDAO{
    // Hash map of each username as a string to a UserData object
    private static final Map<String, UserData> users = new HashMap<>();

    @Override
    public void clear() throws DataAccessException{
        try{
            users.clear();
        } catch (Exception e){
            throw new DataAccessException("Data Access Exception");
        }
    }

    @Override   // Makes a user
    public void createUser(String username, String password, String email) throws DataAccessException {
        try {
            UserData user = new UserData(username, password, email);
            users.put(username, user);
        } catch (Exception e){
            throw new DataAccessException("Data Access Exception");
        }
    }

    @Override   // Gets a user given the username
    public UserData getUser(String username) throws DataAccessException {
        try {
            // If it has the username, return the UserData Object, else return null
            return users.getOrDefault(username, null);
        } catch (Exception e){
            throw new DataAccessException("Data Access Exception");
        }
    }

    @Override   // Returns all the users
    public Map<String, UserData> getUsers() throws DataAccessException {
        try{
            return users;
        } catch (Exception e){
            throw new DataAccessException("Data Access Exception");
        }
    }
}
