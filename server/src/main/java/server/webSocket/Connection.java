package server.webSocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {
  public String userName;
  public Session session;
  public int gameID;

  public Connection(String userName, int gameID, Session session) {
    this.userName = userName;
    this.session = session;
    this.gameID = gameID;
  }

  public void send(String message) throws IOException {
    session.getRemote().sendString(message);
  }
}
