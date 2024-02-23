package dataAccess;
import model.AuthData;

import java.util.Map;

public interface AuthDAO {
    void clear() throws DataAccessException;
    String createAuth(String username) throws DataAccessException; // Create a new authorization and return it
    AuthData getAuth(String authToken) throws DataAccessException; // Retrieve an authorization given an authToken.

    String getUsername(String authToken) throws DataAccessException;

    void deleteAuth(String authToken) throws DataAccessException; // Delete an authorization so that it is no longer valid.
    public Map<String, AuthData> getAuthTokens() throws DataAccessException;
}
