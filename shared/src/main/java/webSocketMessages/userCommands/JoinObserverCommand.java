package webSocketMessages.userCommands;

public class JoinObserverCommand extends  UserGameCommand{
    Integer gameID;
    public JoinObserverCommand(String authToken, Integer gameID) {
        super(authToken);
        this.gameID = gameID;
    }
}
