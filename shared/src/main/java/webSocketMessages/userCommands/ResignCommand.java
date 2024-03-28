package webSocketMessages.userCommands;

import chess.ChessMove;

public class ResignCommand extends UserGameCommand{
    public ResignCommand(String authToken, Integer gameID) {
        super(authToken);
        this.gameID = gameID;
        this.commandType = CommandType.RESIGN;
    }
    public Integer getGameID(){
        return this.gameID;
    }
}
