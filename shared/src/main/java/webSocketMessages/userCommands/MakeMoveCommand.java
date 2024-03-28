package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMoveCommand extends  UserGameCommand{
    Integer gameID;
    ChessMove move;
    public MakeMoveCommand(String authToken, Integer gameID, ChessMove move) {
        super(authToken);
        this.gameID = gameID;
        this.move = move;
        this.commandType = CommandType.MAKE_MOVE;
    }
    public Integer getGameID(){
        return this.gameID;
    }
    public ChessMove getMove(){
        return this.move;
    }
}
