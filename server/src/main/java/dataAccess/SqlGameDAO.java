package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.interfaces.GameDAOInterface;
import model.GameData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class SqlGameDAO implements GameDAOInterface {
  private static SqlGameDAO instance;

  public SqlGameDAO() {
    try {
      configureDatabase();
    } catch (DataAccessException ex) {
      System.out.println("Error: could not configure");
    }
  }
  public static SqlGameDAO getInstance() {
    if (instance == null){
      instance = new SqlGameDAO();
    }
    return instance;
  }
  public int createGame(String gameName) throws DataAccessException {
    var newGame = new ChessGame();
    newGame.getBoard().resetBoard();
    try(var conn = DatabaseManager.getConnection()) {
      try (var preparedStatement=conn.prepareStatement("INSERT INTO games (game) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
        var game = new Gson().toJson(new GameData(0, null, null, gameName, newGame));
        preparedStatement.setString(1, game);
        preparedStatement.executeUpdate();

        // returns id
        var rs = preparedStatement.getGeneratedKeys();
        if (rs.next()) {
          return rs.getInt(1);
        }
      }
    } catch (SQLException ex) {
      throw new DataAccessException(ex.toString());
    }
    return 0;
  }

  public void updateGame(String playerColor, int id, String username, ChessGame game) throws DataAccessException {
    var gameData = getGame(id);
    try(var conn = DatabaseManager.getConnection()) {
      try (var preparedStatement=conn.prepareStatement("UPDATE games SET game = ? WHERE id = ?")) {
        preparedStatement.setInt(2, id);
        if (playerColor.equalsIgnoreCase("black")) {
          var updatedGameData=new GameData(id, gameData.whiteUsername(), username, gameData.gameName(), game);
          var jsonGameData=new Gson().toJson(updatedGameData);
          preparedStatement.setString(1, jsonGameData);
        } else if (playerColor.equalsIgnoreCase("white")) {
          var updatedGameData=new GameData(id, username, gameData.blackUsername(), gameData.gameName(), gameData.game());
          var jsonGameData =new Gson().toJson(updatedGameData);
          preparedStatement.setString(1, jsonGameData);
        }
        preparedStatement.executeUpdate();
      }
    } catch (SQLException ex) {
      throw new DataAccessException(ex.toString());
    }
  }

  public GameData getGame(int id) throws DataAccessException {  // PROBABLY NEEDS RETURN GENERATED KEYS?
    try (var conn = DatabaseManager.getConnection()) {
      try (var preparedStatement=conn.prepareStatement("SELECT game FROM games WHERE id =?")) {
        preparedStatement.setInt(1, id);
        var rs=preparedStatement.executeQuery();
        if (rs.next()) {
          return readGame(rs);
        }
      }

    } catch (SQLException ex) {
      throw new DataAccessException(ex.toString());
    }
    return null;
  }

  public GameData[] listGames() throws DataAccessException {
    HashMap<Integer, GameData> allGames = new HashMap<>();
    try(var conn = DatabaseManager.getConnection()) {
      var statement = "SELECT id, game FROM games";
      try (var ps = conn.prepareStatement(statement)) {
        try (var rs = ps.executeQuery()) {
          while (rs.next()) {
            var id = rs.getInt("id");
            allGames.put(id, readGame(rs));
          }
        }
      }
      return allGames.values().toArray(new GameData[0]);
    } catch (SQLException ex) {
      throw new DataAccessException(ex.toString());
    }
  }

  public void deleteAllGames() throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      try (var preparedStatement=conn.prepareStatement("TRUNCATE TABLE games")) {
        preparedStatement.executeUpdate();
      }
    } catch (SQLException ex) {
      throw new DataAccessException(ex.toString());
    }
  }

  private GameData readGame(ResultSet rs) throws SQLException{
    var json = rs.getString("game");
    return new Gson().fromJson(json, GameData.class);
  }

  private final String[] createStatements = {
          """
          CREATE TABLE IF NOT EXISTS games (
          `id` int NOT NULL AUTO_INCREMENT,
          `game` TEXT DEFAULT NULL,
          PRIMARY KEY (`id`)
          ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
          """
  };

  private void configureDatabase() throws DataAccessException {
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
