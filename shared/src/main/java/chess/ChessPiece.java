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
        Collection<ChessMove> moveList = new ArrayList<>();

        // add switch statement structure
        switch (type) {
            case KING:
                moveList = helperKing(board, myPosition);
                break;
            case PAWN:
                moveList = helperPawn(board, myPosition);
                break;
            case ROOK:
                moveList = helperRook(board, myPosition);
                break;
            case QUEEN:
                moveList = helperQueen(board, myPosition);
                break;
            case BISHOP:
                moveList = helperBishop(board, myPosition);
                break;
            case KNIGHT:
                moveList = helperKnight(board, myPosition);
                break;
            default:
        }

        return moveList;
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
        var row = myPosition.getRow();
        var col = myPosition.getColumn();
        ArrayList<ChessMove> myMoves = new ArrayList<>();
        // loop up/right
        while (row + 1 < 8 && col + 1 < 8) { // checking for edge
            var endPosition = new ChessPosition(row+1,col+1);
            var newPiece = board.getPiece(endPosition);

            // checks
            myMoves.add(new ChessMove(myPosition, endPosition, null));

        }

        throw new RuntimeException("Not implemented");
    }

    public Collection<ChessMove> helperQueen(ChessBoard board, ChessPosition myPosition){
        throw new RuntimeException("Not implemented");
    }
}
