package dataAccess;

import model.AuthData;
import model.UserData;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

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

  public AuthData addAuthData(AuthData auth) throws DataAccessException {
    var conn = DatabaseManager.getConnection();
    try (var preparedStatement = conn.prepareStatement("INSERT INTO auths (token, username) VALUES (?, ?)", RETURN_GENERATED_KEYS)) {
      preparedStatement.setString(1, auth.authToken());
      preparedStatement.setString(2, auth.username());

      preparedStatement.executeUpdate();
    } catch (SQLException ex) {
      throw new DataAccessException(ex.toString());
    } // do I need to close a database?

    return new AuthData(auth.authToken(),auth.username());
  }
  public AuthData getAuthToken(String token) throws DataAccessException {
    String username=null;
    var conn = DatabaseManager.getConnection();
    try(var preparedStatement = conn.prepareStatement("SELECT token, username FROM auths WHERE token =?")) {
      preparedStatement.setString(1, token);
      var rs = preparedStatement.executeQuery();
      if (rs.next()) {
        username = rs.getString("username");
      }
      return new AuthData(token, username);
    } catch (SQLException ex) {
      throw new DataAccessException(ex.toString());
    } // do I need to close a database?
  }

  public Collection<AuthData> listAuthTokens() throws DataAccessException {
    HashMap<String, AuthData> allAuthData = new HashMap<>();
    var conn = DatabaseManager.getConnection();
    try(var preparedStatement = conn.prepareStatement("SELECT token, username FROM auths")) {
      var rs = preparedStatement.executeQuery();
      while ( rs.next()) {
        var token = rs.getString("token");
        var username = rs.getString("username");
        var user =  new AuthData(token, username);
        allAuthData.put(username, user);
      }
      return allAuthData.values();
    } catch (SQLException ex) {
      throw new DataAccessException(ex.toString());
    } // do I need to close a database?
  }

  public void deleteAuthToken(String token) throws DataAccessException {
    allAuthTokens.remove(token);
  }

  public void deleteAllAuthTokens() throws DataAccessException {
    var conn = DatabaseManager.getConnection();
    try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE auths")) {
      preparedStatement.executeUpdate();
    } catch (SQLException ex) {
      throw new DataAccessException(ex.toString());
    } // do I need to close a database?
  }

  private final String[] createStatements = {
          """
          CREATE TABLE IF NOT EXISTS auths (
          `id` int NOT NULL AUTO_INCREMENT,
          `token` varchar(256) NOT NULL,
          `username` varchar(256) NOT NULL,
          PRIMARY KEY (`id`),
          INDEX(token),
          INDEX(username),
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
