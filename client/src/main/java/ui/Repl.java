package ui;

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
        var loadGameMessage = new Gson().fromJson(message, LoadGameMessage.class);
        // update game
      }
      case ERROR -> {
        // print error
      }
      case NOTIFICATION -> {
        // print notification
      }
    }


    printPrompt();
  }

  private void printPrompt()  {
    System.out.print("\n" +  ">>> ");
  }
}
