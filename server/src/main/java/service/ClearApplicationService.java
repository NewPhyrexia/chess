package service;

import dataAccess.AuthDAOInterface;
import dataAccess.DataAccessException;
import dataAccess.GameDAOInterface;
import dataAccess.UserDAOInterface;

import java.util.Collection;


public class ClearApplicationService {

  private AuthDAOInterface authInterface;
  private GameDAOInterface gameInterface;
  private UserDAOInterface userInterface;

  public void deleteAllDB() throws DataAccessException{
    authInterface.deleteAllAuthTokens();
    gameInterface.deleteAllGames();
    userInterface.deleteAllUsers();
  }
}
