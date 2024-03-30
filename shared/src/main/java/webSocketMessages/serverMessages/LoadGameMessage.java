package webSocketMessages.serverMessages;

import chess.ChessGame;

public class LoadGameMessage extends ServerMessage{
    public LoadGameMessage(ChessGame game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }
    public LoadGameMessage(ServerMessage serverMessage) {
        super(ServerMessageType.LOAD_GAME);
        this.game = serverMessage.game;
    }

    public ChessGame getGame(){
        return game;
    }
}
