package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.SqlGameDAO;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SqlGameDAOTests {

  static final SqlGameDAO gameDAO = new SqlGameDAO();

  @BeforeEach
  void clear() throws DataAccessException {
    gameDAO.deleteAllGames();
  }

  @Test
  void createGame() throws DataAccessException {
    var id = gameDAO.createGame("testGame");
    assertEquals(1,id);
  }

  @Test
  void failCreateGame() throws DataAccessException {
    var id = gameDAO.createGame("testGame");
    assertNotEquals(2,id);
  }

  @Test
  void updateGame() throws DataAccessException{
    var id = gameDAO.createGame("testGame");
    gameDAO.updateGame("white", id, "testUsername", gameDAO.getGame(id).game());
    var game = gameDAO.getGame(id);
    assertEquals("testUsername", game.whiteUsername());
  }

  @Test
  void failUpdateGame() throws DataAccessException{
    var id = gameDAO.createGame("testGame");
    gameDAO.updateGame("white", id, "testUsername", gameDAO.getGame(id).game());
    var game = gameDAO.getGame(id);
    assertNotEquals("negTestUsername", game.whiteUsername());
  }

  @Test
  void getGame() throws DataAccessException{
    var id = gameDAO.createGame("testGame");
    gameDAO.updateGame("white", id, "testUsername", gameDAO.getGame(id).game());
    assertEquals("testGame", gameDAO.getGame(id).gameName());
  }

  @Test
  void failGetGame() throws DataAccessException{
    var id = gameDAO.createGame("testGame");
    gameDAO.updateGame("white", id, "testUsername", gameDAO.getGame(id).game());
    assertNotEquals("negTestGame", gameDAO.getGame(id).gameName());
  }

  @Test
  void listGames() throws DataAccessException{
    gameDAO.createGame("testGame");
    gameDAO.createGame("testGame1");
    var games = gameDAO.listGames();

    assertEquals(2, games.length);
  }

  @Test
  void failListGames() throws DataAccessException{
    gameDAO.createGame("testGame");
    var games = gameDAO.listGames();

    assertNotEquals(2, games.length);
  }

  @Test
  void deleteAllGames() throws DataAccessException{
    gameDAO.createGame("testGame");
    gameDAO.createGame("testGame1");

    gameDAO.deleteAllGames();
    var games = gameDAO.listGames();

    assertEquals(0, games.length);
  }
}
