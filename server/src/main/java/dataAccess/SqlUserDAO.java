package dataAccess;

import com.google.gson.Gson;
import model.UserData;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlUserDAO implements UserDAOInterface{
  public SqlUserDAO() throws DataAccessException {
    configureDatabase();
  }



  private UserData readUser(ResultSet rs) throws SQLException{
    var json = rs.getString("json");
    return new Gson().fromJson(json, UserData.class);
  }

  private final String[] createStatements = {
          """
          CREATE TABLE IF NOT EXISTS userData (
          `id` int NOT NULL AUTO_INCREMENT,
          `username` varchar(256) NOT NULL,
          `password` varchar(256) NOT NULL,
          `email` varchar(256) NOT NULL,
          `json` TEXT DEFAULT NULL,
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
