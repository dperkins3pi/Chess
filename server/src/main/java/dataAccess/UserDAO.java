package dataAccess;
import model.UserData;

public interface UserDAO {
    void clear() throws DataAccessException;
    void createUser(String username) throws DataAccessException;  // Create a new user.
    UserData getUser(String username) throws DataAccessException; // Get a user
}
