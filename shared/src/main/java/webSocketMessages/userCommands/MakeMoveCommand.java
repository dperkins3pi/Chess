package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand{
    public MakeMoveCommand(String authToken, Integer gameID, ChessMove move) {
        super(authToken);
        this.gameID = gameID;
        this.move = move;
        this.commandType = CommandType.MAKE_MOVE;
    }
    public MakeMoveCommand(UserGameCommand command) {
        super(command.getAuthString());
        this.gameID = command.gameID;
        this.move = command.move;
        this.commandType = CommandType.MAKE_MOVE;
    }

    public Integer getGameID(){
        return this.gameID;
    }
    public ChessMove getMove(){
        return this.move;
    }
}
