package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {

  public MakeMoveCommand(String authToken, int gameID, ChessMove move) {
    super(authToken, gameID);
    this.commandType = CommandType.MAKE_MOVE;
    this.move = move;
  }

  private ChessMove move;

  public ChessMove getMove() { return move; }
}
