package dataAccess;

import model.GameData;

import java.util.Collection;

public interface GameDAOInterface {

//  GameDAO getInstance() throws DataAccessException;
  GameData createGame(GameData game) throws DataAccessException;

  GameData getGame(int gameID) throws DataAccessException;

  Collection<GameData>  listGames() throws DataAccessException;
  void deleteGame(int gameID) throws DataAccessException;

  void deleteAllGames()  throws DataAccessException;
}
