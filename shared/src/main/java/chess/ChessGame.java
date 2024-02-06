package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard board;

    private TeamColor team;

    public ChessGame(){
        this.board = new ChessBoard();
        this.team = TeamColor.WHITE;
    }
    public ChessGame(ChessBoard board, TeamColor team) {
        this.board = board;
        this.team = team;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return team;
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
        Collection<ChessMove> validMoves = new ArrayList<>();
        var piece = board.getPiece(startPosition);
        var pieceMoves = piece.pieceMoves(board, startPosition);

        // return null if a piece is not on starting position
        if (piece == null){
            return null;
        }
        // return all legal moves
        for (ChessMove move : pieceMoves){
            var testBoard = new ChessBoard(board.getBoard());
            var testGame = new ChessGame(testBoard,piece.getTeamColor());
            boolean isValid = true;
            try {
                testGame.validMoveHelper(move, piece);
//                validMoveHelper(move, piece);
            }  catch (InvalidMoveException e){
                isValid = false;
            } finally {
                if (isValid) {
                    validMoves.add(move);
                }
            }
        }

        return validMoves;
    }

    private void validMoveHelper(ChessMove move, ChessPiece piece) throws InvalidMoveException {
        var endPosition = move.getEndPosition();
        var startPosition = move.getStartPosition();

        InvalidMoveException inCheck = new InvalidMoveException("King in check");

        // save board state
        var savedBoardState = board;

        // make move
        board.addPiece(endPosition, piece);
        board.addPiece(startPosition, null);

        // if the move fails the check revert the board and throw error
        if (isInCheck(piece.getTeamColor())){
            board = savedBoardState;
            throw inCheck;
        }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        /*Receives a given move and executes it, provided it is a legal move.
         If the move is illegal, it throws an InvalidMoveException.
         A move is illegal if the chess piece cannot move there,
         if the move leaves the team’s king in danger,
         or if it’s not the corresponding team's turn*/
        // user input
        //makeMove
            //check if move is valid by calling valid move ##
                // checks for checkmate ##
                    //if valid actually make the move in make move
            // Team turn in make move

        var piece = board.getPiece(move.getStartPosition());
        var endPosition = move.getEndPosition();
        var startPosition = move.getStartPosition();

        // instantiate exceptions
        InvalidMoveException invalidMove = new InvalidMoveException("Not a valid move");

        // throw not valid move if the piece selected is null or cannot move to the chosen location
        if (piece == null || !validMoves(startPosition).isEmpty()){
            throw invalidMove;
        }

        // save board state
        var savedBoardState = board;

        // make move
        board.addPiece(endPosition, piece);
        board.addPiece(startPosition, null);


        // revert back if the move fails the check and throw error
        if (isInStalemate(team)){
            board = savedBoardState;
            throw new InvalidMoveException("Stalemate occurred");
        }

        // Change Player Turn
        team = (team == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        // Returns true if the specified team’s King could be captured by an opposing piece

        // get king's position
        var kingsPosition = board.findKingsPosition(teamColor);

        // go through board and see if enemies can move to king's position
        for (int i = 1; i < 9; i++){
            for (int j = 1; j < 9; j++){
                var position = new ChessPosition(i,j);
                var piece = board.getPiece(position);
                if(piece != null && piece.getTeamColor() != teamColor) {
                    if (piece.pieceMoves(board,position).contains(new ChessMove(position,kingsPosition,null))) {
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
        // Returns true if the given team has no way to protect their king from being captured

        // Can King move out of check

        // Can an ally piece move to remove king from Check

        // Can Any piece capture the piece threatening the king

//        return false;
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {

        for (int i = 1; i < 9; i++){
            for (int j = 1; j < 9; j++){
                var position = new ChessPosition(i,j);
                var piece = board.getPiece(position);
                if(piece != null && piece.getTeamColor() == teamColor) {
                    if (validMoves(position).isEmpty()) {
                        return true;
                    }
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
        return board;
    }
}
