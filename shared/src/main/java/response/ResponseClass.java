package response;

public class ResponseClass {   // Used to return response objects
    String username;
    String authToken;
    String message;
    Integer gameID;

    public String getUsername() {
        return username;
    }

    public String getAuthToken() {
        return authToken;
    }

    public Integer getGameID() {return gameID;}

    public ResponseClass(String username, String authToken) {
        this.username = username;
        this.authToken = authToken;
    }

    public ResponseClass(String message) {
        this.message = message;
    }

    public ResponseClass(int id) {
        this.gameID = id;
    }
}
