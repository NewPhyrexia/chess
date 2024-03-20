package ui;

import dataAccess.DataAccessException;
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
        default -> help();
        // other methods
      }
    }
  }

  public String help() {
    if (state == State.LOGGED_OUT) {
      return """
              - signIn <username>
              - quit
              """;
    }
    return """
            - other methods
            """;
  }

  private void assertSignedIn() throws DataAccessException {
    if (state == State.LOGGED_OUT)  {
      throw new DataAccessException("You must be signed in");
    }
  }
  // method bodies below

}
