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
            var testGame = new ChessGame(board,piece.getTeamColor());
            boolean isValid = true;
            try {
                testGame.makeMove(move);
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
        var piece = board.getPiece(move.getStartPosition());
        var endPosition = move.getEndPosition();
        var startPosition = move.getStartPosition();

        // instantiate exceptions
        InvalidMoveException inCheck = new InvalidMoveException("Wow really?");
        InvalidMoveException offBoard = new InvalidMoveException("You'll fall off the board if you go there");
        InvalidMoveException general = new InvalidMoveException("You'll fall off the board if you go there");

        // check to see if "move" is in the piece types available move list
        if (!piece.pieceMoves(board,startPosition).contains(move)){
            throw general;
        }

        // Check if endPosition is on the board
        var row = move.getEndPosition().getRow();
        var col = move.getEndPosition().getColumn();

        if (row > 9 || row < 0 || col > 9 || col < 0){
            throw offBoard;
        }

        // save board state
        var saveBoardState = board;

        // make move
        board.addPiece(endPosition, piece);
        board.addPiece(startPosition, null);

        // checks after move
        if (isInCheck(team)){
            // throw error
            board = saveBoardState;
            throw inCheck;
        }

        if (isInCheckmate(team)){
            // throw error
            board = saveBoardState;
            throw inCheck;
        }

        if (isInStalemate(team)){
            // throw error
            board = saveBoardState;
            throw new InvalidMoveException("Stalemate occured");
        }

        // if move not valid rollback move with tempo board

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

        // go through board and see if enemies can move to king's position


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
        // Returns true if the given team has no legal moves, and it is currently that team’s turn
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
        return board;
    }
}
