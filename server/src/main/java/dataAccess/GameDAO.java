package dataAccess;


import model.GameData;

import java.util.HashMap;

public class GameDAO {
  final private HashMap<Integer, GameData> allGames = new HashMap<>();

  public GameData addGameData(GameData game) {
    game = new GameData(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName(), game.implementation());

    allGames.put(game.gameID(), game);
    return game;
  }

  public void deleteAllAuthTokens() throws DataAccessException {
    allGames.clear();
  }
}
