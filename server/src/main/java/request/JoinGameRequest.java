package request;

public class JoinGameRequest {
    String playerColor;
    Integer gameID;

    public String getPlayerColor() {
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
