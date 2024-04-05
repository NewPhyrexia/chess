package server.webSocket;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.SqlAuthDAO;
import dataAccess.SqlGameDAO;
import dataAccess.interfaces.AuthDAOInterface;
import dataAccess.interfaces.GameDAOInterface;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;
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
  private GameDAOInterface gameInterface = SqlGameDAO.getInstance();

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

  private void leave(UserGameCommand command, Session session) throws IOException {
    var authToken = command.getAuthString();

  }

  private void resign(UserGameCommand command, Session session) throws IOException {
    var authToken = command.getAuthString();

  }

  private void makeMove(MakeMoveCommand command, Session session) throws IOException {
    var authToken = command.getAuthString();

  }

  private void joinPlayer(JoinPlayerCommand command, Session session) throws IOException, DataAccessException {
    var commandAuthToken = command.getAuthString();
    var authToken = authInterface.getAuthToken(commandAuthToken);
    var gameData = gameInterface.getGame(command.getGameID());

    if ( authToken == null) {
      // send "error" message to root
      var errorMessage = new ErrorMessage("Error: unauthorized");
      session.getRemote().sendString(new Gson().toJson(errorMessage));
    }

    // additional checks
    var userName = authToken.username();
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
      session.getRemote().sendString(new Gson().toJson(new LoadGameMessage(gameData.implementation())));
    }
  }

  private void joinObserver(UserGameCommand command, Session session) throws IOException {
    var authToken = command.getAuthString();

  }
}
