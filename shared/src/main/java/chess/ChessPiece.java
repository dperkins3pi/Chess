package chess;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */

    public HashSet<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        // Initialize data
        HashSet<ChessMove> moves = new HashSet<ChessMove>();  // Store Possible Moves
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        // Check for each piece type
        switch(type) {
            case KING:
                break;
            case QUEEN:
                break;
            case BISHOP:
                // Check down-left direction
                for (int i = 1; i < 8; i++) {
                    if(row - i >= 1 && col - i >= 1) {
                        var position = new ChessPosition(row - i, col - i);
                        if(board.getPiece(position) == null) {  // hit empty square
                            var new_move = new ChessMove(myPosition, position, type);
                            moves.add(new_move);
                        }
                        else if(board.getPiece(position).getTeamColor() != this.getTeamColor()){  // hit piece on oposite team
                            var new_move = new ChessMove(myPosition, position, type);
                            moves.add(new_move);
                            break;  // can't go any farther because piece would be captured
                        }
                        else {   // hit piece of same team
                            break;
                        }
                    }
                    else {
                        break;
                    }
                }
                // Check up-left direction
                for (int i = 1; i < 8; i++) {
                    if(row + i <= 8 && col - i >= 1) {
                        var position = new ChessPosition(row + i, col - i);
                        if(board.getPiece(position) == null) {  // hit empty square
                            var new_move = new ChessMove(myPosition, position, type);
                            moves.add(new_move);
                        }
                        else if(board.getPiece(position).getTeamColor() != this.getTeamColor()){  // hit piece on oposite team
                            var new_move = new ChessMove(myPosition, position, type);
                            moves.add(new_move);
                            break;  // can't go any farther because piece would be captured
                        }
                        else {   // hit piece of same team
                            break;
                        }
                    }
                    else {
                        break;
                    }
                }
                // Check up-right direction
                for (int i = 1; i < 8; i++) {
                    if(row - i >= 1 && col + i <= 8) {
                        var position = new ChessPosition(row - i, col + i);
                        if(board.getPiece(position) == null) {  // hit empty square
                            var new_move = new ChessMove(myPosition, position, type);
                            moves.add(new_move);
                        }
                        else if(board.getPiece(position).getTeamColor() != this.getTeamColor()){  // hit piece on oposite team
                            var new_move = new ChessMove(myPosition, position, type);
                            moves.add(new_move);
                            break;  // can't go any farther because piece would be captured
                        }
                        else {   // hit piece of same team
                            break;
                        }
                    }
                    else {
                        break;
                    }
                }
                // Check down-right direction
                for (int i = 1; i < 8; i++) {
                    if(row + i <= 8 && col + i <= 8) {
                        var position = new ChessPosition(row + i, col + i);
                        if(board.getPiece(position) == null) {  // hit empty square
                            var new_move = new ChessMove(myPosition, position, type);
                            moves.add(new_move);
                        }
                        else if(board.getPiece(position).getTeamColor() != this.getTeamColor()){  // hit piece on oposite team
                            var new_move = new ChessMove(myPosition, position, type);
                            moves.add(new_move);
                            break;  // can't go any farther because piece would be captured
                        }
                        else {   // hit piece of same team
                            break;
                        }
                    }
                    else {
                        break;
                    }
                }
            case KNIGHT:
                break;
            case ROOK:
                break;
            case PAWN:
                break;
        }

        return moves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}
