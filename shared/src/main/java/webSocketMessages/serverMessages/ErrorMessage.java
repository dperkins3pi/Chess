package webSocketMessages.serverMessages;

import chess.ChessGame;

public class ErrorMessage extends ServerMessage{
    public ErrorMessage(String message) {
        super(ServerMessageType.ERROR);
        this.message = message;
    }

    public ErrorMessage(ServerMessage serverMessage) {
        super(ServerMessage.ServerMessageType.ERROR);
        this.message = serverMessage.message;
    }
}
