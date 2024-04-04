package server.webSocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.userCommands.JoinPlayerCommand;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {

  private final ConnectionManager connections = new ConnectionManager();

  @OnWebSocketMessage
  public void onMessage(Session session, String message) throws IOException {
//    System.out.print(message); // for testing
    var userGameCommand = new Gson().fromJson(message, UserGameCommand.class);
    switch (userGameCommand.getCommandType()) {

      case LEAVE:
        leave();
        break;
      case RESIGN:
        resign();
        break;

      case MAKE_MOVE:
        makeMove();
        break;

      case JOIN_PLAYER:
        JoinPlayerCommand command = new Gson().fromJson(message, JoinPlayerCommand.class);
        joinPlayer(command, session);
        break;

      case JOIN_OBSERVER:
        joinObserver();
        break;

    }
//    session.getRemote().sendString(message);  // for testing
  }

  private void leave() throws IOException {
  }

  private void resign() throws IOException {
  }

  private void makeMove() throws IOException {
  }

  private void joinPlayer(JoinPlayerCommand command, Session session) throws IOException {
    // check authtoken
    // get userName
    // notify all but root that root joined game as ---- color
    // second notification to root with the game as the message in a json
//    connections.add(userName,session); // add in gameID

  }

  private void joinObserver() throws IOException {
  }
}
