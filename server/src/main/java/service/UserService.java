package service;

import dataAccess.AuthDAOInterface;
import dataAccess.GameDAOInterface;
import dataAccess.UserDAOInterface;
import model.AuthData;
import model.UserData;

public class UserService {

  private AuthDAOInterface authInterface;
  private UserDAOInterface userInterface;
  private GameDAOInterface gameInterface;

  public UserService(UserDAOInterface userInterface) {this.userInterface = userInterface;}

//  public AuthData register(UserData user) {}
//  public AuthData login(UserData  user) {}
//  public void logout(UserData user) {}

}
