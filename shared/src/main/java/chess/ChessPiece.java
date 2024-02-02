package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

import static chess.ChessPiece.PieceType.*;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;
    private boolean alreadyMoved;   // Marks true if the piece has already moved

    private boolean passant_possible;   // True if the piece is a pawn that just moved up to
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
        this.alreadyMoved = false;
        this.passant_possible = false;
    }

    public void SetAlreadyMoved(){  // Mark the piece as moved
        this.alreadyMoved = true;
    }

    public boolean NotAlreadyMoved(){
        return !alreadyMoved;
    }

    public void SetPassant(boolean val){  // Mark the piece as moved
        this.passant_possible = val;
    }

    public boolean getPassantPossible(){
        return this.passant_possible;
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
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        // Order Doesn't matter, so we use a Hash set
        HashSet<ChessMove> moves = new HashSet<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        var position = new ChessPosition(0, 0);  //To store positions that will be added
        // Switch statement to find move depending on type of piece
        switch (type) {
            case KING:
                if(!alreadyMoved){
                    if(myPosition.getRow() == 1){
                        var rook1 = board.getPiece(new ChessPosition(1, 1));
                        if (rook1 != null && rook1.NotAlreadyMoved() &&
                                board.getPiece(new ChessPosition(1, 2))==null &&
                                board.getPiece(new ChessPosition(1, 3))==null &&
                                board.getPiece(new ChessPosition(1, 4))==null){
                            var new_position_king = new ChessPosition(1, 3);
                            moves.add(new ChessMove(myPosition, new_position_king, null));  // Add move as a vaid move
                        }
                        var rook2 = board.getPiece(new ChessPosition(1, 8));
                        if (rook2 != null && rook2.NotAlreadyMoved() &&
                                board.getPiece(new ChessPosition(1, 6))==null &&
                                board.getPiece(new ChessPosition(1, 7))==null){
                            var new_position_king = new ChessPosition(1, 7);
                            moves.add(new ChessMove(myPosition, new_position_king, null));  // Add move as a vaid move
                        }
                    }
                    if(myPosition.getRow() == 8){
                        var rook1 = board.getPiece(new ChessPosition(8, 1));
                        if (rook1 != null && rook1.NotAlreadyMoved() &&
                                board.getPiece(new ChessPosition(8, 2))==null &&
                                board.getPiece(new ChessPosition(8, 3))==null &&
                                board.getPiece(new ChessPosition(8, 4))==null){
                            var new_position_king = new ChessPosition(8, 3);
                            moves.add(new ChessMove(myPosition, new_position_king, null));  // Add move as a vaid move
                        }
                        var rook2 = board.getPiece(new ChessPosition(8, 8));
                        if (rook2 != null && rook2.NotAlreadyMoved() &&
                                board.getPiece(new ChessPosition(8, 6))==null &&
                                board.getPiece(new ChessPosition(8, 7))==null){
                            var new_position_king = new ChessPosition(8, 7);
                            moves.add(new ChessMove(myPosition, new_position_king, null));  // Add move as a vaid move
                        }
                    }
                }
                if(row < 8){ //Up =
                    position = new ChessPosition(row+1, col);
                    validMove(board, myPosition, moves, position);
                    if(col < 8){ // Up right
                        position = new ChessPosition(row+1, col+1);
                        validMove(board, myPosition, moves, position);
                    }
                    if(col > 1){  // Up left
                        position = new ChessPosition(row+1, col-1);
                        validMove(board, myPosition, moves, position);
                    }
                }
                if(row > 1){ //Down =
                    position = new ChessPosition(row-1, col);
                    validMove(board, myPosition, moves, position);
                    if(col < 8){ // Down right
                        position = new ChessPosition(row-1, col+1);
                        validMove(board, myPosition, moves, position);
                    }
                    if(col > 1){  // Down left
                        position = new ChessPosition(row-1, col-1);
                        validMove(board, myPosition, moves, position);
                    }
                }
                if(col < 8){ // Right
                    position = new ChessPosition(row, col+1);
                    validMove(board, myPosition, moves, position);
                }
                if(col > 1){  // Left
                    position = new ChessPosition(row, col-1);
                    validMove(board, myPosition, moves, position);
                }
                break;
            case QUEEN:
                // Up Right
                for (int i=1; i + row <= 8 && i + col <= 8; i++){
                    position = new ChessPosition(row+i, col+i);
                    if (validMove(board, myPosition, moves, position)) break;    //Can't go there already taken
                }
                // Up Left
                for (int i=1; i + row <= 8 && col - i >= 1; i++){
                    position = new ChessPosition(row+i, col-i);
                    if (validMove(board, myPosition, moves, position)) break;
                }
                // Down Right
                for (int i=1; row - i >= 1 && col + i <= 8; i++){
                    position = new ChessPosition(row-i, col+i);
                    if (validMove(board, myPosition, moves, position)) break;
                }
                // Down Left
                for (int i=1; row - i >= 1 && col - i >= 1; i++){
                    position = new ChessPosition(row-i, col-i);
                    if (validMove(board, myPosition, moves, position)) break;
                }
                for (int i=1; i + row <= 8; i++) { //Up
                    position = new ChessPosition(row + i, col);
                    if (validMove(board, myPosition, moves, position)) break;
                }
                for (int i=1; row - i >= 1; i++) { //Down
                    position = new ChessPosition(row - i, col);
                    if (validMove(board, myPosition, moves, position)) break;
                }
                for (int i=1; i + col <= 8; i++) { //Right
                    position = new ChessPosition(row, col + i);
                    if (validMove(board, myPosition, moves, position)) break;
                }
                for (int i=1; col - i >= 1; i++) { //Left
                    position = new ChessPosition(row, col - i);
                    if (validMove(board, myPosition, moves, position)) break;
                }
                break;
            case BISHOP:
                // Up Right
                for (int i=1; i + row <= 8 && i + col <= 8; i++){
                    position = new ChessPosition(row+i, col+i);
                    if (validMove(board, myPosition, moves, position)) break;    //Can't go there already taken
                }
                // Up Left
                for (int i=1; i + row <= 8 && col - i >= 1; i++){
                    position = new ChessPosition(row+i, col-i);
                    if (validMove(board, myPosition, moves, position)) break;
                }
                // Down Right
                for (int i=1; row - i >= 1 && col + i <= 8; i++){
                    position = new ChessPosition(row-i, col+i);
                    if (validMove(board, myPosition, moves, position)) break;
                }
                // Down Left
                for (int i=1; row - i >= 1 && col - i >= 1; i++){
                    position = new ChessPosition(row-i, col-i);
                    if (validMove(board, myPosition, moves, position)) break;
                }
                break;
            case KNIGHT:
                if (row + 1 <= 8 && col + 2 <= 8){
                    position = new ChessPosition(row+1, col+2);
                    validMove(board, myPosition, moves, position);
                }
                if (row + 1 <= 8 && col - 2 >= 1){
                    position = new ChessPosition(row+1, col-2);
                    validMove(board, myPosition, moves, position);
                }
                if (row + 2 <= 8 && col + 1 <= 8){
                    position = new ChessPosition(row+2, col+1);
                    validMove(board, myPosition, moves, position);
                }
                if (row + 2 <= 8 && col - 1 >= 1){
                    position = new ChessPosition(row+2, col-1);
                    validMove(board, myPosition, moves, position);
                }
                if (row - 1 >= 1 && col + 2 <= 8){
                    position = new ChessPosition(row-1, col+2);
                    validMove(board, myPosition, moves, position);
                }
                if (row - 1 >= 1 && col - 2 >= 1){
                    position = new ChessPosition(row-1, col-2);
                    validMove(board, myPosition, moves, position);
                }
                if (row - 2 >= 1 && col + 1 <= 8){
                    position = new ChessPosition(row-2, col+1);
                    validMove(board, myPosition, moves, position);
                }
                if (row - 2 >= 1 && col - 1 >= 1){
                    position = new ChessPosition(row-2, col-1);
                    validMove(board, myPosition, moves, position);
                }
                break;
            case ROOK:
                for (int i=1; i + row <= 8; i++) { //Up
                    position = new ChessPosition(row + i, col);
                    if (validMove(board, myPosition, moves, position)) break;
                }
                for (int i=1; row - i >= 1; i++) { //Down
                    position = new ChessPosition(row - i, col);
                    if (validMove(board, myPosition, moves, position)) break;
                }
                for (int i=1; i + col <= 8; i++) { //Right
                    position = new ChessPosition(row, col + i);
                    if (validMove(board, myPosition, moves, position)) break;
                }
                for (int i=1; col - i >= 1; i++) { //Left
                    position = new ChessPosition(row, col - i);
                    if (validMove(board, myPosition, moves, position)) break;
                }
                break;
            case PAWN:
                if (getTeamColor() == ChessGame.TeamColor.WHITE){  //Capital letter
                    if (row < 7){   //Normal move
                        position = new ChessPosition(row + 1, col);
                        if(board.getPiece(position) == null){  // Empty spot so we can actually go there
                            var new_move = new ChessMove(myPosition, position, null);
                            moves.add(new_move);
                            if (row == 2){   //Hasn't moved yet
                                position = new ChessPosition(row + 2, col);
                                if(board.getPiece(position) == null){  // Empty spot so we can actually go there
                                    new_move = new ChessMove(myPosition, position, null);
                                    moves.add(new_move);
                                }
                            }
                        }
                        // Capture enemies
                        if (col < 7){
                            position = new ChessPosition(row + 1, col + 1);
                            if(board.getPiece(position) != null && board.getPiece(position).getTeamColor() != this.getTeamColor()) {
                                // Enemy is there
                                var new_move = new ChessMove(myPosition, position, null);
                                moves.add(new_move);
                            }
                            // Passant
                            var other_position = new ChessPosition(row, col + 1);
                            var other_piece = board.getPiece(other_position);
                            if(other_piece != null && other_piece.getPassantPossible()){
                                var new_move = new ChessMove(myPosition, position, null);;
                                moves.add(new_move);
                            }
                        }
                        if (col > 1){
                            position = new ChessPosition(row + 1, col - 1);
                            if(board.getPiece(position) != null && board.getPiece(position).getTeamColor() != this.getTeamColor()){
                                // Enemy is there
                                var new_move = new ChessMove(myPosition, position, null);
                                moves.add(new_move);
                            }
                            // Passant
                            var other_position = new ChessPosition(row, col - 1);
                            var other_piece = board.getPiece(other_position);
                            if(other_piece != null && other_piece.getPassantPossible()){
                                var new_move = new ChessMove(myPosition, position, null);;
                                moves.add(new_move);
                            }
                        }
                    }
                    if (row == 7){  // Promotion time!
                        position = new ChessPosition(row + 1, col);
                        if(board.getPiece(position) == null){  // Empty spot so we can actually go there
                            var new_move = new ChessMove(myPosition, position, QUEEN);
                            moves.add(new_move);
                            new_move = new ChessMove(myPosition, position, KNIGHT);
                            moves.add(new_move);
                            new_move = new ChessMove(myPosition, position, BISHOP);
                            moves.add(new_move);
                            new_move = new ChessMove(myPosition, position, ROOK);
                            moves.add(new_move);
                        }
                        if (col < 7){
                            position = new ChessPosition(row + 1, col + 1);
                            if(board.getPiece(position) != null && board.getPiece(position).getTeamColor() != this.getTeamColor()) {
                                // Enemy is there
                                var new_move = new ChessMove(myPosition, position, QUEEN);
                                moves.add(new_move);
                                new_move = new ChessMove(myPosition, position, KNIGHT);
                                moves.add(new_move);
                                new_move = new ChessMove(myPosition, position, BISHOP);
                                moves.add(new_move);
                                new_move = new ChessMove(myPosition, position, ROOK);
                                moves.add(new_move);
                            }
                        }
                        if (col > 1){
                            position = new ChessPosition(row + 1, col - 1);
                            if(board.getPiece(position) != null && board.getPiece(position).getTeamColor() != this.getTeamColor()){
                                // Enemy is there
                                var new_move = new ChessMove(myPosition, position, QUEEN);
                                moves.add(new_move);
                                new_move = new ChessMove(myPosition, position, KNIGHT);
                                moves.add(new_move);
                                new_move = new ChessMove(myPosition, position, BISHOP);
                                moves.add(new_move);
                                new_move = new ChessMove(myPosition, position, ROOK);
                                moves.add(new_move);
                            }
                        }
                    }
                }
                else if (getTeamColor() == ChessGame.TeamColor.BLACK){  //LowerCase letter
                    if (row > 2){   //Normal move
                        position = new ChessPosition(row - 1, col);
                        if(board.getPiece(position) == null){  // Empty spot so we can actually go there
                            var new_move = new ChessMove(myPosition, position, null);
                            moves.add(new_move);
                            if (row == 7){   //Hasn't moved yet
                                position = new ChessPosition(row - 2, col);
                                if(board.getPiece(position) == null){  // Empty spot so we can actually go there
                                    new_move = new ChessMove(myPosition, position, null);
                                    moves.add(new_move);
                                }
                            }
                        }
                        // Capture enemies
                        if (col < 7){
                            position = new ChessPosition(row - 1, col + 1);
                            if(board.getPiece(position) != null && board.getPiece(position).getTeamColor() != this.getTeamColor()) {
                                // Enemy is there
                                var new_move = new ChessMove(myPosition, position, null);
                                moves.add(new_move);
                            }
                            // Passant
                            var other_position = new ChessPosition(row, col + 1);
                            var other_piece = board.getPiece(other_position);
                            if(other_piece != null && other_piece.getPassantPossible()){
                                var new_move = new ChessMove(myPosition, position, null);;
                                moves.add(new_move);
                            }
                        }
                        if (col > 1){
                            position = new ChessPosition(row - 1, col - 1);
                            if(board.getPiece(position) != null && board.getPiece(position).getTeamColor() != this.getTeamColor()){
                                // Enemy is there
                                var new_move = new ChessMove(myPosition, position, null);
                                moves.add(new_move);
                            }
                            // Passant
                            var other_position = new ChessPosition(row, col - 1);
                            var other_piece = board.getPiece(other_position);
                            if(other_piece != null && other_piece.getPassantPossible()){
                                var new_move = new ChessMove(myPosition, position, null);;
                                moves.add(new_move);
                            }
                        }
                    }
                    if (row == 2){  // Promotion time!
                        position = new ChessPosition(row - 1, col);
                        if(board.getPiece(position) == null){  // Empty spot so we can actually go there
                            var new_move = new ChessMove(myPosition, position, QUEEN);
                            moves.add(new_move);
                            new_move = new ChessMove(myPosition, position, KNIGHT);
                            moves.add(new_move);
                            new_move = new ChessMove(myPosition, position, BISHOP);
                            moves.add(new_move);
                            new_move = new ChessMove(myPosition, position, ROOK);
                            moves.add(new_move);
                        }
                        if (col < 7){
                            position = new ChessPosition(row - 1, col + 1);
                            if(board.getPiece(position) != null && board.getPiece(position).getTeamColor() != this.getTeamColor()) {
                                // Enemy is there
                                var new_move = new ChessMove(myPosition, position, QUEEN);
                                moves.add(new_move);
                                new_move = new ChessMove(myPosition, position, KNIGHT);
                                moves.add(new_move);
                                new_move = new ChessMove(myPosition, position, BISHOP);
                                moves.add(new_move);
                                new_move = new ChessMove(myPosition, position, ROOK);
                                moves.add(new_move);
                            }
                        }
                        if (col > 1){
                            position = new ChessPosition(row - 1, col - 1);
                            if(board.getPiece(position) != null && board.getPiece(position).getTeamColor() != this.getTeamColor()){
                                // Enemy is there
                                var new_move = new ChessMove(myPosition, position, QUEEN);
                                moves.add(new_move);
                                new_move = new ChessMove(myPosition, position, KNIGHT);
                                moves.add(new_move);
                                new_move = new ChessMove(myPosition, position, BISHOP);
                                moves.add(new_move);
                                new_move = new ChessMove(myPosition, position, ROOK);
                                moves.add(new_move);
                            }
                        }
                    }
                }
                break;
        }

        return moves;
    }

    /**
     * Looks at a spot to see if it is a valid move
     * If the spot is empty, add move to the Hashset and returns false
     * If the spot has a piece from the other team, add move and return true
     * If the spot has a piece from the same team, return true
     *
     * @return true is the piece hit the end in a certain direction
     */
    private boolean validMove(ChessBoard board, ChessPosition myPosition, HashSet<ChessMove> moves, ChessPosition position) {
        if(board.getPiece(position) == null){  // Empty spot so we can keep going
            var new_move = new ChessMove(myPosition, position, null);
            moves.add(new_move);
        }
        else if(board.getPiece(position).getTeamColor() == this.getTeamColor()) {
            return true;
        }
        else if(board.getPiece(position).getTeamColor() != this.getTeamColor()) {
            var new_move = new ChessMove(myPosition, position, null);
            moves.add(new_move);
            return true;
        }
        return false;
    }

    @Override
    public String toString(){
        if (this.getTeamColor() == ChessGame.TeamColor.WHITE){  //Capital Letter
            if (this.getPieceType() == BISHOP){
                return "B";
            }
            else if (this.getPieceType() == KING){
                return "K";
            }
            else if (this.getPieceType() == KNIGHT){
                return "N";
            }
            else if (this.getPieceType() == PAWN){
                return "P";
            }
            else if (this.getPieceType() == QUEEN){
                return "Q";
            }
            else if (this.getPieceType() == ROOK){
                return "R";
            }
        }
        else if (this.getTeamColor() == ChessGame.TeamColor.BLACK) { //Lowercase letter
            if (this.getPieceType() == BISHOP){
                return "b";
            }
            else if (this.getPieceType() == KING){
                return "k";
            }
            else if (this.getPieceType() == KNIGHT){
                return "n";
            }
            else if (this.getPieceType() == PAWN){
                return "p";
            }
            else if (this.getPieceType() == QUEEN){
                return "q";
            }
            else if (this.getPieceType() == ROOK){
                return "r";
            }
        }
        return " ";
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
