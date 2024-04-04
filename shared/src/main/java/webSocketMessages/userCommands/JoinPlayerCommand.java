package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayerCommand extends UserGameCommand {
  public JoinPlayerCommand(String userName, String authToken, int gameID, ChessGame.TeamColor playerColor) {
    super(userName, authToken, gameID);
    this.playerColor = playerColor;
    this.commandType = CommandType.JOIN_PLAYER;
  }

  private ChessGame.TeamColor playerColor;
  public ChessGame.TeamColor getPlayerColor() { return playerColor; }
}
