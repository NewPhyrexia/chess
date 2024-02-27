package dataAccess;


import chess.ChessGame;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class GameDAO implements GameDAOInterface{
  final private HashMap<Integer, ChessGame> allGames = new HashMap<>();
  private static GameDAO instance;

  private static int gameID = 0;

  public static GameDAO getInstance() {
    if (instance == null){
      instance = new GameDAO();
    }
    return instance;
  }
  public int createGame(String gameName) {
    var game = new ChessGame();
    game.getBoard().resetBoard();

    allGames.put(gameID, game);
    gameID++;
    return gameID;
  }

  public ChessGame getGame(int gameID) {
    return allGames.get(gameID);
  }

  public Collection<ChessGame> listGames() {
    return allGames.values();
  }

  public void deleteGame(int gameID) {
    allGames.remove(gameID);
  }
  public void deleteAllGames() {
    allGames.clear();
  }
}
