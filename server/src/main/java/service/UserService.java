package service;

import dataAccess.AuthDAOInterface;
import dataAccess.DataAccessException;
import dataAccess.GameDAOInterface;
import dataAccess.UserDAOInterface;
import model.AuthData;
import model.UserData;

import java.util.Collection;

public class UserService {

  private AuthDAOInterface authInterface;
  private UserDAOInterface userInterface;
  private GameDAOInterface gameInterface;

  public UserService(UserDAOInterface userInterface, AuthDAOInterface authInterface) {
    this.userInterface = userInterface;
    this.authInterface = authInterface;
  }

  public String register(UserData user) throws DataAccessException {
    // check if user exists
    if (userInterface.getUser(user.username()) != null) {
      return null;
    }

    userInterface.addUser(user);
    var authData = authInterface.createAuthToken(user.username());
    authInterface.addAuthData(authData);
    return authData.authToken();
  }

  public Collection<UserData> listUsers() throws DataAccessException {
    return userInterface.listUsers();
  }

  public Collection<AuthData> listAuthTokens() throws DataAccessException {
    return authInterface.listAuthTokens();
  }

//  public AuthData login(UserData  user) {}
//  public void logout(UserData user) {}

}
