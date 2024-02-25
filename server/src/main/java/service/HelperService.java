package service;

import dataAccess.*;

public class HelperService {

  private AuthDAOInterface authInterface = AuthDAO.getInstance();

  private UserDAOInterface userDAO = getUserInterface();
  private AuthDAOInterface authDAO = getAuthInterface();
  private GameDAOInterface gameDAO = getGameInterface();

  public HelperService(AuthDAOInterface authInterface) {
    this.authInterface = authInterface;
  }

  public boolean AuthTokenCheck(String token) throws DataAccessException {
    return authInterface.getAuthToken(token) != null;
  }

  public AuthDAOInterface getAuthInterface() {
    return AuthDAO.getInstance();
  }

  public UserDAOInterface getUserInterface() {
    return UserDAO.getInstance();
  }

  public GameDAOInterface getGameInterface() {
    return GameDAO.getInstance();
  }

  public UserService getUserService() {
    return new UserService(userDAO, authDAO);
  }

  public GameService getGameService() {
    return new GameService(gameDAO);
  }
}
