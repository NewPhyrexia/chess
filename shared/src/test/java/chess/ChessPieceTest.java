package chess;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChessPieceTest {
  @Test
  public void toStringTest(){
    ChessPiece myPiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
    String test = myPiece.toString();
    System.out.println(test);
  }

}