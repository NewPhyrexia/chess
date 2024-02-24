package service;

import dataAccess.AuthDAOInterface;
import dataAccess.DataAccessException;
import dataAccess.GameDAOInterface;
import dataAccess.UserDAOInterface;


public class ClearAppService {

  private AuthDAOInterface authInterface;
  private GameDAOInterface gameInterface;
  private UserDAOInterface userInterface;

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
