package service;

import dataAccess.AuthDAOInterface;
import dataAccess.DataAccessException;
import dataAccess.GameDAOInterface;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Collection;

public class GameService {

  private AuthDAOInterface authInterface;
  private GameDAOInterface gameInterface;
  private HelperService helperService = new HelperService(authInterface);

  public GameService(GameDAOInterface gameInterface) {this.gameInterface = gameInterface;}
  public int createGame(String token, GameData gameData) throws DataAccessException {
    if (helperService.AuthTokenCheck(token)) {
      var game=gameInterface.createGame(gameData);
      return game.gameID();
    } else return 0;
  }

  public Collection<GameData> listGames() throws DataAccessException {
    return gameInterface.listGames();
  }
//  public AuthData joinGame(GameData game) {}
//  public AuthData listGames(GameData game) {}
}
