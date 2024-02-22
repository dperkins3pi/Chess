package dataAccess;
import model.AuthData;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO{
        // Hash map of each authtoken as a string to an AuthData object
        private static final Map<String, AuthData> authTokens = new HashMap<>();

        @Override
        public void clear() throws DataAccessException {
                authTokens.clear();
        }

        @Override
        public String createAuth(String username) throws DataAccessException {
                String new_token = UUID.randomUUID().toString();  // Create authToken
                AuthData auth_token = new AuthData(new_token, username);
                authTokens.put(new_token, auth_token);  // Add it to the map
                return new_token;
        }

        @Override
        public AuthData getAuth(String authToken) throws DataAccessException {
                return authTokens.getOrDefault(authToken, null);
        }

        @Override
        public void deleteAuth(String authToken) throws DataAccessException {

        }
        @Override
        public Map<String, AuthData> getAuthTokens(){
                return authTokens;
        }
}
