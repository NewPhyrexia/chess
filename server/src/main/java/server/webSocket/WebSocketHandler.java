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
import webSocketMessages.userCommands.JoinPlayerCommand;
import webSocketMessages.userCommands.MakeMoveCommand;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;

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

    if (authInterface.getAuthToken(commandAuthToken) == null) {
      // send "error" message
    }
    connections.add(command.getUserName(), command.getGameID(), session);

    // notify all but root that root joined game as ---- color

    // second notification to root with the game as the message in a json
  }

  private void joinObserver(UserGameCommand command, Session session) throws IOException {
    var authToken = command.getAuthString();

  }
}
