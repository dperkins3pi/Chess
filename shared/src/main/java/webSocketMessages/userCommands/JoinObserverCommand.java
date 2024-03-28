package webSocketMessages.userCommands;

public class JoinObserverCommand extends  UserGameCommand{
    Integer gameID;
    public JoinObserverCommand(String authToken, Integer gameID) {
        super(authToken);
        this.gameID = gameID;
        this.commandType = CommandType.JOIN_OBSERVER;
    }
    public Integer getGameID(){
        return this.gameID;
    }
}
