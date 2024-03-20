package ui;

import java.util.Scanner;
import ui.EscapeSequences.*;


public class Repl {

  private final ChessMatchClient client;

  public Repl(String serverUrl) {client = new ChessMatchClient(serverUrl);}

  public void run() {
    System.out.println("Welcome to Chess.");
//    System.out.println("Welcome to Chess."); // add client.help() here

    Scanner scanner = new Scanner(System.in);
    var results = "";
    while (!results.equals("quit")) {
      printPrompt();
      String line = scanner.nextLine();

      try {
        result = client.eval(line);
        System.out.print(BLUE + results); // need to change to match my EscapeSeq
      } catch (Throwable e) {
        System.out.print(e.getMessage());
      }
    }
    System.out.println();
  }

//  public void notify (Notification notification) {  // stubbed for web sockets
//    System.out.println(RED + notification.message());
//    printPrompt();
//  }

  private void printPrompt()  {
    System.out.print("\n" + RESET + ">>> " + GREEN);
  }


}
