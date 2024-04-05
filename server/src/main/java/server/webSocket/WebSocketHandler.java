package server.webSocket;

import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.SqlAuthDAO;
import dataAccess.SqlGameDAO;
import dataAccess.interfaces.AuthDAOInterface;
import dataAccess.interfaces.GameDAOInterface;
import model.AuthData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.userCommands.JoinPlayerCommand;
import webSocketMessages.userCommands.MakeMoveCommand;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;
import java.util.Objects;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;

@WebSocket
public class WebSocketHandler {

  private final ConnectionManager connections = new ConnectionManager();
  private final AuthDAOInterface authInterface = SqlAuthDAO.getInstance();
  private final GameDAOInterface gameInterface = SqlGameDAO.getInstance();

  @OnWebSocketMessage
  public void onMessage(Session session, String message) throws IOException, DataAccessException, InvalidMoveException {
    var userGameCommand = new Gson().fromJson(message, UserGameCommand.class);
    var joinPlayerCommand = new Gson().fromJson(message, JoinPlayerCommand.class);
    var makeMoveCommand = new Gson().fromJson(message, MakeMoveCommand.class);

    switch (userGameCommand.getCommandType()) {

      case LEAVE -> leave(userGameCommand, session);

      case RESIGN -> resign(userGameCommand, session);

      case MAKE_MOVE -> makeMove(makeMoveCommand, session);

      case JOIN_PLAYER -> joinPlayer(joinPlayerCommand, session);

      case JOIN_OBSERVER -> joinObserver(userGameCommand, session);
    }
  }

  private void leave(UserGameCommand command, Session session) throws IOException, DataAccessException {
    var authToken=authCheck(command, session);
    var userName = authToken.username();
    var gameData = gameInterface.getGame(command.getGameID());

    if (gameData == null) {
      var errorMessage = new ErrorMessage("Error: Game does not exist");
      session.getRemote().sendString(new Gson().toJson(errorMessage));
    } else {
        // remove root from game and update db
      if (Objects.equals(gameData.blackUsername(), userName)) {
        gameInterface.updateGame("black", gameData.gameID(), null, gameData.game());
      } else if (Objects.equals(gameData.whiteUsername(), userName)) {
        gameInterface.updateGame("white", gameData.gameID(), null, gameData.game());
      }
      // notify other clients that root left the game
      var message = String.format("%s has left the game", userName);
      connections.broadcast(userName, new NotificationMessage(message));
      connections.remove(userName);
    }
  }

  private void resign(UserGameCommand command, Session session) throws IOException, DataAccessException {
    var authToken=authCheck(command, session);
    var userName = authToken.username();
    var gameData = gameInterface.getGame(command.getGameID());

    if (gameData == null) {
      var errorMessage = new ErrorMessage("Error: Game does not exist");
      session.getRemote().sendString(new Gson().toJson(errorMessage));
    }

    var blackUser = gameData.blackUsername();
    var whiteUser = gameData.whiteUsername();
    if (gameData.game().getGameOverStatus()) {
      var errorMessage = new ErrorMessage("Error: Game has already finished");
      session.getRemote().sendString(new Gson().toJson(errorMessage));
    } else if (!Objects.equals(blackUser, userName) && !Objects.equals(whiteUser, userName)) {
      var errorMessage=new ErrorMessage("Error: Observer cannot resign for a player");
      session.getRemote().sendString(new Gson().toJson(errorMessage));
    } else {
      gameData.game().gameOver(); // update game to gameOver true
      if (Objects.equals(blackUser, userName)) {
        gameInterface.updateGame("black", gameData.gameID(), userName, gameData.game());
      } else {
        gameInterface.updateGame("white", gameData.gameID(), userName, gameData.game());
      }
      // notify -all- clients that root left the game
      var message = String.format("%s has resigned", userName);
      connections.broadcast(null, new NotificationMessage(message));
    }
  }

