package ui;

import web.websocket.NotificationHandler;
import webSocketMessages.serverMessages.ServerMessage;

import java.util.Scanner;
import static ui.EscapeSequences.*;


public class Repl implements NotificationHandler {

  private final ChessMatchClient client;

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

  public void notify (ServerMessage message) {
//    System.out.println(SET_TEXT_COLOR_RED + message.message());
    // filter message by type
    //
    printPrompt();
  }

  private void printPrompt()  {
    System.out.print("\n" +  ">>> ");
  }
}
