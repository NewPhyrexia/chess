package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static java.lang.Math.abs;
import static ui.EscapeSequences.*;

public class RenderBoard {

  private static final int BOARD_ROWS = 8;
  private static final int BOARD_COLS = 8;
  private static final int LINE_WIDTH_IN_CHARS = 1;
  private ChessGame game = null;
  private static final String EMPTY = "   ";

  public RenderBoard(ChessGame game) {
    this.game = game;
  }

  public void main() {
    var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
//    ChessGame game = new ChessGame();
    out.print(ERASE_SCREEN);

    drawChessBoard(out, game);

    out.print(SET_BG_COLOR_BLACK);
    out.print(SET_TEXT_COLOR_WHITE);
  }

  private static void drawChessBoard(PrintStream out, ChessGame game) {
    ChessBoard board = game.getBoard();
//    board.resetBoard(); // temp for testing
    var boardPieceArray = board.getBoard();
    var teamTurn = game.getTeamTurn();

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
            if (board.getPiece(new ChessPosition(rows+1, cols+1)) != null) {
              var row = abs(rows - 7);
//              cols = abs(cols - 7);
              var chessPieceType = boardPieceArray[row][cols].getPieceType();
              var pieceColor = boardPieceArray[row][cols].getTeamColor();
              var pieceToPrint = "X";

              if (pieceColor == ChessGame.TeamColor.WHITE) {
                setWhitePlayer(out);
              } else {setBlackPlayer(out);}

              switch (chessPieceType) {
                case KING:
                  pieceToPrint = "K";
                  break;
                case QUEEN:
                  pieceToPrint = "Q";
                  break;
                case BISHOP:
                  pieceToPrint = "B";
                  break;
                case ROOK:
                  pieceToPrint = "R";
                  break;
                case KNIGHT:
                  pieceToPrint = "N";
                  break;
                case PAWN:
                  pieceToPrint = "P";
                  break;
              }
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
        break;

//      case BLACK:

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
