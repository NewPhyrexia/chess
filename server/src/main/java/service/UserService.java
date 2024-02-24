package service;

import dataAccess.AuthDAOInterface;
import dataAccess.DataAccessException;
import dataAccess.GameDAOInterface;
import dataAccess.UserDAOInterface;
import model.AuthData;
import model.UserData;

public class UserService {

  private AuthDAOInterface authInterface;
  private UserDAOInterface userInterface;
  private GameDAOInterface gameInterface;

  public UserService(UserDAOInterface userInterface) {this.userInterface = userInterface;}

  public AuthData register(UserData user) throws DataAccessException {
    // check if user exists
    if (userInterface.getUser(user.username()) == null) {
      return null;
    }

    userInterface.addUser(user);
//    authInterface.

  }
//  public AuthData login(UserData  user) {}
//  public void logout(UserData user) {}

}
