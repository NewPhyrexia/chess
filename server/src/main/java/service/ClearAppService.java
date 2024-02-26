package service;

import dataAccess.*;


public class ClearAppService {
  static final UserDAOInterface userInterface= UserDAO.getInstance();
  static final AuthDAOInterface authInterface= AuthDAO.getInstance();
  static final GameDAOInterface gameInterface= GameDAO.getInstance();

  public void deleteAllDB() throws DataAccessException{
    userInterface.deleteAllUsers();
    authInterface.deleteAllAuthTokens();
    gameInterface.deleteAllGames();
  }

  // Try, catch, finally?
}
