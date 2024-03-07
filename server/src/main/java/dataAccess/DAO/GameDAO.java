package dataAccess.DAO;


import chess.ChessGame;
import dataAccess.interfaces.GameDAOInterface;
import model.GameData;

import java.util.HashMap;

public class GameDAO implements GameDAOInterface {
  final private HashMap<Integer, GameData> allGames = new HashMap<>();
  private static GameDAO instance;

  public static int tempGameID = 1; // for pre server testing only

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

  public void updateGame(String playerColor, int gameID, String username) {
    var gameData = getGame(gameID);
    if (playerColor.equalsIgnoreCase("black")){
      var updatedGameData = new GameData(gameID, gameData.whiteUsername(), username, gameData.gameName(),gameData.implementation());
      allGames.put(gameID, updatedGameData);
    } else if (playerColor.equalsIgnoreCase("white")){
      var updatedGameData = new GameData(gameID, username, gameData.blackUsername(), gameData.gameName(),gameData.implementation());
      allGames.put(gameID, updatedGameData);
    }
  }

  public GameData getGame(int gameID) {
    return allGames.get(gameID);
  }

  public GameData[] listGames() {
    return allGames.values().toArray(new GameData[0]);
  }

  public void deleteAllGames() {
    allGames.clear();
  }
}
