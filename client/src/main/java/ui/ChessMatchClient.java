package ui;

import dataAccess.DataAccessException;
import exception.ResponseException;
import model.GameData;
import req.*;
import web.server.ServerFacade;
import java.util.Arrays;
import java.util.Objects;


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
        case "clearapp" -> clearApp();
        case "register" -> register(params);
        case "login" -> login(params);
        case "logout" -> logout();
        case "creategame" -> createGame(params);
        case "listgames" -> listGames();
        case "join" -> joinGame(params);
        case "observer" -> joinGameAsObserver(params);
        // other methods for websocket
        case "quit" -> "quit";
        default -> help();
      };
    } catch (ResponseException ex) {
      return ex.getMessage();
    }
  }

  public String clearApp() throws ResponseException {
    server.clearApp();
    return "db cleared";
  }
  public String register(String... params) throws ResponseException {
    if (params.length == 3) {
      state = State.LOGGED_IN;
      server.register(new RegistrationReq(params[0],params[1], params[2]));
      return String.format("You are logged as %s", params[0]);
    }
    throw new ResponseException(400, "Expected: <username> <password> <email>");
  }

  public String login(String... params) throws ResponseException {
    if (params.length == 2) {
      state = State.LOGGED_IN;
      server.login(new LoginReq(params[0], params[1]));
      //      visitorName = String.join("-", params);
      //      // websocket here
      return String.format("You are logged in as %s", params[0]);
    }
    throw new ResponseException(400, "Expected: <username> <password>");
  }

  public String logout() throws ResponseException {
    state = State.LOGGED_OUT;
      server.logout();
      return "You have logged out";
  }

  public String createGame(String... params) throws ResponseException {
    if (params.length == 1) {
      String gameName = params[0];
      gameID = server.createGame(new CreateGameReq(null, gameName)).gameID();
      return String.format("You created game %s with id: %d ", gameName, gameID);
    }
    throw new ResponseException(400, "Expected: <gameName>");
  }

  public String listGames() throws ResponseException {
      var gameArray = server.listGames().games();
      String[] games = new String[gameArray.length];
      int i = 0;
      for (GameData element : gameArray) {
        var gameName = element.gameName();
        games[i] = i+1 + ": " + gameName + element.whiteUsername() + element.blackUsername() + "\n"; //Reformat
        i++;
      }
      return Arrays.toString(games);
  }

  public String joinGame(String... params) throws ResponseException {
    var color = params[1].replace("[", "").replace("]", "");
    if (params.length == 2 && (color.equalsIgnoreCase("white")|| color.equalsIgnoreCase("black"))) { // player
      server.joinGame(new JoinGameReq(null, color, Integer.parseInt(params[0])));
    } else if (params.length == 2 && color.isEmpty()) { //observer
      server.joinGame(new JoinGameReq(null, null, Integer.parseInt(params[0])));
    } else {throw new ResponseException(400, "Expected: <gameID> [white|black]");}
    return printChessBoard();
  }

  public String joinGameAsObserver(String... params) throws ResponseException {
    if (params.length == 1) {
      server.joinGame(new JoinGameReq(null, null, Integer.parseInt(params[0])));
    } else {throw new ResponseException(400, "Expected: <gameID>");}
    return printChessBoard();
  }

  private String printChessBoard() {
    return """
                   a b c d e f g h 
                8 |r|n|b|q|k|b|n|r| 8
                7 |p|p|p|p|p|p|p|p| 7
                6 | | | | | | | | | 6
                5 | | | | | | | | | 5
                4 | | | | | | | | | 4
                3 | | | | | | | | | 3
                2 |P|P|P|P|P|P|P|P| 2
                1 |R|N|B|Q|K|B|N|R| 1
                   a b c d e f g h
                   
                   h g f e d c b a
                1 |R|N|B|K|Q|B|N|R| 1
                2 |P|P|P|P|P|P|P|P| 2
                3 | | | | | | | | | 3
                4 | | | | | | | | | 4
                5 | | | | | | | | | 5
                6 | | | | | | | | | 6
                7 |p|p|p|p|p|p|p|p| 7
                8 |r|n|b|k|q|b|n|r| 8
                   h g f e d c b a
                """;
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
            - join <gameID> [white|black]
            - observer <gameID>
            - logout
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
