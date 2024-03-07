package dataAccess;

import com.google.gson.Gson;
import model.UserData;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class SqlUserDAO implements UserDAOInterface{

  private static SqlUserDAO instance;
  public SqlUserDAO() throws DataAccessException {
    configureDatabase();
  }

  public static SqlUserDAO getInstance() throws DataAccessException {
    if (instance == null){
      instance = new SqlUserDAO();
    }
    return instance;
  }

  public UserData addUser(UserData user) throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      try (var preparedStatement=conn.prepareStatement("INSERT INTO users (username, password, email) VALUES (?, ?, ?)", RETURN_GENERATED_KEYS)) {
        preparedStatement.setString(1, user.username());
        preparedStatement.setString(2, user.password());
        preparedStatement.setString(3, user.email());

        preparedStatement.executeUpdate();
      }
    } catch (SQLException ex) {
      throw new DataAccessException(ex.toString());
    }

    return new UserData(user.username(), user.password(), user.email());
  }

  public Collection<UserData> listUsers() throws DataAccessException {
    HashMap<String, UserData> allUserData = new HashMap<>();
    try (var conn = DatabaseManager.getConnection()) {
      try (var preparedStatement=conn.prepareStatement("SELECT username, password, email FROM users")) {
        var rs=preparedStatement.executeQuery();
        while (rs.next()) {
          var username=rs.getString("username");
          var password=rs.getString("password");
          var email=rs.getString("email");
          var user=new UserData(username, password, email);
          allUserData.put(username, user);
        }
        return allUserData.values();
      }
    } catch (SQLException ex) {
      throw new DataAccessException(ex.toString());
    }
  }

  public UserData getUser(String username) throws DataAccessException{
    String password=null;
    String email=null;
    try (var conn = DatabaseManager.getConnection()) {
      try (var preparedStatement=conn.prepareStatement("SELECT username, password, email FROM users WHERE username =?")) {
        preparedStatement.setString(1, username);
        var rs=preparedStatement.executeQuery();
        if (rs.next()) {
          password=rs.getString("password");
          email=rs.getString("email");
        }
        return new UserData(username, password, email);
      }
    } catch (SQLException ex) {
      throw new DataAccessException(ex.toString());
    }
  }

  public void deleteAllUsers() throws DataAccessException{
    try (var conn = DatabaseManager.getConnection()) {
      try (var preparedStatement=conn.prepareStatement("TRUNCATE TABLE users")) {
        preparedStatement.executeUpdate();
      }
    } catch (SQLException ex) {
      throw new DataAccessException(ex.toString());
    }
  }

  private final String[] createStatements = {
          """
          CREATE TABLE IF NOT EXISTS users (
          `username` varchar(256) NOT NULL,
          `password` varchar(256) NOT NULL,
          `email` varchar(256) NOT NULL,
          PRIMARY KEY (`username`),
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
