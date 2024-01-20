package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType type;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that=(ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
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
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        // get the piece's type
        var piece = board.getPiece(myPosition);
        var type = piece.getPieceType();
        Collection<ChessMove> validMoves = new ArrayList<>();

        // add switch statement structure
        switch (type) {
            case KING:
                validMoves = helperKing(board, myPosition);
                break;
            case PAWN:
                validMoves = helperPawn(board, myPosition);
                break;
            case ROOK:
                validMoves = helperRook(board, myPosition);
                break;
            case QUEEN:
                validMoves = helperQueen(board, myPosition);
                break;
            case BISHOP:
                validMoves = helperBishop(board, myPosition);
                break;
            case KNIGHT:
                validMoves = helperKnight(board, myPosition);
                break;
            default:
        }

        return validMoves;
    }

    public Collection<ChessMove> helperKing(ChessBoard board, ChessPosition myPosition){
        throw new RuntimeException("Not implemented");
    }

    public Collection<ChessMove> helperPawn(ChessBoard board, ChessPosition myPosition){
        throw new RuntimeException("Not implemented");
    }

    public Collection<ChessMove> helperRook(ChessBoard board, ChessPosition myPosition){
        throw new RuntimeException("Not implemented");
    }

    public Collection<ChessMove> helperKnight(ChessBoard board, ChessPosition myPosition){
        throw new RuntimeException("Not implemented");
    }

    public Collection<ChessMove> helperBishop(ChessBoard board, ChessPosition myPosition){
        // Stops at edge, stops before allies, stops on top of enemy
        int r = myPosition.getRow() + 1;
        int c = myPosition.getColumn() + 1;
        ArrayList<ChessMove> validMoves = new ArrayList<>();


        // loop up/right
        int row = r;
        int col = c;
        while (row + 1 < 9 && col + 1 < 9) { // checking for edge
            // update variables for looping to function properly
            row++;
            col++;

            ChessPosition endPosition = new ChessPosition(row,col);
            ChessPiece newPiece = board.getPiece(endPosition);

            // movement checks
            if (newPiece == null) { // if the space is empty save position and continue loop
                validMoves.add(new ChessMove(myPosition, endPosition, null));
            }
            else if (newPiece.pieceColor != pieceColor) { // capture enemy break loop
                validMoves.add(new ChessMove(myPosition, endPosition, null));
                break;
            }
            else { // ally is blocking way, break loop
                break;
            }
        }

        // loop down/right
        row = r;
        col = c;
        while (row - 1 > 0 && col + 1 < 9) { // checking for edge
            // update variables for looping to function properly
            row--;
            col++;

            ChessPosition endPosition = new ChessPosition(row,col);
            ChessPiece newPiece = board.getPiece(endPosition);

            // movement checks
            if (newPiece == null) { // if the space is empty save position and continue loop
                validMoves.add(new ChessMove(myPosition, endPosition, null));
            }
            else if (newPiece.pieceColor != pieceColor) { // capture enemy break loop
                validMoves.add(new ChessMove(myPosition, endPosition, null));
                break;
            }
            else { // ally is blocking way, break loop
                break;
            }


        }

        // loop down/left
        row = r;
        col = c;
        while (row - 1 > 0 && col - 1 > 0) { // checking for edge
            // update variables for looping to function properly
            row--;
            col--;

            ChessPosition endPosition = new ChessPosition(row,col);
            ChessPiece newPiece = board.getPiece(endPosition);

            // movement checks
            if (newPiece == null) { // if the space is empty save position and continue loop
                validMoves.add(new ChessMove(myPosition, endPosition, null));
            }
            else if (newPiece.pieceColor != pieceColor) { // capture enemy break loop
                validMoves.add(new ChessMove(myPosition, endPosition, null));
                break;
            }
            else { // ally is blocking way, break loop
                break;
            }


        }

        // loop up/left
        row = r;
        col = c;
        while (row + 1 < 9 && col - 1 > 0) { // checking for edge
            // update variables for looping to function properly
            row++;
            col--;

            ChessPosition endPosition = new ChessPosition(row,col);
            ChessPiece newPiece = board.getPiece(endPosition);

            // movement checks
            if (newPiece == null) { // if the space is empty save position and continue loop
                validMoves.add(new ChessMove(myPosition, endPosition, null));
            }
            else if (newPiece.pieceColor != pieceColor) { // capture enemy break loop
                validMoves.add(new ChessMove(myPosition, endPosition, null));
                break;
            }
            else { // ally is blocking way, break loop
                break;
            }


        }

        return validMoves;
    }



    public Collection<ChessMove> helperQueen(ChessBoard board, ChessPosition myPosition){
        throw new RuntimeException("Not implemented");
    }
}
