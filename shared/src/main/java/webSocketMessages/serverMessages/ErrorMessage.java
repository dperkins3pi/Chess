package webSocketMessages.serverMessages;

import chess.ChessGame;

public class ErrorMessage extends ServerMessage{
    public ErrorMessage(String errorMessage) {
        super(ServerMessageType.ERROR);
        this.errorMessage = errorMessage;
    }

    public ErrorMessage(ServerMessage serverMessage) {
        super(ServerMessage.ServerMessageType.ERROR);
        this.errorMessage = serverMessage.errorMessage;
    }

    public String getErrorMessage(){
        return this.errorMessage;
    }
}
