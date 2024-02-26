package service;

import dataAccess.*;
import model.GameData;

import java.util.Collection;

public class GameService {

  private final AuthDAOInterface authInterface = AuthDAO.getInstance();
  private GameDAOInterface gameInterface = GameDAO.getInstance();
  private final HelperService helperService = new HelperService(authInterface);

  public GameService(GameDAOInterface gameInterface) {this.gameInterface = gameInterface;}
  public int createGame(String token, GameData gameData) throws DataAccessException {
    if (helperService.AuthTokenCheck(token)) {
      var game = gameInterface.createGame(gameData);
      return game.gameID();
    } else return 0;
  }

  public Collection<GameData> listAllGames(String token) throws DataAccessException {
    if (!helperService.AuthTokenCheck(token)) {
      return null;
    } else return gameInterface.listGames();
  }

  public Collection<GameData> listGames() throws DataAccessException { // specific method for testing clearApp
    return gameInterface.listGames();
  }


  public record joinGame(String playerColor, int gameID) {

  }
}
