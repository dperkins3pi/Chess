package webSocketMessages.userCommands;

public class JoinPlayerCommand extends UserGameCommand{
    public JoinPlayerCommand(String authToken, Integer gameID, String teamColor) {
        super(authToken);
        this.gameID = gameID;
        this.teamColor = teamColor;
        this.commandType = CommandType.JOIN_PLAYER;
    }

    public JoinPlayerCommand(UserGameCommand command){
        super(command.getAuthString());
        this.gameID = command.gameID;
        this.teamColor = command.teamColor;
        this.commandType = CommandType.JOIN_OBSERVER;
    }
    public Integer getGameID(){
        return this.gameID;
    }
    public String getTeamColor(){
        return this.teamColor;
    }
}
