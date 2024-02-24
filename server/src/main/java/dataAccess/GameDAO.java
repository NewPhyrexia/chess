package dataAccess;


import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class GameDAO implements GameDAOInterface{
  final private HashMap<Integer, GameData> allGames = new HashMap<>();

  public GameData createGame(GameData game) {
    game = new GameData(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName(), game.implementation());

    allGames.put(game.gameID(), game);
    return game;
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
