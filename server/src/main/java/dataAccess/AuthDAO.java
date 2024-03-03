package dataAccess;
import exceptions.AlreadyTakenException;
import exceptions.UnauthorizedException;
import model.AuthData;

import java.util.Map;

public interface AuthDAO {
    void clear() throws DataAccessException;
    String createAuth(String username) throws DataAccessException, AlreadyTakenException; // Create a new authorization and return it
    AuthData getAuth(String authToken) throws DataAccessException, UnauthorizedException; // Retrieve an authorization given an authToken.

    String getUsername(String authToken) throws DataAccessException, UnauthorizedException;

    void deleteAuth(String authToken) throws DataAccessException, UnauthorizedException; // Delete an authorization so that it is no longer valid.
}
