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
        HashSet<ChessMove> moves = new HashSet<>();  // Store Possible Moves
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        // Check for each piece type
        switch(type) {
            case KING:
                // See if there are valid moves on higher rows
                if(row < 8) {
                    var position = new ChessPosition(row + 1, col);
                    CheckMove(board, myPosition, moves, position);
                    if(col < 8) {
                        var position2 = new ChessPosition(row + 1, col + 1);
                        CheckMove(board, myPosition, moves, position2);
                    }
                    if(col > 1) {
                        var position3 = new ChessPosition(row + 1, col - 1);
                        CheckMove(board, myPosition, moves, position3);
                    }
                }
                // See if there are valid moves on lower rows
                if(row > 1) {
                    var position4 = new ChessPosition(row - 1, col);
                    CheckMove(board, myPosition, moves, position4);
                    if(col < 8) {
                        var position5 = new ChessPosition(row - 1, col + 1);
                        CheckMove(board, myPosition, moves, position5);
                    }
                    if(col > 1) {
                        var position6 = new ChessPosition(row - 1, col - 1);
                        CheckMove(board, myPosition, moves, position6);
                    }
                }
                // See if there are valid moves along the same row
                if(col < 8) {
                    var position7 = new ChessPosition(row, col + 1);
                    CheckMove(board, myPosition, moves, position7);
                }
                if(col > 1) {
                    var position7 = new ChessPosition(row, col - 1);
                    CheckMove(board, myPosition, moves, position7);
                }
                break;
            case QUEEN:
                // Check down-left direction
                for (int i = 1; i < 8; i++) {
                    if(row - i >= 1 && col - i >= 1) {
                        var position = new ChessPosition(row - i, col - i);
                        if (CheckMove(board, myPosition, moves, position)) break;
                    }
                    else break;
                }
                // Check up-right direction
                for (int i = 1; i < 8; i++) {
                    if(row - i >= 1 && col + i <= 8) {
                        var position = new ChessPosition(row - i, col + i);
                        if (CheckMove(board, myPosition, moves, position)) break;
                    }
                    else break;
                }
                // Check up-left direction
                for (int i = 1; i < 8; i++) {
                    if(row + i <= 8 && col - i >= 1) {
                        var position = new ChessPosition(row + i, col - i);
                        if (CheckMove(board, myPosition, moves, position)) break;
                    }
                    else break;
                }
                // Check down-right direction
                for (int i = 1; i < 8; i++) {
                    if(row + i <= 8 && col + i <= 8) {
                        var position = new ChessPosition(row + i, col + i);
                        if (CheckMove(board, myPosition, moves, position)) break;
                    }
                    else break;
                }
                // Check down direction
                for (int i = 1; i < 8; i++) {
                    if(row + i <= 8) {
                        var position = new ChessPosition(row + i, col);
                        if (CheckMove(board, myPosition, moves, position)) break;
                    }
                    else break;
                }
                // Check up direction
                for (int i = 1; i < 8; i++) {
                    if(row - i >= 1) {
                        var position = new ChessPosition(row - i, col);
                        if (CheckMove(board, myPosition, moves, position)) break;
                    }
                    else break;
                }
                // Check left direction
                for (int i = 1; i < 8; i++) {
                    if(col - i >= 1) {
                        var position = new ChessPosition(row, col - i);
                        if (CheckMove(board, myPosition, moves, position)) break;
                    }
                    else break;
                }
                // Check right direction
                for (int i = 1; i < 8; i++) {
                    if(col + i <= 8) {
                        var position = new ChessPosition(row, col + i);
                        if (CheckMove(board, myPosition, moves, position)) break;
                    }
                    else break;
                }
                break;
            case BISHOP:
                // Check down-left direction
                for (int i = 1; i < 8; i++) {
                    if(row - i >= 1 && col - i >= 1) {
                        var position = new ChessPosition(row - i, col - i);
                        if (CheckMove(board, myPosition, moves, position)) break;
                    }
                    else break;
                }
                // Check up-right direction
                for (int i = 1; i < 8; i++) {
                    if(row - i >= 1 && col + i <= 8) {
                        var position = new ChessPosition(row - i, col + i);
                        if (CheckMove(board, myPosition, moves, position)) break;
                    }
                    else break;
                }
                // Check up-left direction
                for (int i = 1; i < 8; i++) {
                    if(row + i <= 8 && col - i >= 1) {
                        var position = new ChessPosition(row + i, col - i);
                        if (CheckMove(board, myPosition, moves, position)) break;
                    }
                    else break;
                }
                // Check down-right direction
                for (int i = 1; i < 8; i++) {
                    if(row + i <= 8 && col + i <= 8) {
                        var position = new ChessPosition(row + i, col + i);
                        if (CheckMove(board, myPosition, moves, position)) break;
                    }
                    else break;
                }
                break;
            case KNIGHT:
                // Down Right
                if(row + 1 <= 8 && col + 2 <= 8) {
                    var position = new ChessPosition(row + 1, col + 2);
                    CheckMove(board, myPosition, moves, position);
                }
                if(row + 2 <= 8 && col + 1 <= 8) {
                    var position2 = new ChessPosition(row + 2, col + 1);
                    CheckMove(board, myPosition, moves, position2);
                }
                // Up Right
                if(row - 1 >= 1 && col + 2 <= 8) {
                    var position3 = new ChessPosition(row - 1, col + 2);
                    CheckMove(board, myPosition, moves, position3);
                }
                if(row - 2 >= 1 && col + 1 <= 8) {
                    var position4 = new ChessPosition(row - 2, col + 1);
                    CheckMove(board, myPosition, moves, position4);
                }
                // Down Left
                if(row + 1 <= 8 && col - 2 >= 1) {
                    var position5 = new ChessPosition(row + 1, col - 2);
                    CheckMove(board, myPosition, moves, position5);
                }
                if(row + 2 <= 8 && col - 1 >= 1) {
                    var position6 = new ChessPosition(row + 2, col - 1);
                    CheckMove(board, myPosition, moves, position6);
                }
                // Up Right
                if(row - 1 >= 1 && col - 2 >= 1) {
                    var position7 = new ChessPosition(row - 1, col - 2);
                    CheckMove(board, myPosition, moves, position7);
                }
                if(row - 2 >= 1 && col - 1 >= 1) {
                    var position8 = new ChessPosition(row - 2, col - 1);
                    CheckMove(board, myPosition, moves, position8);
                }
                break;
            case ROOK:
                // Check down direction
                for (int i = 1; i < 8; i++) {
                    if(row + i <= 8) {
                        var position = new ChessPosition(row + i, col);
                        if (CheckMove(board, myPosition, moves, position)) break;
                    }
                    else break;
                }
                // Check up direction
                for (int i = 1; i < 8; i++) {
                    if(row - i >= 1) {
                        var position = new ChessPosition(row - i, col);
                        if (CheckMove(board, myPosition, moves, position)) break;
                    }
                    else break;
                }
                // Check left direction
                for (int i = 1; i < 8; i++) {
                    if(col - i >= 1) {
                        var position = new ChessPosition(row, col - i);
                        if (CheckMove(board, myPosition, moves, position)) break;
                    }
                    else break;
                }
                // Check right direction
                for (int i = 1; i < 8; i++) {
                    if(col + i <= 8) {
                        var position = new ChessPosition(row, col + i);
                        if (CheckMove(board, myPosition, moves, position)) break;
                    }
                    else break;
                }
                break;
            case PAWN:
                break;
        }

        return moves;
    }

    /**
     * Checks if a position is a valid move
     * If it is valid, adds it to the list of moves
     *
     * @return true if move is blocked or invalid
     */
    private boolean CheckMove(ChessBoard board, ChessPosition myPosition, HashSet<ChessMove> moves, ChessPosition position) {
        if(board.getPiece(position) == null) {  // hit empty square
            var new_move = new ChessMove(myPosition, position, type);
            moves.add(new_move);
        }
        else if(board.getPiece(position).getTeamColor() != this.getTeamColor()){  // hit piece on oposite team
            var new_move = new ChessMove(myPosition, position, type);
            moves.add(new_move);
            return true;
        }
        else {   // hit piece of same team
            return true;
        }
        return false;
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
