package chess;

import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private final ChessPiece[][] squares = new ChessPiece[8][8];
    public ChessBoard() {
        
    }
    public ChessBoard(ChessBoard board) {  //create a copy of the board
        for(int i = 1; i <= 8; i++){
            for(int j = 1; j <= 8; j++){
                var position = new ChessPosition(i, j);
                var piece = board.getPiece(position);
                this.addPiece(position, piece);
            }
        }
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow() - 1][position.getColumn() - 1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        var position = new ChessPosition(0, 0);
        // Empty board
        for (int i=0; i<8; i++){
            for (int j=0; j<8; j++){
                squares[i][j] = null;
            }
        }
        // Add pieces
        // Pawns
        var white_piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        var black_piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        for (int i=1; i<=8; i++){
            position = new ChessPosition(2, i);
            addPiece(position, white_piece);
            position = new ChessPosition(7, i);
            addPiece(position, black_piece);
        }
        // Rooks
        white_piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        black_piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        position = new ChessPosition(1, 1);
        addPiece(position, white_piece);
        position = new ChessPosition(1, 8);
        addPiece(position, white_piece);
        position = new ChessPosition(8, 1);
        addPiece(position, black_piece);
        position = new ChessPosition(8, 8);
        addPiece(position, black_piece);
        // Knights
        white_piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        black_piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        position = new ChessPosition(1, 2);
        addPiece(position, white_piece);
        position = new ChessPosition(1, 7);
        addPiece(position, white_piece);
        position = new ChessPosition(8, 2);
        addPiece(position, black_piece);
        position = new ChessPosition(8, 7);
        addPiece(position, black_piece);
        // Bishops
        white_piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        black_piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        position = new ChessPosition(1, 3);
        addPiece(position, white_piece);
        position = new ChessPosition(1, 6);
        addPiece(position, white_piece);
        position = new ChessPosition(8, 3);
        addPiece(position, black_piece);
        position = new ChessPosition(8, 6);
        addPiece(position, black_piece);
        // Queens
        white_piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        black_piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        position = new ChessPosition(1, 4);
        addPiece(position, white_piece);
        position = new ChessPosition(8, 4);
        addPiece(position, black_piece);
        // Kings
        white_piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        black_piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        position = new ChessPosition(1, 5);
        addPiece(position, white_piece);
        position = new ChessPosition(8, 5);
        addPiece(position, black_piece);
    }

    @Override
    public String toString(){
        String the_string = "";
        for (int i=0; i<8; i++){  // Go through the rows
            the_string += "|";
            for (int j=0; j<8; j++){  // Go through the columns
                if (squares[i][j] == null){
                    the_string += " |";
                }
                else {
                    the_string += squares[i][j].toString() + "|";
                }
            }
            the_string += "\n";  // Add new line
        }
        return the_string;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(squares);
    }
}
