package service;

import dataAccess.*;
import dataAccess.interfaces.AuthDAOInterface;
import dataAccess.interfaces.GameDAOInterface;
import dataAccess.interfaces.UserDAOInterface;
import req.ClearAppServiceReq;
import res.ClearAppServiceRes;


public class ClearAppService {
  static final UserDAOInterface userInterface= SqlUserDAO.getInstance();
  static final AuthDAOInterface authInterface= SqlAuthDAO.getInstance();
  static final GameDAOInterface gameInterface= SqlGameDAO.getInstance();

  /**
   * Deletes all entries in the User, Game, and Auth databases
   * @param request
   * @return
   * @throws DataAccessException
   */
  public ClearAppServiceRes deleteAllDB(ClearAppServiceReq request) throws DataAccessException{
    try {
      userInterface.deleteAllUsers();
      authInterface.deleteAllAuthTokens();
      gameInterface.deleteAllGames();
    } catch(Exception e) {
      if (e instanceof DataAccessException){
        return new ClearAppServiceRes("Error: DataAccessException.");
      }
    }
    return new ClearAppServiceRes(null);
  }
}
