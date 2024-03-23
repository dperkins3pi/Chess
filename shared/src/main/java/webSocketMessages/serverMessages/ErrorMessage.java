package webSocketMessages.serverMessages;

import chess.ChessGame;

public class ErrorMessage extends ServerMessage{
    String message;
    public ErrorMessage(String message) {
        super(ServerMessageType.ERROR);
        this.message = message;
    }
}
