package service;

import dataAccess.DataAccessException;
import dataAccess.GameDAOInterface;
import model.AuthData;
import model.GameData;
import model.UserData;

public class GameService {

  private GameDAOInterface gameInterface;

  public GameService(GameDAOInterface gameInterface) {this.gameInterface = gameInterface;}
  public int createGame(GameData gameData) throws DataAccessException {
    var game = gameInterface.createGame(gameData);
    return game.gameID();
  }
//  public AuthData joinGame(GameData game) {}
//  public AuthData listGames(GameData game) {}
}
