package server.webSocket;

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
  public void onMessage(Session session, String message) throws IOException, DataAccessException {
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
        gameInterface.updateGame("black", gameData.gameID(), null);
      } else if (Objects.equals(gameData.whiteUsername(), userName)) {
        gameInterface.updateGame("white", gameData.gameID(), null);
      }
      // notify other clients that root left the game
      var message = String.format("%s has left the game", userName);
      connections.broadcast(userName, new NotificationMessage(message));
      connections.remove(userName); // id this where im supposed to remove the connection? -------------------------------------
    }
  }

  private void resign(UserGameCommand command, Session session) throws IOException, DataAccessException {
    var authToken=authCheck(command, session);
    var userName = authToken.username();
    var gameData = gameInterface.getGame(command.getGameID());

    if (gameData == null) {
      var errorMessage = new ErrorMessage("Error: Game does not exist");
      session.getRemote().sendString(new Gson().toJson(errorMessage));
    } else if (!gameData.game().getGameOverStatus()) {
      var errorMessage = new ErrorMessage("Error: Game has already finished");
      session.getRemote().sendString(new Gson().toJson(errorMessage));
    } else {
      // update game to gameOver true
      if (Objects.equals(gameData.blackUsername(), userName)) {
        gameData.game().gameOver();
        gameInterface.updateGame("black", gameData.gameID(), userName);
        // notify other clients that root left the game
        var message = String.format("%s has resigned", userName);
        connections.broadcast(userName, new NotificationMessage(message));
      } else if (Objects.equals(gameData.whiteUsername(), userName)) {
        gameData.game().gameOver();
        gameInterface.updateGame("white", gameData.gameID(), userName);
        // notify other clients that root left the game
        var message = String.format("%s has resigned", userName);
        connections.broadcast(userName, new NotificationMessage(message));
      } else {
        var errorMessage = new ErrorMessage("Error: Observer cannot resign for a player");
        session.getRemote().sendString(new Gson().toJson(errorMessage));
      }
    }
  }

  private void makeMove(MakeMoveCommand command, Session session) throws IOException, DataAccessException {
    var authToken=authCheck(command, session);


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
