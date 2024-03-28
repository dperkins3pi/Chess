package request;

public class JoinGameOutput {
    String state;
    Integer gameID = null;
    String color = null;

    public String getState() {
        return state;
    }

    public Integer getGameID() {
        return gameID;
    }

    public String getColor() {
        return color;
    }

    public JoinGameOutput(String state, Integer gameID, String color) {
        this.state = state;
        this.gameID = gameID;
        this.color = color;
    }
}
