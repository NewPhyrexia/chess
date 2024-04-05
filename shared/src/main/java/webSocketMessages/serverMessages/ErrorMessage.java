package webSocketMessages.serverMessages;

public class ErrorMessage extends ServerMessage {

  public ErrorMessage( String errorMessage) {
    super(ServerMessageType.ERROR);
    this.errorMessage = errorMessage;
  }
  private String errorMessage;

  public String getErrorMessage() { return errorMessage; }
}
