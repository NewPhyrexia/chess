package service;

import dataAccess.*;
import dataAccess.interfaces.AuthDAOInterface;

public class HelperService {


  private AuthDAOInterface authInterface;

  public HelperService(AuthDAOInterface authInterface) {
    this.authInterface = authInterface;
  }

  public boolean authTokenCheck(String token) throws DataAccessException {
    return authInterface.getAuthToken(token) != null;
  }
}
