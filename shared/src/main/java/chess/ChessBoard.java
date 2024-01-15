package chess;

import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private ChessPiece[][] squares = new ChessPiece[8][8];
    public ChessBoard() {
        
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
        // Set all to Null
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                this.squares[i][j] = null;
            }
        }
        var white_piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        var black_piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        for(int i = 1; i <= 8; i++){  // The PAWNS
            var position = new ChessPosition(2, i);
            this.addPiece(position, white_piece);
            position = new ChessPosition(7, i);
            this.addPiece(position, black_piece);
        }
        // ROOKS
        white_piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        black_piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        var position = new ChessPosition(1, 1);
        this.addPiece(position, white_piece);
        position = new ChessPosition(1, 8);
        this.addPiece(position, white_piece);
        position = new ChessPosition(8, 1);
        this.addPiece(position, black_piece);
        position = new ChessPosition(8, 8);
        this.addPiece(position, black_piece);
        //Knights
        white_piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        black_piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        position = new ChessPosition(1, 2);
        this.addPiece(position, white_piece);
        position = new ChessPosition(1, 7);
        this.addPiece(position, white_piece);
        position = new ChessPosition(8, 2);
        this.addPiece(position, black_piece);
        position = new ChessPosition(8, 7);
        this.addPiece(position, black_piece);
        //Bishops
        white_piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        black_piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        position = new ChessPosition(1, 3);
        this.addPiece(position, white_piece);
        position = new ChessPosition(1, 6);
        this.addPiece(position, white_piece);
        position = new ChessPosition(8, 3);
        this.addPiece(position, black_piece);
        position = new ChessPosition(8, 6);
        this.addPiece(position, black_piece);
        //Queens
        white_piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        black_piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        position = new ChessPosition(1, 4);
        this.addPiece(position, white_piece);
        position = new ChessPosition(8, 4);
        this.addPiece(position, black_piece);
        //Kings
        white_piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        black_piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        position = new ChessPosition(1, 5);
        this.addPiece(position, white_piece);
        position = new ChessPosition(8, 5);
        this.addPiece(position, black_piece);
    }

    @Override
    public String toString() {
        var the_string = "";
        for(int i = 0; i < 8; i++){
            the_string += "|";
            for(int j = 0; j < 8; j++){
                if(squares[i][j] == null) the_string += " ";
                else the_string += squares[i][j].toString();
                the_string += "|";
            }
            the_string += "\n";
        }
        return the_string;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.equals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(squares);
    }
}
