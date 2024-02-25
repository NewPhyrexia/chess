package service;

import dataAccess.AuthDAO;
import dataAccess.AuthDAOInterface;
import dataAccess.DataAccessException;

public class HelperService {

  private AuthDAOInterface authInterface = AuthDAO.getInstance();

  private AuthDAO authDAO = AuthDAO.getInstance();

  public HelperService(AuthDAOInterface authInterface) {
    this.authInterface = authInterface;
  }

  public boolean AuthTokenCheck(String token) throws DataAccessException {
    return authInterface.getAuthToken(token) != null;
  }
}
