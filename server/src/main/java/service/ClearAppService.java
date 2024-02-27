package service;

import dataAccess.*;
import req.ClearAppServiceReq;
import res.ClearAppServiceRes;


public class ClearAppService {
  static final UserDAOInterface userInterface= UserDAO.getInstance();
  static final AuthDAOInterface authInterface= AuthDAO.getInstance();
  static final GameDAOInterface gameInterface= GameDAO.getInstance();

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
