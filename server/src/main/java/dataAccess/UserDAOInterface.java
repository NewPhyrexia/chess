package dataAccess;

import model.RegistrationReq;

import java.util.Collection;

public interface UserDAOInterface {
  RegistrationReq addUser(RegistrationReq user) throws DataAccessException;

  Collection<RegistrationReq> listUsers() throws DataAccessException;

  RegistrationReq getUser(String username) throws DataAccessException;

  String getPassword(String username) throws DataAccessException;

  void deleteUser(String username) throws DataAccessException;

  void deleteAllUsers()  throws DataAccessException;
}
