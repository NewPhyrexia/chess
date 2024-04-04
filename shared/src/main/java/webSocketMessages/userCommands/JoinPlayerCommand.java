package webSocketMessages.userCommands;

public class JoinPlayerCommand extends UserGameCommand {
  public JoinPlayerCommand(String authToken, int gameID, String playerColor) {
    super(authToken, gameID);
    this.playerColor = playerColor;
    this.commandType = CommandType.JOIN_PLAYER;
  }

  private String playerColor;
  public String getPlayerColor() { return playerColor; }
}