  private void makeMove(MakeMoveCommand command, Session session) throws IOException, DataAccessException, InvalidMoveException {
    var authToken=authCheck(command, session);
    var userName = authToken.username();
    var gameData = gameInterface.getGame(command.getGameID());
    var move = command.getMove();
    var startPos = move.getStartPosition();
    var endPos = move.getEndPosition();

    if (gameData == null) {
      var errorMessage = new ErrorMessage("Error: Game does not exist");
      session.getRemote().sendString(new Gson().toJson(errorMessage));
    }

    var game = gameData.game();
    var piece = game.getBoard().getPiece(startPos);
    var blackUser = gameData.blackUsername();
    var whiteUser = gameData.whiteUsername();

    if (game.getGameOverStatus()) {
      var errorMessage = new ErrorMessage("Error: Game has already finished");
      session.getRemote().sendString(new Gson().toJson(errorMessage));
    } else if (!Objects.equals(blackUser, userName) && !Objects.equals(whiteUser, userName)) {
      var errorMessage=new ErrorMessage("Error: Observer cannot move for a player");
      session.getRemote().sendString(new Gson().toJson(errorMessage));
    } else if (!game.validMoves(startPos).contains(move)) { // syntax may be executed incorrectly
      var errorMessage=new ErrorMessage("Error: Invalid move");
      session.getRemote().sendString(new Gson().toJson(errorMessage));
    } else {
      var turn = gameData.game().getTeamTurn();
      if (turn == WHITE && Objects.equals(whiteUser, userName)) {
        game.makeMove(move);
        gameInterface.updateGame("white", gameData.gameID(), userName, game);
      } else if (turn == BLACK && Objects.equals(blackUser, userName)) {
        game.makeMove(move);
        gameInterface.updateGame("black", gameData.gameID(), userName, game);
      } else {
        var errorMessage=new ErrorMessage("Error: Opponent's turn");
        session.getRemote().sendString(new Gson().toJson(errorMessage));
        return;
      }

      // send game to all
      connections.broadcast(null, new LoadGameMessage(gameData.game()));
      // send notification to all but root
      var message = String.format("%s has made move: %s from %s to %s.", userName, piece, startPos, endPos);
      connections.broadcast(userName, new NotificationMessage(message));

      if (game.isInCheckmate(WHITE)) {
        game.gameOver();
        var msg = String.format("%s is in checkmate", whiteUser);
        connections.broadcast(null, new NotificationMessage(msg));
      } else if (game.isInCheckmate(BLACK)) {
        game.gameOver();
        var msg = String.format("%s is in checkmate", blackUser);
        connections.broadcast(null, new NotificationMessage(msg));
      }
    }
  }

  private void joinPlayer(JoinPlayerCommand command, Session session) throws IOException, DataAccessException {
    var authToken=authCheck(command, session);
    var userName = authToken.username();
    var gameData = gameInterface.getGame(command.getGameID());

    if (gameData == null) {
      var errorMessage = new ErrorMessage("Error: Game does not exist");
      session.getRemote().sendString(new Gson().toJson(errorMessage));
    } else if ((command.getPlayerColor() == WHITE && !Objects.equals(gameData.whiteUsername(), userName))
            || (command.getPlayerColor() == BLACK && !Objects.equals(gameData.blackUsername(), userName))) {
      var errorMessage = new ErrorMessage("Error: PlayerColor already taken");
      session.getRemote().sendString(new Gson().toJson(errorMessage));
    } else {
      connections.add(userName, command.getGameID(), session);
      // sends a Notification message to all other clients informing them what color the root client is joining as
      var message = String.format("%s has joined the game as %s", userName, command.getPlayerColor());
      connections.broadcast(userName, new NotificationMessage(message));

      // Server sends a LOAD_GAME message back to the root client.
      session.getRemote().sendString(new Gson().toJson(new LoadGameMessage(gameData.game())));
    }
  }

  private void joinObserver(UserGameCommand command, Session session) throws IOException, DataAccessException {
    var authToken=authCheck(command, session);
    var userName = authToken.username();
    var gameData = gameInterface.getGame(command.getGameID());

    if (gameData == null) {
      var errorMessage = new ErrorMessage("Error: Game does not exist");
      session.getRemote().sendString(new Gson().toJson(errorMessage));
    } else {
      connections.add(userName, command.getGameID(), session);
      // sends a Notification message to all other clients informing them what color the root client is joining as
      var message = String.format("%s has joined the game as an observer", userName);
      connections.broadcast(userName, new NotificationMessage(message));

      // Server sends a LOAD_GAME message back to the root client.
      session.getRemote().sendString(new Gson().toJson(new LoadGameMessage(gameData.game())));
    }
  }

  private AuthData authCheck(UserGameCommand command, Session session) throws DataAccessException, IOException {
    var commandAuthToken = command.getAuthString();
    var authToken = authInterface.getAuthToken(commandAuthToken);

    if ( authToken == null) {
      // send "error" message to root
      var errorMessage = new ErrorMessage("Error: unauthorized");
      session.getRemote().sendString(new Gson().toJson(errorMessage));
    }
    return authToken;
  }
}
