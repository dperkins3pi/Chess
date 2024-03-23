package webSocketMessages.userCommands;

public class JoinPlayerCommand extends  UserGameCommand{
    Integer gameID;
    String teamColor;
    public JoinPlayerCommand(String authToken, Integer gameID, String teamColor) {
        super(authToken);
        this.gameID = gameID;
        this.teamColor = teamColor;
    }
}
