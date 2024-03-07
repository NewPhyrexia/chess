package dataAccess;

import model.AuthData;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class SqlAuthDAO {
  private static SqlAuthDAO instance;

  public SqlAuthDAO() throws DataAccessException {
    configureDatabase();
  }
  public static SqlAuthDAO getInstance() throws DataAccessException {
    if (instance == null){
      instance = new SqlAuthDAO();
    }
    return instance;
  }

  public AuthData createAuthToken(String username) throws DataAccessException {
    if(username == null || username.isEmpty()){
      return null;
    }

    String token = UUID.randomUUID().toString();
    return new AuthData(token, username);
  }

  public AuthData addAuthData(AuthData data) throws DataAccessException {
    data = new AuthData(data.authToken(), data.username());

    allAuthTokens.put(data.authToken(), data);
    return data;
  }
  public AuthData getAuthToken(String token) throws DataAccessException {
    return allAuthTokens.get(token);
  }

  public Collection<AuthData> listAuthTokens() throws DataAccessException {
    return allAuthTokens.values();
  }

  public void deleteAuthToken(String token) throws DataAccessException {
    allAuthTokens.remove(token);
  }

  public void deleteAllAuthTokens() throws DataAccessException {
    allAuthTokens.clear();
  }

  private final String[] createStatements = {  // NEEDS TO BE CHANGED FOR THIS CLASS
          """
          CREATE TABLE IF NOT EXISTS users (
          `id` int NOT NULL AUTO_INCREMENT,
          `username` varchar(256) NOT NULL,
          `password` varchar(256) NOT NULL,
          `email` varchar(256) NOT NULL,
          PRIMARY KEY (`id`),
          INDEX(username),
          INDEX(password),
          INDEX(email)
          ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
          """
  };

  private void configureDatabase() throws DataAccessException{
    DatabaseManager.createDatabase();
    try (var conn = DatabaseManager.getConnection()) {
      for (var statement : createStatements) {
        try (var preparedStatement = conn.prepareStatement(statement)) {
          preparedStatement.executeUpdate();
        }
      }
    } catch (SQLException ex) {
      throw new DataAccessException(String.format("Unable to configure database %s", ex.getMessage()));
    }
  }
}
