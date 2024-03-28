package webSocketMessages.userCommands;

public class LeaveCommand extends UserGameCommand{
    public LeaveCommand(String authToken, Integer gameID) {
        super(authToken);
        this.gameID = gameID;
        this.commandType = CommandType.LEAVE;
    }
    public LeaveCommand(UserGameCommand command){
        super(command.getAuthString());
        this.gameID = command.gameID;
        this.commandType = CommandType.JOIN_OBSERVER;
    }
    public Integer getGameID(){
        return this.gameID;
    }
}
