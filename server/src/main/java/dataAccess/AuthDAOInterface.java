package dataAccess;

import model.AuthData;

import java.util.Collection;

public interface AuthDAOInterface {

  AuthData createAuthToken(String username) throws DataAccessException;

  AuthData addAuthToken(AuthData token) throws DataAccessException;

  Collection<AuthData>  listAuthTokens()  throws DataAccessException;

  AuthData getAuthToken(String token) throws DataAccessException;

  void deleteAuthToken(String token) throws DataAccessException;

  void deleteAllAuthTokens() throws DataAccessException;
}
