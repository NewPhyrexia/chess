package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.sql.SQLException;
import java.util.HashMap;

public class SqlGameDAO {
  private static SqlGameDAO instance;


  public static SqlGameDAO getInstance() {
    if (instance == null){
      instance = new SqlGameDAO();
    }
    return instance;
  }
  public int createGame(String gameName) {
    var game = new ChessGame();
    game.getBoard().resetBoard();
    int gameID = tempGameID;
    allGames.put(gameID, new GameData(gameID, null, null, gameName, game));
    tempGameID++;
    return gameID;
  }

  public void updateGame(String playerColor, int gameID, String username) {
    var gameData = getGame(gameID);
    if (playerColor.equalsIgnoreCase("black")){
      var updatedGameData = new GameData(gameID, gameData.whiteUsername(), username, gameData.gameName(),gameData.implementation());
      allGames.put(gameID, updatedGameData);
    } else if (playerColor.equalsIgnoreCase("white")){
      var updatedGameData = new GameData(gameID, username, gameData.blackUsername(), gameData.gameName(),gameData.implementation());
      allGames.put(gameID, updatedGameData);
    }
  }

  public GameData getGame(int gameID) {
    return allGames.get(gameID);
  }

  public GameData[] listGames() {
    return allGames.values().toArray(new GameData[0]);
  }

  public void deleteAllGames() {
    allGames.clear();
  }

  private final String[] createStatements = { // NEED TO FINISH THIS
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
