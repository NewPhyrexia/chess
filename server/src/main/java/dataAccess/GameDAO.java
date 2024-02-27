package dataAccess;


import chess.ChessGame;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class GameDAO implements GameDAOInterface{
  final private HashMap<Integer, GameData> allGames = new HashMap<>();
  private static GameDAO instance;

  public static int tempGameID = 0; // for pre server testing only

  public static GameDAO getInstance() {
    if (instance == null){
      instance = new GameDAO();
    }
    return instance;
  }
  public int createGame(String gameName) {
    var game = new ChessGame();
    game.getBoard().resetBoard();
    int gameID = tempGameID;
    allGames.put(gameID, new GameData(gameID, null, null, gameName, game));
    tempGameID++;
    return gameID;
  }

  public GameData getGame(int gameID) {
    return allGames.get(gameID);
  }

  public Collection<GameData> listGames() {
    return allGames.values();
  }

  public void deleteGame(int gameID) {
    allGames.remove(gameID);
  }
  public void deleteAllGames() {
    allGames.clear();
  }
}
