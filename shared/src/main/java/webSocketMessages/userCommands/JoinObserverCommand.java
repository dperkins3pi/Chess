package webSocketMessages.userCommands;

public class JoinObserverCommand extends UserGameCommand{
    public JoinObserverCommand(String authToken, Integer gameID) {
        super(authToken);
        this.gameID = gameID;
        this.commandType = CommandType.JOIN_OBSERVER;
    }
    public JoinObserverCommand(UserGameCommand command){
        super(command.getAuthString());
        this.gameID = command.gameID;
        this.commandType = CommandType.JOIN_OBSERVER;
    }

    public Integer getGameID(){
        return this.gameID;
    }
}
