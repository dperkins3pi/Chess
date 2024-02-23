package response;

import model.GameData;

import java.util.Collection;

public class ResponseClass {
    String username;
    String authToken;
    String message;
    Collection<GameData> games;
    Integer gameID;

    public String getUsername() {
        return username;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getMessage() {
        return message;
    }

    public ResponseClass(String username, String authToken) {
        this.username = username;
        this.authToken = authToken;
    }

    public ResponseClass(String message) {
        this.message = message;
    }

    public ResponseClass(Collection<GameData> games) {
        this.games = games;
    }

    public ResponseClass(int ID) {
        this.gameID = ID;
    }
}
