package ui;

import dataAccess.DataAccessException;
import exception.ResponseException;
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
        case "register" -> register(params);
//        case "login" -> login(params);
//        case "logout" -> logout(params);
//        case "createGame" -> createGame(params);
//        case "listGames" -> listGames(params);
//        case "joinGame" -> joinGame(params);
//        case "joinAsObserver" -> joinAsObserver(params);
        default -> help();
        // other methods
      };
    } catch (ResponseException ex) {
      return ex.getMessage();
    }
  }

  public String register(String... params) throws ResponseException {
    if (params.length >= 1) {
      state = State.LOGGED_IN;
      visitorName = String.join("-", params);
      // websocket here
      return String.format("You are logged in as %s", visitorName);
    }
    throw new ResponseException(400, "Expected: <username> <password> <email>");
  }

  public String help() {
    if (state == State.LOGGED_OUT) {
      return """
              - register <username> <password> <email>
              - quit
              """;
    }
    return """
            - other methods
            """;
  }

  private void assertSignedIn() throws ResponseException {
    if (state == State.LOGGED_OUT)  {
      throw new ResponseException(400, "You must be signed in");
    }
  }
  // method bodies below

}
