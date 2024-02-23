package request;

public class JoinGameRequest {
    String playerColor;
    Integer gameID;

    public String getPlayerColor() {  // Used to parse in request object
        return playerColor;
    }

    public Integer getGameID() {
        return gameID;
    }

    public JoinGameRequest(String playerColor, Integer gameID) {
        this.playerColor = playerColor;
        this.gameID = gameID;
    }
}
