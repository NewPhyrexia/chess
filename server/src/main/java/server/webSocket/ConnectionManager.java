package server.webSocket;

import org.eclipse.jetty.websocket.api.Session;

import javax.management.Notification;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
  public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

  public void add(String userName, Session session) {
    var connection = new Connection(userName, session);
    connections.put(userName, connection);
  }

  public void remove(String userName) {connections.remove(userName);}

  public void broadcast(String excludedUser, Notification notification) throws IOException {
    var removeConnections =  new ArrayList<Connection>();
    for (var c : connections.values()) {
      if (c.session.isOpen()) {
        if (!c.userName.equals(excludedUser)) {
          c.send(notification.toString());
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
