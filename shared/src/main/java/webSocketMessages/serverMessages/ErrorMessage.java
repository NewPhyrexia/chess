package webSocketMessages.serverMessages;

public class ErrorMessage extends ServerMessage {

  public ErrorMessage(ServerMessageType type, String errorMessage) {
    super(type);
    this.errorMessage = errorMessage;
  }
  private String errorMessage;

  public String getErrorMessage() { return errorMessage; }
}
