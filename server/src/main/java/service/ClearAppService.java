package service;

import dataAccess.AuthDAOInterface;
import dataAccess.DataAccessException;
import dataAccess.GameDAOInterface;
import dataAccess.UserDAOInterface;


public class ClearAppService {

  private final AuthDAOInterface authInterface;
  private final GameDAOInterface gameInterface;
  private final UserDAOInterface userInterface;

  public ClearAppService(AuthDAOInterface authInterface, GameDAOInterface gameInterface, UserDAOInterface userInterface) {
    this.authInterface = authInterface;
    this.gameInterface = gameInterface;
    this.userInterface = userInterface;
  }


  public void deleteAllDB() throws DataAccessException{
    userInterface.deleteAllUsers();
    authInterface.deleteAllAuthTokens();
    gameInterface.deleteAllGames();
  }
}
