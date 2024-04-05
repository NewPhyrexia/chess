package webSocketMessages.serverMessages;

import chess.ChessGame;

public class LoadGameMessage extends ServerMessage{
  public LoadGameMessage(ChessGame game) {
    super(ServerMessageType.LOAD_GAME);
    this.game = game;
  }

  private ChessGame game;

  public ChessGame getGame() { return game; }
}
