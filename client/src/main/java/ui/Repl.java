package ui;

import chess.ChessGame;
import com.google.gson.Gson;
import web.websocket.NotificationHandler;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;

import java.util.Scanner;
import static ui.EscapeSequences.*;


public class Repl implements NotificationHandler {

  private final ChessMatchClient client;

  private final RenderBoard renderBoard = new RenderBoard();
  private static ChessGame game;

  public Repl(String serverUrl) {client = new ChessMatchClient(serverUrl, this);}

  public void run() {
    System.out.println("Welcome to Chess.");
    System.out.print(client.help());

    Scanner scanner = new Scanner(System.in);
    var result = "";
    while (!result.equals("quit")) {
      printPrompt();
      String line = scanner.nextLine();

      try {
        result = client.eval(line);
        System.out.print(/*SET_TEXT_COLOR_BLUE +*/ result);
      } catch (Throwable e) {
        System.out.print(e.getMessage());
      }
    }
    System.out.println();
  }

  public void notify(ServerMessage message) {
    // filter messages here to notify
    switch (message.getServerMessageType()) {
      case LOAD_GAME -> {
        // cast to correct class
        var loadGame = (LoadGameMessage) message;
        game = loadGame.getGame();
        // render from client
        System.out.print("\n");
        renderBoard.drawChessBoard(game);
      }
      case ERROR -> {
        // cast to correct class
        var errorMessage = (ErrorMessage) message;
        System.out.print(errorMessage.getErrorMessage());
      }
      case NOTIFICATION -> {
        // cast to correct class
        var notificationMessage = (NotificationMessage) message;
        System.out.print(notificationMessage.getMessage());
      }
    }

    printPrompt();
  }

  public static ChessGame getGame() { return game; }
  private void printPrompt()  {
    System.out.print("\n" +  ">>> ");
  }
}
