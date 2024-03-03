package dataAccess;
import exceptions.AlreadyTakenException;
import exceptions.UnauthorizedException;
import model.UserData;

import java.util.Map;

public interface UserDAO {
    void clear() throws DataAccessException;
    void createUser(String username, String password, String email) throws DataAccessException, UnauthorizedException, AlreadyTakenException;
    UserData getUser(String username) throws DataAccessException; // Get a user given a username
}
