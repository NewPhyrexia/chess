package dataAccess;

import model.GameData;


public interface GameDAOInterface {

  int createGame(String gameName) throws DataAccessException;

  void updateGame(String playerColor, int gameID, String username) throws DataAccessException;

  GameData getGame(int gameID) throws DataAccessException;

  GameData[]  listGames() throws DataAccessException;
  void deleteGame(int gameID) throws DataAccessException;

  void deleteAllGames()  throws DataAccessException;
}
