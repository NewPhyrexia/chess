package ui;

import dataAccess.DataAccessException;
import exception.ResponseException;
import req.*;
import web.server.ServerFacade;
import java.util.Arrays;


public class ChessMatchClient {
  private String visitorName = null;

  private final ServerFacade server;

  private final String serverUrl;

  private int gameID;

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
        case "clearApp" -> clearApp();
        case "register" -> register(params);
        case "login" -> login(params);
        case "logout" -> logout();
//        case "createGame" -> createGame(params);
//        case "listGames" -> listGames();
//        case "join", "observer" -> joinGame(params);
        // other methods for websocket
        default -> help();
      };
    } catch (ResponseException ex) {
      return ex.getMessage();
    }
  }

  public String clearApp() throws ResponseException {
    if (true) {
      server.clearApp();
      return "db cleared";
    }
    throw new ResponseException(400, "failed to clear db");
  }
  public String register(String... params) throws ResponseException {
    if (params.length >= 3) {
      server.register(new RegistrationReq(params[0],params[1], params[2]));
      return String.format("You are logged as %s", params[0]);
    }
    throw new ResponseException(400, "Expected: <username> <password> <email>");
  }

  public String login(String... params) throws ResponseException {
    if (params.length >= 2) {
      state = State.LOGGED_IN;
      server.login(new LoginReq(params[0], params[1]));
      //      visitorName = String.join("-", params);
      //      // websocket here
      return String.format("You are logged in as %s", params[0]);
    }
    throw new ResponseException(400, "Expected: <username> <password>");
  }

  public String logout() throws ResponseException {
      server.logout();
      return "You have logged out";
  }

//  public String createGame(String... params) throws ResponseException {
//    if (params.length >= 1) {
//      gameID = server.createGame(new CreateGameReq(authToken, params[0]));
//      return String.format("You created game %s with id: %d ", params[0], gameID);
//    }
//    throw new ResponseException(400, "Expected: <gameName>");
//  }
//
//  public String listGames() throws ResponseException {
//    if (!authToken.isEmpty()) {
//      var games = server.listGames(new ListGamesReq(authToken));
//      return ""; // how do I list all the games in the return?
//    }
//    throw new ResponseException(400, "Not a valid user to list games");
//  }
//
//  public String joinGame(String... params) throws ResponseException {
//    if (params.length >= 2 && !params[1].isEmpty()) { // player
//      server.joinGame(new JoinGameReq(authToken, params[1], Integer.parseInt(params[0])));
//    } else if (params.length >= 2) { //observer
//      server.joinGame(new JoinGameReq(authToken, null, Integer.parseInt(params[0])));
//    }
//    throw new ResponseException(400, "Expected: <gameID> [WHITE|BLACK|<empty>]");
//  }

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
