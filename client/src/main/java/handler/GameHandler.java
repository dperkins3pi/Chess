package handler;

import chess.ChessGame;
import webSocketMessages.serverMessages.ServerMessage;

public interface GameHandler {
    void updateGame(String message);
    void printMessage(String message);
}
