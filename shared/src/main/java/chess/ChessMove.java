package chess;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {

    private final ChessPosition startPosition;
    private final ChessPosition endPosition;
    private final ChessPiece.PieceType promotionPiece;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotionPiece = promotionPiece;
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return this.startPosition;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return this.endPosition;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return this.promotionPiece;
    }

    // Override the toString() class to improve readability of errors
    @Override
    public String toString() {
        // Get positions on Chess Board
        var start_x = this.getStartPosition().getRow();
        var start_y = this.getStartPosition().getColumn();
        var end_x = this.getEndPosition().getRow();
        var end_y = this.getEndPosition().getColumn();

        // Convert into String
        var starting_string =  "{" + String.valueOf(start_x) + "," + String.valueOf(start_y) + "}";  // won't use
        var ending_string =  "{" + String.valueOf(end_x) + "," + String.valueOf(end_y) + "}";
        return starting_string + "->" + ending_string;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        else if (obj == this) {
            return true;
        }
        else if (obj.getClass() != this.getClass()) {
            return false;
        }
        ChessMove o = (ChessMove) obj; //to allow access of attribute
        return (this.getStartPosition().equals(o.getStartPosition()) && this.getEndPosition().equals(o.getEndPosition())
        && this.getPromotionPiece().equals(o.getPromotionPiece()));
    }

    @Override
    public int hashCode() {
        int hash = this.getStartPosition().hashCode();
        hash = hash * 31 + this.getStartPosition().hashCode();
        return hash;
    }
}
