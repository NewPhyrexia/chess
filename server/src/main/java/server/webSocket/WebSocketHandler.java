package server.webSocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {

  private final ConnectionManager connections = new ConnectionManager();

  @OnWebSocketMessage
  public void onMessage(Session session, String message) throws IOException {
    System.out.print(message); // for testing
    var userGameCommand = new Gson().fromJson(message, UserGameCommand.class);
    switch (userGameCommand.getCommandType()) {
      case LEAVE -> leave();
      case RESIGN -> resign();
      case MAKE_MOVE -> makeMove();
      case JOIN_PLAYER -> joinPlayer();
      case JOIN_OBSERVER -> joinObserver();
    }
    session.getRemote().sendString(message);  // for testing
  }

  private void leave() throws IOException {
  }

  private void resign() throws IOException {
  }

  private void makeMove() throws IOException {
  }

  private void joinPlayer() throws IOException {

  }

  private void joinObserver() throws IOException {
  }
}
