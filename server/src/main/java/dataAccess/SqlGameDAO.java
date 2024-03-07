package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class SqlGameDAO {
  private static SqlGameDAO instance;

  public SqlGameDAO() throws DataAccessException {
    configureDatabase();
  }
  public static SqlGameDAO getInstance() throws DataAccessException {
    if (instance == null){
      instance = new SqlGameDAO();
    }
    return instance;
  }
  public int createGame(String gameName) throws DataAccessException {
    var game = new ChessGame();
    game.getBoard().resetBoard();
    int gameID = tempGameID;
    allGames.put(gameID, new GameData(gameID, null, null, gameName, game));
    tempGameID++;
    return gameID;
  }

  public void updateGame(String playerColor, int gameID, String username) throws DataAccessException {
    var gameData = getGame(gameID);
    if (playerColor.equalsIgnoreCase("black")){
      var updatedGameData = new GameData(gameID, gameData.whiteUsername(), username, gameData.gameName(),gameData.implementation());
      allGames.put(gameID, updatedGameData);
    } else if (playerColor.equalsIgnoreCase("white")){
      var updatedGameData = new GameData(gameID, username, gameData.blackUsername(), gameData.gameName(),gameData.implementation());
      allGames.put(gameID, updatedGameData);
    }
  }

  public GameData getGame(int id) throws DataAccessException {
    String username=null;
    var conn = DatabaseManager.getConnection();
    try(var preparedStatement = conn.prepareStatement("SELECT game FROM games WHERE id =?")) {
      preparedStatement.setInt(0, id);
      var rs = preparedStatement.executeQuery();
      if (rs.next()) {
        return readGame(rs);
      }
    } catch (SQLException ex) {
      throw new DataAccessException(ex.toString());
    } // do I need to close a database?
    return null;
  }

  public GameData[] listGames() throws DataAccessException {
    HashMap<Integer, GameData> allGames = new HashMap<>();
    try(var conn = DatabaseManager.getConnection();) {
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
    } // do I need to close a database?
  }

  public void deleteAllGames() throws DataAccessException {
    var conn = DatabaseManager.getConnection();
    try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE auths")) {
      preparedStatement.executeUpdate();
    } catch (SQLException ex) {
      throw new DataAccessException(ex.toString());
    } // do I need to close a database?
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
          PRIMARY KEY (`id`),
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
