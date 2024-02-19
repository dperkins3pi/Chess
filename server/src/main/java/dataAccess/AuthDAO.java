package dataAccess;
import model.AuthData;

public interface AuthDAO {

    void clear() throws DataAccessException;
    void createAuth() throws DataAccessException; // Create a new authorization.
    AuthDAO getAuth(String authToken) throws DataAccessException; // Retrieve an authorization given an authToken.
    void deleteAuth(String authToken) throws DataAccessException; // Delete an authorization so that it is no longer valid.
}
