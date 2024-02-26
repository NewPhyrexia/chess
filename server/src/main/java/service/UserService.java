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

  public String login(UserData  user) throws DataAccessException {
    // user not in system
    if (userInterface.getUser(user.username()) == null) {
      return null;
    }
    // provided password does not match saved password
    if (!user.password().equals(userInterface.getUser(user.username()).password())) {
      return null;
    }
      // create new authToken
      var token = authInterface.createAuthToken(user.username());
      authInterface.addAuthData(token);

      return token.authToken();
  }

  public void logout(String token) throws DataAccessException{
    authInterface.deleteAuthToken(token);
  }

}
