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
                try{
                        authTokens.clear();
                } catch (Exception e){
                        throw new DataAccessException("Data Access Exception");
                }
        }

        @Override
        public String createAuth(String username) throws DataAccessException {
                try {
                        String newToken = UUID.randomUUID().toString();  // Create authToken
                        AuthData authToken = new AuthData(newToken, username);
                        authTokens.put(newToken, authToken);  // Add it to the map
                        return newToken;
                } catch (Exception e){
                        throw new DataAccessException("Data Access Exception");
                }
        }

        @Override
        public AuthData getAuth(String authToken) throws DataAccessException {
                try{
                        return authTokens.getOrDefault(authToken, null);
                } catch (Exception e){
                        throw new DataAccessException("Data Access Exception");
                }
        }

        @Override
        public String getUsername(String authToken) throws DataAccessException {
                try {
                        AuthData authData = authTokens.getOrDefault(authToken, null);
                        return authData.username();
                } catch (Exception e){
                        throw new DataAccessException("Data Access Exception");
                }
        }

        @Override
        public void deleteAuth(String authToken) throws DataAccessException {
                try{
                        authTokens.remove(authToken);
                } catch (Exception e){
                        throw new DataAccessException("Data Access Exception");
                }
        }
        public Map<String, AuthData> getAuthTokens() throws DataAccessException {
                try {
                        return authTokens;
                } catch (Exception e){
                        throw new DataAccessException("Data Access Exception");
                }
        }
}
