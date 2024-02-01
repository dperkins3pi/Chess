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

    public ChessGame() {
        setTeamTurn(TeamColor.WHITE); // White starts
        this.board.resetBoard();
        setBoard(this.board);  // Set board to start
    }
    public ChessGame(ChessGame newGame){
        this.setTeamTurn(newGame.getTeamTurn());
        this.board = new ChessBoard(newGame.board);
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
        var possible_moves = piece.pieceMoves(this.board, startPosition);
        var valid_moves = new HashSet<ChessMove>();
        for (var move: possible_moves){
            var pieceAttacked = board.getPiece(move.getEndPosition());   // Store piece that may be lost
            board.addPiece(move.getStartPosition(), null);
            if (move.getPromotionPiece() == null){   // Need to change it back if it gets promoted
                board.addPiece(move.getEndPosition(), piece);
            }
            if (move.getPromotionPiece() != null){
                piece = new ChessPiece(color, move.getPromotionPiece());
                board.addPiece(move.getEndPosition(), piece);
            }
            boolean is_in_check = isInCheck(color);
            // Undo moves
            board.addPiece(move.getEndPosition(), pieceAttacked);
            if (move.getPromotionPiece() == null){
                board.addPiece(move.getStartPosition(), piece);
            }
            if (move.getPromotionPiece() != null){
                piece = new ChessPiece(color, ChessPiece.PieceType.PAWN);
                board.addPiece(move.getStartPosition(), piece);
            }
            if (!is_in_check){
                System.out.println("Adding" + move.toString());
                valid_moves.add(move);
            }
        }
        return valid_moves;
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
            var error_message = "There is no piece there";
            throw new InvalidMoveException(error_message);
        }
        if (piece.getTeamColor() != this.getTeamTurn()) {  //If the wrong team tries to move
            var error_message = "It is not " + piece.getTeamColor() + "'s turn to play";
            throw new InvalidMoveException(error_message);
        }
        var valid_moves = piece.pieceMoves(this.board, move.getStartPosition());
        if(valid_moves.contains(move)){
            if (move.getPromotionPiece() != null){  // If it will be promoted, change the piece
                piece = new ChessPiece(team, move.getPromotionPiece());
            }
            board.addPiece(move.getEndPosition(), piece);
            board.addPiece(move.getStartPosition(), null);  //Remove the piece from where it started
            if(this.isInCheck(team)){  // The move is invalid if it makes it in check
                board.addPiece(move.getEndPosition(), null);
                board.addPiece(move.getStartPosition(), piece);  //Remove the piece from where it started
                var error_message = "The move is invalid because it puts you in check";
                throw new InvalidMoveException(error_message);
            }
            // Change turn to next team
            if(this.getTeamTurn() == TeamColor.BLACK){
                setTeamTurn(TeamColor.WHITE);
            }
            else{
                setTeamTurn(TeamColor.BLACK);
            }
        }
        else{
            var error_message = "The move " + move.toString() + " for the " + piece.getPieceType() + " is not possible";
            throw new InvalidMoveException(error_message);
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
        var king_position = new ChessPosition(0, 0);
        for(int i=1; i <= 8; i++){
            for(int j=1; j <= 8; j++){
                var position = new ChessPosition(i, j);
                var piece = this.board.getPiece(position);
                if(piece != null && piece.getTeamColor() == teamColor && piece.getPieceType() == ChessPiece.PieceType.KING){
                    king_position = position;
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
                    var king_move = new ChessMove(position, king_position, null);
                    var king_move2 = new ChessMove(position, king_position, ChessPiece.PieceType.QUEEN);  //Rare case that pawn promotion results in check
                    if (piece.pieceMoves(board, position).contains(king_move) || piece.pieceMoves(board, position).contains(king_move2)) {
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
                if (!moves.isEmpty() && this.board.getPiece(position).getTeamColor() == teamColor){  // If the piece is on the team that can move
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
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                var position = new ChessPosition(i, j);
                var moves = validMoves(position);
                if (moves.isEmpty() && this.board.getPiece(position).getTeamColor() == teamColor) {
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
