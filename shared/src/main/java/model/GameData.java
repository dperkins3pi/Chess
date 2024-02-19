package model;

import chess.ChessGame;

public record GameData() {
    private static int gameID;
    private static String whiteUsername;
    private static String blackUsername;
    private static String gameName;
    private static ChessGame game;
}
