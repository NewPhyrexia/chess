package ui;

import web.server.ServerFacade;
import java.util.Arrays;


public class ChessMatchClient {
  private String visitorName = null;

  private final ServerFacade server;

  private final String serverUrl;

//  private NotificationHandler notificationHandeler; // websocket
//  private WebSocketFacade ws; // websocket
  private State state = State.LOGGED_OUT;

  public ChessMatchClient(String serverUrl) {
    server = new ServerFacade(serverUrl);
    this.serverUrl = serverUrl;
//    this.notificationHandler = notificationHandler; // Websocket
  }

  public String eval(String input) {
    try {
      var tokens = input.toLowerCase().split(" ");
      var cmd = (tokens.length > 0) ? tokens[0] : "help";
      var params = Arrays.copyOfRange(tokens, 1, tokens.length);
      return switch (cmd) {
        case "signin" -> signIn(params);
        // other methods
      }
    }
  }


  // method bodies below

}
