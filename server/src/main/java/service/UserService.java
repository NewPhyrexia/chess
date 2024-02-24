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

  public UserService(UserDAOInterface userInterface) {this.userInterface = userInterface;}

  public AuthData register(UserData user) throws DataAccessException {
    // check if user exists
    if (userInterface.getUser(user.username()) != null) {
      return null;
    }

    userInterface.addUser(user);
    var token = authInterface.createAuthToken(user.username());
    authInterface.addAuthToken(token);
    return token;
  }

  public Collection<UserData> listUsers() throws DataAccessException {
    return userInterface.listUsers();
  }

//  public AuthData login(UserData  user) {}
//  public void logout(UserData user) {}

}
