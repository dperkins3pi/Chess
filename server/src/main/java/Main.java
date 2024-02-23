import chess.*;
import server.Server;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        Server theServer = new Server();
        theServer.run(8080);   //To run, go to http://localhost:8080

        System.out.println("♕ 240 Chess Server: " + piece);  // Checks if the code actually ran
    }
}