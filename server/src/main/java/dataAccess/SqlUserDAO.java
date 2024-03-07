package dataAccess;

import com.google.gson.Gson;
import model.UserData;


import java.sql.ResultSet;
import java.sql.SQLException;

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
    var conn = DatabaseManager.getConnection();
    try (var preparedStatement = conn.prepareStatement("INSERT INTO user (username, password, email) VALUES (?, ?, ?)", RETURN_GENERATED_KEYS)) {
      preparedStatement.setString(1, user.username());
      preparedStatement.setString(2, user.password());
      preparedStatement.setString(3, user.email());

      preparedStatement.executeUpdate();
    } catch (SQLException ex) {
      throw new DataAccessException(ex.toString());
    }

    return new UserData(user.username(), user.password(), user.email());
  }

  private UserData readUser(ResultSet rs) throws SQLException{
    var json = rs.getString("json");
    return new Gson().fromJson(json, UserData.class);
  }

  private final String[] createStatements = {
          """
          CREATE TABLE IF NOT EXISTS user (
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
      throw new DataAccessException(500, String.format("Unable to configure database %s", ex.getMessage()));
    }
  }
}
