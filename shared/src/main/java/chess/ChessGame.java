package chess;

import java.util.Collection;
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
        return piece.pieceMoves(this.board, startPosition);
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        var piece = this.board.getPiece(move.getStartPosition());
        if (piece.getTeamColor() != this.getTeamTurn()) {  //If the wrong team tries to move
            var error_message = "It is not " + piece.getTeamColor() + "'s turn to play";
            throw new InvalidMoveException(error_message);
        }
        var valid_moves = validMoves(move.getStartPosition());
        if(valid_moves.contains(move)){
            board.addPiece(move.getEndPosition(), piece);
            board.addPiece(move.getStartPosition(), null);  //Remove the piece from where it started
            if(this.isInCheck(team)){
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
                    if (validMoves(position).contains(king_move) || validMoves(position).contains(king_move2)) {
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
//        if (!isInCheck(teamColor)){
//            return false;
//        }
//        var possible_board = new ChessBoard(board);  // Get copy of board to avoid changing it
//        for (int i = 1; i <= 8; i++){   // Go through each piece on the board
//            for (int j = 1; j <= 8; j++){
//                var position = new ChessPosition(i, j);
//                var piece = this.board.getPiece(position);
//                if (piece != null && piece.getTeamColor() == teamColor){  // If the piece is on the team that can move
//                    var moves = piece.pieceMoves(board, position);   // Get possible moves
//                    for (var move: moves){
//                        try{
//                            makeMove(move);
//                        } catch (InvalidMoveException e) {
//                            throw new RuntimeException(e);
//                        }
//                    }
//                }
//            }
//        }
//        return true;
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
        throw new RuntimeException("Not implemented");
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
