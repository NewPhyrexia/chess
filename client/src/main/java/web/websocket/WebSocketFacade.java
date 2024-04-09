package web.websocket;

import com.google.gson.Gson;
import exception.ResponseException;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {

  Session session;
  NotificationHandler notificationHandler;

  public WebSocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException {
    try {
      url = url.replace("http", "ws");
      URI socketURI = new URI(url + "/connect");
      this.notificationHandler = notificationHandler;

      WebSocketContainer container =ContainerProvider.getWebSocketContainer();
      this.session = container.connectToServer(this, socketURI);

      this.session.addMessageHandler(new MessageHandler.Whole<String>() {
        @Override
        public void onMessage(String message) {
          ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);

          // filter messages here to notify
          switch (serverMessage.getServerMessageType()) {

            case LOAD_GAME -> {
              var loadGameMessage = new Gson().fromJson(message, LoadGameMessage.class);
              notificationHandler.notify(loadGameMessage);
            }
            case ERROR -> {
              var errorMessage = new Gson().fromJson(message, ErrorMessage.class);
              notificationHandler.notify(errorMessage);
            }
            case NOTIFICATION -> {
              var notificationMessage = new Gson().fromJson(message, NotificationMessage.class);
              notificationHandler.notify(notificationMessage);
            }
          }
        }
      });
    } catch (DeploymentException | IOException | URISyntaxException ex) {
      throw new ResponseException(500, ex.getMessage());
    }
  }

  @Override
  public void onOpen(Session session, EndpointConfig endpointConfig) {
  }


  public void closeConnection() throws IOException {
    session.close();
  }
  public void sendMessage(UserGameCommand command) throws IOException {
    var jsonCommand = new Gson().toJson(command);
    this.session.getBasicRemote().sendText(jsonCommand);
  }

}
