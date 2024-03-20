package ui;

import dataAccess.DataAccessException;
import exception.ResponseException;
import req.RegistrationReq;
import web.server.ServerFacade;
import java.util.Arrays;


public class ChessMatchClient {
  private String visitorName = null;

  private final ServerFacade server;

  private final String serverUrl;

  private String authToken;

//  private NotificationHandler notificationHandler; // websocket
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
        case "login" -> login(params);
        case "logout" -> logout();
        case "createGame" -> createGame(params);
        case "listGames" -> listGames();
        case "joinGame" -> joinGame(params);
        case "joinAsObserver" -> joinAsObserver(params);
        // other methods for websocket
        default -> help();
      };
    } catch (ResponseException ex) {
      return ex.getMessage();
    }
  }

  public String register(String... params) throws ResponseException {
    if (params.length >= 3) {
      state = State.LOGGED_IN;
//      visitorName = String.join("-", params);
//      // websocket here
      authToken = server.register(new RegistrationReq(params[0],params[1], params[2]));
      return String.format("You are registered as %s", params[0]);
    }
    throw new ResponseException(400, "Expected: <username> <password> <email>");
  }

  public String login(String... params) {
    return null;
  }

  public String logout() {
    return null;
  }

  public String createGame(String... params) {
    return null;
  }

  public String listGames() {
    return null;
  }

  public String joinGame(String... params) {
    return null;
  }

  public String joinAsObserver(String... params) {
    return null;
  }

  public String help() {
    if (state == State.LOGGED_OUT) {
      return """
              - register <username> <password> <email>
              - login <username> <password>
              - quit
              - help
              """;
    }
//    else if (state == State.JOINED_GAME) { //websocket place holder
//      return """
//              -
//              """;
//    } else if (state == State.JOINED_AS_OBSERVER) {
//      return """
//              -
//              """;
//    }
    return """
            - createGame <gameName>
            - listGames
            - join <gameID> [WHITE|BLACK|<empty>]
            - observer <gameID>
            - logout
            - quit
            - help
            """;
  }

  private void assertSignedIn() throws ResponseException {
    if (state == State.LOGGED_OUT)  {
      throw new ResponseException(400, "You must be signed in");
    }
  }
  // method bodies below

}
