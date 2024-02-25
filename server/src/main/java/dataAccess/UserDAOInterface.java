package dataAccess;

import model.UserData;

import java.util.Collection;

public interface UserDAOInterface {
  UserDAO getInstance() throws DataAccessException;

  UserData addUser(UserData user) throws DataAccessException;

  Collection<UserData> listUsers() throws DataAccessException;

  UserData getUser(String username) throws DataAccessException;

  void deleteUser(String username) throws DataAccessException;

  void deleteAllUsers()  throws DataAccessException;
}
