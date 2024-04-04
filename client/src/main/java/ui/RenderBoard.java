package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static java.lang.Math.abs;
import static ui.EscapeSequences.*;

public class RenderBoard {

  private static final int BOARD_ROWS = 8;
  private static final int BOARD_COLS = 8;
  private ChessGame game = null;
  private static final String EMPTY = "   ";

  public RenderBoard(ChessGame game) {
    this.game = game;
  }

  public /*static*/ void main(/*String[] agrs*/) {
    var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    ChessGame game = new ChessGame();
    out.print(ERASE_SCREEN);

    drawChessBoard(out, game);

    out.print(SET_BG_COLOR_BLACK);
    out.print(SET_TEXT_COLOR_WHITE);
  }

  private static void drawChessBoard(PrintStream out, ChessGame game) {
    ChessBoard board = game.getBoard();
    board.resetBoard(); // temp for testing-------------------------------------------------------------------
//    board.addPiece(new ChessPosition(3,6), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN)); // for testing
//    board.addPiece(new ChessPosition(6,3), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
    var boardPieceArray = board.getBoard();
    var teamTurn = game.getTeamTurn();
//    teamTurn = ChessGame.TeamColor.BLACK; // for testing------------------------------------------------------



    switch (teamTurn) {

      case WHITE:

        boolean isWhite = true;
        printWhiteHeaderFooter(out);
        for (int rows = 0; rows < BOARD_ROWS; rows++) {
          // print grey col
          setBoarder(out);
          var rowNum = Integer.toString(abs(rows - 8));
          out.print(" " + rowNum +" ");

          for (int cols = 0; cols < BOARD_COLS; cols++) {

            if (isWhite) {
              setWhite(out);
            } else { setBlack(out); }

            // sets piece color/type and prints
            var row = abs(rows - 7);
            if (board.getPiece(new ChessPosition(row+1, cols+1)) != null) {
              var chessPieceType = boardPieceArray[row][cols].getPieceType();
              var pieceColor = boardPieceArray[row][cols].getTeamColor();
              var pieceToPrint = "X";

              if (pieceColor == ChessGame.TeamColor.WHITE) {
                setWhitePlayer(out);
              } else {setBlackPlayer(out);}

              pieceToPrint=switch (chessPieceType) {
                case KING -> "K";
                case QUEEN -> "Q";
                case BISHOP -> "B";
                case ROOK -> "R";
                case KNIGHT -> "N";
                case PAWN -> "P";
              };
              out.print(" " + pieceToPrint + " ");

            } else { out.print(EMPTY); }
            isWhite = !isWhite;
          }
          isWhite = !isWhite;
          // print grey col
          setBoarder(out);
          out.print(" " + rowNum +" ");

          out.print(RESET_TEXT_COLOR);
          out.print(RESET_BG_COLOR);
          out.println();
        }
        printWhiteHeaderFooter(out);
        break;  // not needed while testing
//        out.println(); // for testing --------------------------------------------------

      case BLACK:

        isWhite = true;
        printBlackHeaderFooter(out);
        for (int rows = 0; rows < BOARD_ROWS; rows++) {
          // print grey col
          setBoarder(out);
          var rowNum = Integer.toString(rows+1);
          out.print(" " + rowNum +" ");

          for (int cols = 0; cols < BOARD_COLS; cols++) {

            if (isWhite) {
              setWhite(out);
            } else { setBlack(out); }

            // sets piece color/type and prints
            var col = abs(cols - 7);
            if (board.getPiece(new ChessPosition(rows+1, col+1)) != null) {
              var chessPieceType = boardPieceArray[rows][col].getPieceType();
              var pieceColor = boardPieceArray[rows][col].getTeamColor();
              var pieceToPrint = "X";

              if (pieceColor == ChessGame.TeamColor.WHITE) {
                setWhitePlayer(out);
              } else {setBlackPlayer(out);}

              pieceToPrint=switch (chessPieceType) {
                case KING -> "K";
                case QUEEN -> "Q";
                case BISHOP -> "B";
                case ROOK -> "R";
                case KNIGHT -> "N";
                case PAWN -> "P";
              };
              out.print(" " + pieceToPrint + " ");

            } else { out.print(EMPTY); }
            isWhite = !isWhite;
          }
          isWhite = !isWhite;
          // print grey col
          setBoarder(out);
          out.print(" " + rowNum +" ");

          out.print(RESET_TEXT_COLOR);
          out.print(RESET_BG_COLOR);
          out.println();
        }
        printBlackHeaderFooter(out);
        break;
    }
  }

  private static void printWhiteHeaderFooter(PrintStream out) {
    setBoarder(out);
    out.print("   ");
    out.print(" a ");
    out.print(" b ");
    out.print(" c ");
    out.print(" d ");
    out.print(" e ");
    out.print(" f ");
    out.print(" g ");
    out.print(" h ");
    out.print("   ");
    out.print(RESET_TEXT_COLOR);
    out.print(RESET_BG_COLOR);
    out.println();
  }
  private static void printBlackHeaderFooter(PrintStream out) {
    setBoarder(out);
    out.print("   ");
    out.print(" h ");
    out.print(" g ");
    out.print(" f ");
    out.print(" e ");
    out.print(" d ");
    out.print(" c ");
    out.print(" b ");
    out.print(" a ");
    out.print("   ");
    out.print(RESET_TEXT_COLOR);
    out.print(RESET_BG_COLOR);
    out.println();
  }
  private static void setWhite(PrintStream out) {
    out.print(SET_BG_COLOR_WHITE);
    out.print(SET_TEXT_COLOR_WHITE);
  }
  private static void setBlack(PrintStream out) {
    out.print(SET_BG_COLOR_BLACK);
    out.print(SET_TEXT_COLOR_BLACK);
  }
  private static void setBoarder(PrintStream out) {
    out.print(SET_BG_COLOR_LIGHT_GREY);
    out.print(SET_TEXT_COLOR_BLACK);
  }
  private static void setBlackPlayer(PrintStream out) {
    out.print(SET_TEXT_COLOR_BLUE);
  }
  private static void setWhitePlayer(PrintStream out) {
    out.print(SET_TEXT_COLOR_RED);
  }
}
