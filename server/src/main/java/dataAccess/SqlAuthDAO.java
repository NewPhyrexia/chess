package dataAccess;

import dataAccess.interfaces.AuthDAOInterface;
import model.AuthData;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class SqlAuthDAO implements AuthDAOInterface {
  private static SqlAuthDAO instance;

  public SqlAuthDAO() {
    try {
      configureDatabase();
    } catch (DataAccessException ex) {
      System.out.println("Error: could not configure");
    }
  }
  public static SqlAuthDAO getInstance() {
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

  public AuthData addAuthData(AuthData auth) throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      try (var preparedStatement=conn.prepareStatement("INSERT INTO auths (token, username) VALUES (?, ?)")) {
        preparedStatement.setString(1, auth.authToken());
        preparedStatement.setString(2, auth.username());

        preparedStatement.executeUpdate();
      }
    }catch (SQLException ex) {
      throw new DataAccessException(ex.toString());
    }

    return new AuthData(auth.authToken(),auth.username());
  }
  public AuthData getAuthToken(String token) throws DataAccessException {
    String username=null;
    try (var conn = DatabaseManager.getConnection()) {
      try (var preparedStatement=conn.prepareStatement("SELECT token, username FROM auths WHERE token =?")) {
        preparedStatement.setString(1, token);
        var rs=preparedStatement.executeQuery();
        if (rs.next()) {
          username=rs.getString("username");
          return new AuthData(token, username);
        } else { return null;}
      }
    }catch (SQLException ex) {
      throw new DataAccessException(ex.toString());
    }
  }

  public Collection<AuthData> listAuthTokens() throws DataAccessException {
    HashMap<String, AuthData> allAuthData = new HashMap<>();
    try (var conn = DatabaseManager.getConnection()) {
      try (var preparedStatement=conn.prepareStatement("SELECT token, username FROM auths")) {
        var rs=preparedStatement.executeQuery();
        while (rs.next()) {
          var token=rs.getString("token");
          var username=rs.getString("username");
          var user=new AuthData(token, username);
          allAuthData.put(username, user);
        }
        return allAuthData.values();
      }
    }catch (SQLException ex) {
      throw new DataAccessException(ex.toString());
    }
  }

  public void deleteAuthToken(String token) throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      try (var preparedStatement=conn.prepareStatement("DELETE FROM auths WHERE token = ?")) {
        preparedStatement.setString(1, token);
        preparedStatement.executeUpdate();
      }
    } catch (SQLException ex) {
      throw new DataAccessException(ex.toString());
    }
  }

  public void deleteAllAuthTokens() throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      try (var preparedStatement=conn.prepareStatement("TRUNCATE TABLE auths")) {
        preparedStatement.executeUpdate();
      }
    } catch (SQLException ex) {
      throw new DataAccessException(ex.toString());
    }
  }

  private final String[] createStatements = {
          """
          CREATE TABLE IF NOT EXISTS auths (
          `token` varchar(256) NOT NULL,
          `username` varchar(256) NOT NULL,
          PRIMARY KEY (`token`),
          INDEX(token),
          INDEX(username)
          ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
          """
  };

  private void configureDatabase() throws DataAccessException {
    SqlUserDAO.configureDatabase(createStatements);
  }
}
