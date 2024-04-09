package ui;

import chess.ChessGame;
import dataAccess.DataAccessException;
import exception.ResponseException;
import model.GameData;
import req.*;
import web.server.ServerFacade;
import web.websocket.NotificationHandler;
import web.websocket.WebSocketFacade;
import webSocketMessages.userCommands.JoinPlayerCommand;
import webSocketMessages.userCommands.LeaveCommand;
import webSocketMessages.userCommands.ResignCommand;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;


public class ChessMatchClient {
  private String userName = null;

  private final ServerFacade server;

  private final String serverUrl;

  private int gameID;

  private NotificationHandler notificationHandler;
  private WebSocketFacade ws;

  private Repl repl;
  private State state = State.LOGGED_OUT;

  public ChessMatchClient(String serverUrl, NotificationHandler notificationHandler) {
    server = new ServerFacade(serverUrl);
    this.serverUrl = serverUrl;
    this.notificationHandler = notificationHandler;
  }

  public String eval(String input) {
    try {
      var tokens = input.split(" ");
      var cmd = (tokens.length > 0) ? tokens[0].toLowerCase() : "help";
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
        case "redraw" -> redrawBoard();
        case "highlight" -> highlightMove(params);
        case "makemove" -> makeMove(params);
        case "resign" -> resignGame(); // might need a gameID to resign
        case "leave" -> leaveGame();
        case "quit" -> "quit";
        default -> help();
      };
    } catch (ResponseException | IOException ex) {
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
      server.register(new RegistrationReq(params[0], params[1], params[2]));
      return String.format("You are logged as %s", params[0]);
    }
    throw new ResponseException(400, "Expected: <username> <password> <email>");
  }

  public String login(String... params) throws ResponseException {
    if (params.length == 2) {
      state = State.LOGGED_IN;
      server.login(new LoginReq(params[0], params[1]));
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
      String[] games = new String[gameArray.length+1];
      int i = 0;
      for (GameData element : gameArray) {
        var gameName = element.gameName();
        var temp = i+1;
        games[i] =  "\n" + temp + ": " + gameName + " | whitePlayer: " + element.whiteUsername() + " | blackPlayer: " + element.blackUsername();
        i++;
      }
      games[i] = "\n";
      return Arrays.toString(games);
  }

  public String joinGame(String... params) throws ResponseException, IOException {
    var color = params[1].replace("[", "").replace("]", "");
    color = color.toLowerCase();
    if (params.length == 2 && (color.equalsIgnoreCase("white")|| color.equalsIgnoreCase("black"))) { // player
      server.joinGame(new JoinGameReq(null, color, Integer.parseInt(params[0])));
    } else if (params.length == 2 && color.isEmpty()) { //observer
      server.joinGame(new JoinGameReq(null, null, Integer.parseInt(params[0])));
    } else {throw new ResponseException(400, "Expected: <gameID> [white|black]");}

    state = State.JOINED_GAME;
    ws = new WebSocketFacade(serverUrl, repl);

    if (color.equals("white")) {
      ws.sendMessage(new JoinPlayerCommand(server.getAuthToken(), gameID, ChessGame.TeamColor.WHITE));
    } else {ws.sendMessage(new JoinPlayerCommand(server.getAuthToken(), gameID, ChessGame.TeamColor.BLACK));}

    var game = new ChessGame();
    new RenderBoard(game).main();
    return "";
  }

  public String joinGameAsObserver(String... params) throws ResponseException {
    if (params.length == 1) {
      server.joinGame(new JoinGameReq(null, null, Integer.parseInt(params[0])));
    } else {throw new ResponseException(400, "Expected: <gameID>");}
    state = State.JOINED_AS_OBSERVER;
//    ws = new WebSocketFacade(serverUrl, repl);
//    var command = new UserGameCommand(server.getAuthToken(), gameID);
    // set command type to Join observer
//    var response = ws.sendMessage(command);
    // do something with string response

    var game = new ChessGame();
    new RenderBoard(game).main();
    return "";
  }

  // Websocket methods

  public String redrawBoard() throws ResponseException {
    return "";
  }

  public String highlightMove(String... params) throws ResponseException {
    return "";
  }

  public String makeMove(String... params) throws ResponseException {
    return "";
  }

  public String resignGame() throws ResponseException, IOException {
    // prompt 'are you sure?'


    // main logic
    ws = new WebSocketFacade(serverUrl, repl);
    var command = new ResignCommand(server.getAuthToken(), gameID);
    ws.sendMessage(command);
    return "";
  }

  public String leaveGame() throws ResponseException, IOException {
    state = State.LOGGED_IN;
    // send leave command to server facade
    ws.sendMessage(new LeaveCommand(server.getAuthToken(), gameID));
    // remove from ws
//    ws.close();  // do I just set this to null?
    return "";
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
    else if (state == State.JOINED_GAME) {
      return """
              - redraw
              - highlight <piece position>
              - makeMove <piece position> <end position>
              - resign
              - leave
              - help
              """;
    } else if (state == State.JOINED_AS_OBSERVER) {
      return """
              - redraw
              - highlight <piece position>
              - leave
              - help
              """;
    }
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
}
