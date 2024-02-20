package dataAccess;
import model.AuthData;
import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO implements AuthDAO{
        // Hash map of each authtoken as a string to an AuthData object
        private static final Map<String, AuthData> authTokens = new HashMap<>();

        @Override
        public void clear() throws DataAccessException {
                authTokens.clear();
        }

        @Override
        public void createAuth() throws DataAccessException {

        }

        @Override
        public AuthDAO getAuth(String authToken) throws DataAccessException {
                return null;
        }

        @Override
        public void deleteAuth(String authToken) throws DataAccessException {

        }
}
