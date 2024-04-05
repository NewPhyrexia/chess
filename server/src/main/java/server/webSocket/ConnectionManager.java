package server.webSocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
  public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

  public void add(String userName, int gameID, Session session) {
    var connection = new Connection(userName, gameID, session);
    connections.put(userName, connection);
  }

  public void remove(String userName) {connections.remove(userName);}

  public void broadcast(String excludedUser, ServerMessage message) throws IOException {
    var removeConnections =  new ArrayList<Connection>();
    for (var c : connections.values()) {
      if (c.session.isOpen()) {
        if (!c.userName.equals(excludedUser)) {
          c.send(new Gson().toJson(message));
        }
      } else {
        removeConnections.add(c);
      }
    }

    // Close sessions that were left open
    for (var c : removeConnections) {
      connections.remove(c.userName);
    }
  }
}
