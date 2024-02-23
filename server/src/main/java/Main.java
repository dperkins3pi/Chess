import chess.*;
import server.Server;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        Server the_server = new Server();
        the_server.run(1235);   //To run, go to http://localhost:1234

        System.out.println("♕ 240 Chess Server: " + piece);  // Checks if the code actually ran
    }
}