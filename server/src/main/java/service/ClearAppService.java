package service;

import dataAccess.*;
import reqAndRes.ClearAppServiceReq;
import reqAndRes.ClearAppServiceRes;


public class ClearAppService {
  static final UserDAOInterface userInterface= UserDAO.getInstance();
  static final AuthDAOInterface authInterface= AuthDAO.getInstance();
  static final GameDAOInterface gameInterface= GameDAO.getInstance();

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
