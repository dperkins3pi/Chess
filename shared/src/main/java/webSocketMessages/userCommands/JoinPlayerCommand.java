package webSocketMessages.userCommands;

public class JoinPlayerCommand extends UserGameCommand{
    public JoinPlayerCommand(String authToken, Integer gameID, String teamColor) {
        super(authToken);
        this.gameID = gameID;
        this.teamColor = teamColor;
        this.commandType = CommandType.JOIN_PLAYER;
    }
    public Integer getGameID(){
        return this.gameID;
    }
    public String getTeamColor(){
        return this.teamColor;
    }
}
