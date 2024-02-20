package model;
import java.util.UUID;   // Used to get random auth token

public record AuthData(String authToken, String Username){
    // Move this to the Service code
    public AuthData createAuthToken(String username){  // Creates a new AuthData object with a new authtoken
        String new_token = UUID.randomUUID().toString();
        return new AuthData(new_token, username);
    }
}