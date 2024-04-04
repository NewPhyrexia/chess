package server.webSocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {
  @OnWebSocketMessage
  public void onMessage(Session session, String message) throws IOException {
    System.out.print(message); // for testing
    var dMessage = new Gson().fromJson(message, UserGameCommand.class);
    session.getRemote().sendString(message);
  }
}
