package ui;

import chess.ChessBoard;
import chess.ChessGame;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class RenderBoard {

  private static final int BOARD_ROWS = 8;
  private static final int BOARD_COLS = 10;
  private static final int LINE_WIDTH_IN_CHARS = 1;



  private final static String EMPTY = "   ";

  public static void main(String[] args) {
    var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

//    if (args.length != 2) {
//      return;
//    }

    ChessGame game = new ChessGame();
//    ChessBoard game = args[1].getBoard();


    out.print(ERASE_SCREEN);

    drawChessBoard(out, game);

    out.print(SET_BG_COLOR_BLACK);
    out.print(SET_TEXT_COLOR_WHITE);
  }

  private static void drawChessBoard(PrintStream out, ChessGame game) {
    ChessBoard board = game.getBoard();
    board.resetBoard(); // temp for testing
    var teamTurn = game.getTeamTurn();

    switch (teamTurn) {
      case WHITE:

        printWhiteHeaderFooter(out);
        for (int cols = 0; cols < BOARD_COLS; cols++) {

          for (int rows = 0; rows < BOARD_ROWS; rows++) {
            // print grey col

            // print board row out
              // check for piece at location col row
                // assign text color based on piece color

            // print grey col

          }
          out.print(RESET_TEXT_COLOR);
          out.print(RESET_BG_COLOR);
          out.println();
        }
        printWhiteHeaderFooter(out);


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

  private static void printWhitePlayer(PrintStream out) {
    out.print(SET_TEXT_COLOR_BLUE);
  }

  private static void printBlackPlayer(PrintStream out) {
    out.print(SET_TEXT_COLOR_RED);
  }
}
