package service;

import dataAccess.AuthDAOInterface;
import dataAccess.DataAccessException;
import dataAccess.UserDAOInterface;
import model.AuthData;
import model.UserData;
import reqAndRes.RegistrationRes;

import java.util.Collection;

public class UserService {

  private final AuthDAOInterface authInterface;
  private final UserDAOInterface userInterface;

  public UserService(UserDAOInterface userInterface, AuthDAOInterface authInterface) {
    this.userInterface = userInterface;
    this.authInterface = authInterface;
  }

  public RegistrationRes register(UserData user) throws DataAccessException {
    // check if user exists
    if (userInterface.getUser(user.username()) != null) {
      return null;
    }

    userInterface.addUser(user);
    var authData = authInterface.createAuthToken(user.username());
    authInterface.addAuthData(authData);
    return new RegistrationRes(authData.authToken(), user.username(), null);
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

  public void logout(AuthData authToken) throws DataAccessException{
    authInterface.deleteAuthToken(authToken.authToken());
  }

}
