package webSocketMessages.userCommands;

import chess.ChessMove;

public class ResignCommand extends UserGameCommand{
    public ResignCommand(String authToken, Integer gameID) {
        super(authToken);
        this.gameID = gameID;
        this.commandType = CommandType.RESIGN;
    }

    public ResignCommand(UserGameCommand command){
        super(command.getAuthString());
        this.gameID = command.gameID;
        this.commandType = CommandType.JOIN_OBSERVER;
    }
    public Integer getGameID(){
        return this.gameID;
    }
}
