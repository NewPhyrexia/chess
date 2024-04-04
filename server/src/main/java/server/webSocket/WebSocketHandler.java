package server.webSocket;

import com.google.gson.Gson;
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

  @OnWebSocketMessage
  public void onMessage(Session session, String message) throws IOException {
    var userGameCommand = new Gson().fromJson(message, UserGameCommand.class);
    var joinPlayerCommand = new Gson().fromJson(message, JoinPlayerCommand.class);
    var makeMoveCommand = new Gson().fromJson(message, MakeMoveCommand.class);

    switch (userGameCommand.getCommandType()) {

      case LEAVE -> leave();

      case RESIGN -> resign();

      case MAKE_MOVE -> makeMove(makeMoveCommand, session);

      case JOIN_PLAYER -> joinPlayer(joinPlayerCommand, session);

      case JOIN_OBSERVER -> joinObserver();
    }
  }

  private void leave() throws IOException {

  }

  private void resign() throws IOException {

  }

  private void makeMove(MakeMoveCommand command, Session session) throws IOException {

  }

  private void joinPlayer(JoinPlayerCommand command, Session session) throws IOException {
    // check authtoken
    // get userName
        //    connections.add(userName,session); // add in gameID
    // notify all but root that root joined game as ---- color
    // second notification to root with the game as the message in a json
  }

  private void joinObserver() throws IOException {

  }
}
