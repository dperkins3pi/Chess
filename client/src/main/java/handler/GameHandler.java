package handler;

import chess.ChessGame;
import webSocketMessages.serverMessages.ServerMessage;

public interface GameHandler {
    void updateGame(ChessGame game);
    void printMessage(String message);
}
