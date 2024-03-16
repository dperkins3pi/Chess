package request;

public class JoinGameAuthRequest {
    String authToken;
    Integer gameID;
    String playerColor;

    public JoinGameAuthRequest(String authToken, Integer id, String color) {
        this.authToken = authToken;
        this.gameID = id;
        this.playerColor = color;
    }

    public String getAuthToken() {
        return authToken;
    }

    public Integer getId() {
        return gameID;
    }

    public String getColor() {
        return playerColor;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
