package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor team;
    private ChessBoard board = new ChessBoard();

    private ChessPosition PassantPosition;   // Stores the position of pawns that moved 2 (for just one turn)
    // This allows the code to change Passant to false after one turn is made

    public ChessGame() {
        setTeamTurn(TeamColor.WHITE); // White starts
        this.board.resetBoard();
        setBoard(this.board);  // Set board to start
        this.PassantPosition = null;
    }
    public ChessGame(ChessGame newGame){
        this.setTeamTurn(newGame.getTeamTurn());
        this.board = new ChessBoard(newGame.board);
        this.PassantPosition = null;
    }

    public ChessPosition getPassantPosition() {
        return this.PassantPosition;
    }

    public void setPassantPosition(ChessPosition passantPosition) {
        this.PassantPosition = passantPosition;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.team;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.team = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        var piece = this.board.getPiece(startPosition);
        if (piece == null) return new HashSet<ChessMove>();  // If null return an empty hash set
        var color = piece.getTeamColor();
        var possibleMoves = piece.pieceMoves(this.board, startPosition);
        var validMoves = new HashSet<ChessMove>();
        for (var move: possibleMoves){
            if (piece.getPieceType() == ChessPiece.PieceType.KING && piece.notAlreadyMoved()){
                if (move.getEndPosition().getColumn() - move.getStartPosition().getColumn() == 2){  // If the move passed in is casteling
                    board.addPiece(move.getStartPosition(), null);
                    var newPosition = new ChessPosition(move.getStartPosition().getRow(), move.getStartPosition().getColumn() + 1);
                    board.addPiece(newPosition, piece);
                    if(isInCheck(color)) {
                        ;  // Path is not safe
                        board.addPiece(move.getStartPosition(), piece); // Undo the moves
                        board.addPiece(newPosition, null);
                        break;
                    }
                    // move one more time
                    board.addPiece(newPosition, null);
                    board.addPiece(move.getEndPosition(), piece);
                    if(isInCheck(color)) {
                        ;  // Path is not safe
                        board.addPiece(move.getStartPosition(), piece); // Undo the moves
                        board.addPiece(move.getEndPosition(), null);
                        break;
                    }
                    else{
                        board.addPiece(move.getStartPosition(), piece); // Undo the moves
                        board.addPiece(move.getEndPosition(), null);
                        validMoves.add(move);
                    }
                }
                else if (move.getEndPosition().getColumn() - move.getStartPosition().getColumn() == -2){  // If the move passed in is casteling
                    board.addPiece(move.getStartPosition(), null);
                    var new_position = new ChessPosition(move.getStartPosition().getRow(), move.getStartPosition().getColumn() - 1);
                    board.addPiece(new_position, piece);
                    if(isInCheck(color)) {  // Path is not safe
                        board.addPiece(move.getStartPosition(), piece); // Undo the moves
                        board.addPiece(new_position, null);
                        break;
                    }
                    // move on more time
                    board.addPiece(new_position, null);
                    board.addPiece(move.getEndPosition(), piece);
                    if(isInCheck(color)) {
                        ;  // Path is not safe
                        board.addPiece(move.getStartPosition(), piece); // Undo the moves
                        board.addPiece(move.getEndPosition(), null);
                        break;
                    }
                    else {
                        board.addPiece(move.getStartPosition(), piece); // Undo the moves
                        board.addPiece(move.getEndPosition(), null);
                        validMoves.add(move);
                    }
                }
            }
            var pieceAttacked = board.getPiece(move.getEndPosition());   // Store piece that may be lost
            board.addPiece(move.getStartPosition(), null);   // Remove initial piece
            if (move.getPromotionPiece() == null){   // Need to change it back if it gets promoted
                board.addPiece(move.getEndPosition(), piece);
            }
            if (move.getPromotionPiece() != null){     // If it was promoted
                piece = new ChessPiece(color, move.getPromotionPiece());
                board.addPiece(move.getEndPosition(), piece);
            }
            boolean isInCheck = isInCheck(color);   // If it is in check, we will not add the move
            // Undo moves
            board.addPiece(move.getEndPosition(), pieceAttacked);
            if (move.getPromotionPiece() == null){
                board.addPiece(move.getStartPosition(), piece);
            }
            if (move.getPromotionPiece() != null){
                piece = new ChessPiece(color, ChessPiece.PieceType.PAWN);
                board.addPiece(move.getStartPosition(), piece);
            }
            if (!isInCheck){
                validMoves.add(move);   // If it didn't go in check, the move is valid
            }
        }
        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        var piece = this.board.getPiece(move.getStartPosition());
        if (piece == null) {
            var errorMessage = "There is no piece there";
            throw new InvalidMoveException(errorMessage);
        }
        if (piece.getTeamColor() != this.getTeamTurn()) {  //If the wrong team tries to move
            var errorMessage = "It is not " + piece.getTeamColor() + "'s turn to play";
            throw new InvalidMoveException(errorMessage);
        }
        var validMoves = this.validMoves(move.getStartPosition());
        if(validMoves.contains(move)){
            if (move.getPromotionPiece() != null){  // If it will be promoted, change the piece
                piece = new ChessPiece(team, move.getPromotionPiece());
            }
            // If the move was a Passant
            if(piece.getPieceType() == ChessPiece.PieceType.PAWN && board.getPiece(move.getEndPosition()) == null &&
                    move.getEndPosition().getColumn() - move.getStartPosition().getColumn() != 0){
                if(move.getEndPosition().getRow()==3){
                    var newPosition = new ChessPosition(4, move.getEndPosition().getColumn());
                    board.addPiece(newPosition, null);  // Attack the passaned piece
                }
                if(move.getEndPosition().getRow()==6){
                    var newPosition = new ChessPosition(5, move.getEndPosition().getColumn());
                    board.addPiece(newPosition, null);  // Attack the passaned piece
                }
            }
            board.addPiece(move.getEndPosition(), piece);
            board.addPiece(move.getStartPosition(), null);  //Remove the piece from where it started
            // If the move was a castling
            if(piece.getPieceType() == ChessPiece.PieceType.KING && move.getEndPosition().getColumn() - move.getStartPosition().getColumn() == 2){
                var rookPosition = new ChessPosition(move.getStartPosition().getRow(), 8);
                var rook = this.board.getPiece(rookPosition);
                board.addPiece(rookPosition, null); // Remove old rook
                board.addPiece(new ChessPosition(move.getStartPosition().getRow(), 6), rook); // Move old rook
            }
            else if(piece.getPieceType() == ChessPiece.PieceType.KING && move.getEndPosition().getColumn() - move.getStartPosition().getColumn() == -2){
                var rookPosition = new ChessPosition(move.getStartPosition().getRow(), 1);
                var rook = this.board.getPiece(rookPosition);
                board.addPiece(rookPosition, null); // Remove old rook
                board.addPiece(new ChessPosition(move.getStartPosition().getRow(), 4), rook); // Move old rook
            }
            if (getPassantPosition() != null && this.board.getPiece(getPassantPosition()) != null){  // If the previous board had something on passant
                board.getPiece(getPassantPosition()).setPassant(false);  // Passant is no longer possible
            }
            // Change turn to next team
            if(this.getTeamTurn() == TeamColor.BLACK){
                setTeamTurn(TeamColor.WHITE);
            }
            else{
                setTeamTurn(TeamColor.BLACK);
            }
            piece.setAlreadyMoved();  // Mark that the piece has moved
            // Mark that Passant is possible
            if(piece.getPieceType() == ChessPiece.PieceType.PAWN &&
                    move.getEndPosition().getRow() - move.getStartPosition().getRow() == 2 ||
                    move.getEndPosition().getRow() - move.getStartPosition().getRow() == -2){
                piece.setPassant(true);
                setPassantPosition(move.getEndPosition());  // Store what what moved 2
                System.out.println(PassantPosition);
            }
        }
        else{
            var errorMessage = "The move " + move.toString() + " for the " + piece.getPieceType() + " is not possible";
            throw new InvalidMoveException(errorMessage);
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        // Inefficient method; could optimize it by looking around the king
        // Find the king
        var kingPosition = new ChessPosition(0, 0);
        for(int i=1; i <= 8; i++){
            for(int j=1; j <= 8; j++){
                var position = new ChessPosition(i, j);
                var piece = this.board.getPiece(position);
                if(piece != null && piece.getTeamColor() == teamColor && piece.getPieceType() == ChessPiece.PieceType.KING){
                    kingPosition = position;
                    break;
                }
            }
        }
        // See if the king is in any of the valid moves from the opponents' pieces
        for(int i=1; i <= 8; i++){
            for(int j=1; j <= 8; j++){
                var position = new ChessPosition(i, j);
                var piece = this.board.getPiece(position);
                if(piece != null && piece.getTeamColor() != teamColor) {
                    var kingMove = new ChessMove(position, kingPosition, null);
                    var kingMove2 = new ChessMove(position, kingPosition, ChessPiece.PieceType.QUEEN);  //Rare case that pawn promotion results in check
                    if (piece.pieceMoves(board, position).contains(kingMove) || piece.pieceMoves(board, position).contains(kingMove2)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)){
            return false;
        }
        for (int i = 1; i <= 8; i++){   // Go through each piece on the board
            for (int j = 1; j <= 8; j++){
                var position = new ChessPosition(i, j);
                var moves = this.validMoves(position);
                if (this.board.getPiece(position) != null && !moves.isEmpty() && this.board.getPiece(position).getTeamColor() == teamColor){  // If the piece is on the team that can move
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if(this.isInCheck(teamColor)) {  // If already in check it can't be in stalemate
            return false;
        }
        for (int i = 1; i <= 8; i++) {   // Similar to code for checkmate
            for (int j = 1; j <= 8; j++) {
                var position = new ChessPosition(i, j);
                var moves = validMoves(position);
                if (this.board.getPiece(position) != null && moves.isEmpty() && this.board.getPiece(position).getTeamColor() == teamColor) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame = (ChessGame) o;
        return team == chessGame.team && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(team, board);
    }
}
