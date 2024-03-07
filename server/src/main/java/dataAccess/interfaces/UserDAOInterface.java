package dataAccess.interfaces;

import dataAccess.DataAccessException;
import model.UserData;

import java.util.Collection;

public interface UserDAOInterface {
  UserData addUser(UserData user) throws DataAccessException;

  Collection<UserData> listUsers() throws DataAccessException;

  UserData getUser(String username) throws DataAccessException;

  void deleteAllUsers()  throws DataAccessException;
}
