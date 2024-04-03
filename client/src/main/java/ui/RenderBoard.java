package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class RenderBoard {

  public static void main(String[] args) {
    var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

    out.print(ERASE_SCREEN);

    drawChessBoard(out);

    out.print(SET_BG_COLOR_BLACK);
    out.print(SET_TEXT_COLOR_WHITE);
  }

  private static void drawChessBoard(PrintStream out) {

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
    out.print(SET_BG_COLOR_DARK_GREY);
    out.print(SET_TEXT_COLOR_BLACK);
  }

  private static void printWhitePlayer(PrintStream out) {
    out.print(SET_TEXT_COLOR_BLUE);
  }

  private static void printBlackPlayer(PrintStream out) {
    out.print(SET_TEXT_COLOR_RED);
  }
}
