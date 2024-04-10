package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import dataAccess.interfaces.GameDAOInterface;
import exception.ResponseException;
import model.GameData;
import req.*;
import web.server.ServerFacade;
import web.websocket.NotificationHandler;
import web.websocket.WebSocketFacade;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;


public class ChessMatchClient {
  private final ServerFacade server;

  private final String serverUrl;

  private int gameID;

  private ChessGame.TeamColor userColor = null;

  private NotificationHandler notificationHandler;
  private WebSocketFacade ws;

  private State state = State.LOGGED_OUT;

  public ChessMatchClient(String serverUrl, NotificationHandler notificationHandler) {
    server = new ServerFacade(serverUrl);
    this.serverUrl = serverUrl;
    this.notificationHandler = notificationHandler;
  }

  public ChessGame.TeamColor getUserColor() { return userColor; }

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

  // Websocket methods

  public String joinGame(String... params) throws ResponseException, IOException {
    var color = params[1].replace("[", "").replace("]", "");
    color = color.toLowerCase();
    if (params.length == 2 && (color.equalsIgnoreCase("white")|| color.equalsIgnoreCase("black"))) { // player
      server.joinGame(new JoinGameReq(null, color, Integer.parseInt(params[0])));
    } else if (params.length == 2 && color.isEmpty()) { //observer
      server.joinGame(new JoinGameReq(null, null, Integer.parseInt(params[0])));
    } else {throw new ResponseException(400, "Expected: <gameID> [white|black]");}

    state = State.JOINED_GAME;
    ws = new WebSocketFacade(serverUrl, notificationHandler);
    gameID = Integer.parseInt(params[0]);

    if (color.equals("white")) {
      userColor = ChessGame.TeamColor.WHITE;
      ws.sendMessage(new JoinPlayerCommand(server.getAuthToken(), gameID, ChessGame.TeamColor.WHITE));
    } else {
      userColor = ChessGame.TeamColor.BLACK;
      ws.sendMessage(new JoinPlayerCommand(server.getAuthToken(), gameID, ChessGame.TeamColor.BLACK));
    }
    return "";
  }

  public String joinGameAsObserver(String... params) throws ResponseException, IOException {
    if (params.length == 1) {
      server.joinGame(new JoinGameReq(null, null, Integer.parseInt(params[0])));
    } else {throw new ResponseException(400, "Expected: <gameID>");}

    state = State.JOINED_AS_OBSERVER;
    userColor = ChessGame.TeamColor.WHITE;
    ws = new WebSocketFacade(serverUrl, notificationHandler);
    gameID = Integer.parseInt(params[0]);
    var command = new JoinObserverCommand(server.getAuthToken(), gameID);
    ws.sendMessage(command);
    return "";
  }

  public String redrawBoard() throws ResponseException {
    new RenderBoard().drawChessBoard(Repl.getGame(), userColor);
    return "";
  }

  public String highlightMove(String... params) throws ResponseException {
    // takes in a chess position
    if (params.length == 1){
      String[] stringArray = params[0].toLowerCase().split("");
      String validChar = "abcdefgh";
      String validNum = "12345678";
      if ((validChar.contains(stringArray[0]) && validNum.contains(stringArray[1]))) {
        var row=Integer.valueOf(stringArray[1]);
        var col=letterToNum(stringArray[0]);
        var position = new ChessPosition(row, col);
        // pass position into method for highlight

      }

    } else {
      throw new ResponseException(400, "Expected: <a2>");
    }
    return "";
  }

  public String makeMove(String... params) throws ResponseException, IOException {
    if (params.length == 1) {
      String[] stringArray = params[0].toLowerCase().split("");
      String validChar = "abcdefgh";
      String validNum = "12345678";
      if ((validChar.contains(stringArray[0]) && validNum.contains(stringArray[1])
              && (validChar.contains(stringArray[2])) && validNum.contains(stringArray[3]))) {
        var row1=Integer.valueOf(stringArray[1]);
        var col1=letterToNum(stringArray[0]);

        var row2=Integer.valueOf(stringArray[3]);
        var col2=letterToNum(stringArray[2]);

        ChessMove move=new ChessMove(new ChessPosition(row1, col1), new ChessPosition(row2, col2), null);
        ws=new WebSocketFacade(serverUrl, notificationHandler);
        var command=new MakeMoveCommand(server.getAuthToken(), gameID, move);
        ws.sendMessage(command);
      }
    } else {
      throw new ResponseException(400, "Expected: <a2a4>");
    }
    return "";
  }

  private int letterToNum(String c) {
    int num = 0;
    switch (c){
      case "a" -> num = 1;
      case "b" -> num = 2;
      case "c" -> num = 3;
      case "d" -> num = 4;
      case "e" -> num = 5;
      case "f" -> num = 6;
      case "g" -> num = 7;
      case "h" -> num = 8;
    }
    return num;
  }

  public String resignGame() throws ResponseException, IOException {
    Scanner scanner = new Scanner(System.in);
    var result = "";
    while (true) {
      System.out.println("Are you sure you want to resign? [yes|no]");
      System.out.print(">>> ");
      result=scanner.nextLine();
      if (result.equalsIgnoreCase("yes")) {
        ws = new WebSocketFacade(serverUrl, notificationHandler);
        var command = new ResignCommand(server.getAuthToken(), gameID);
        ws.sendMessage(command);
        break;
      } else if (result.equalsIgnoreCase("no")) {
        break;
      }
    }
    return "";
  }

  public String leaveGame() throws ResponseException, IOException {
    state = State.LOGGED_IN;
    userColor = null;
    ws.sendMessage(new LeaveCommand(server.getAuthToken(), gameID));
    ws.closeConnection();
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
              - makeMove <start position end position>
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

//  private void assertSignedIn() throws ResponseException {
//    if (state == State.LOGGED_OUT)  {
//      throw new ResponseException(400, "You must be signed in");
//    }
//  }
}
