package webSocketMessages.serverMessages;

public class NotificationMessage extends ServerMessage {

  public NotificationMessage(ServerMessageType type, String message) {
    super(type);
    this.message = message;
  }

  private String message;

  public String getMessage() { return message; }
}
