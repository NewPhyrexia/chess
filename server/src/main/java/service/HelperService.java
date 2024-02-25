package service;

import dataAccess.AuthDAOInterface;
import dataAccess.DataAccessException;

public class HelperService {

  private AuthDAOInterface authInterface;

  public HelperService(AuthDAOInterface authInterface) {
    this.authInterface = authInterface;
  }

  public boolean AuthTokenCheck(String token) throws DataAccessException {
    return authInterface.getAuthToken(token) != null;
  }
}
