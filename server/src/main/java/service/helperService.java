package service;

import dataAccess.*;

public class helperService {


  private AuthDAOInterface authInterface;

  public helperService(AuthDAOInterface authInterface) {
    this.authInterface = authInterface;
  }

  public boolean AuthTokenCheck(String token) throws DataAccessException {
    return authInterface.getAuthToken(token) != null;
  }
}
