package serviceTests;

import chess.ChessGame;
import dataAccess.*;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ClearAppService;
import service.GameService;
import service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameServiceTests {
  static final UserDAOInterface userInterface= UserDAO.getInstance();
  static final AuthDAOInterface authInterface= AuthDAO.getInstance();
  static final GameDAOInterface gameInterface= GameDAO.getInstance();
  static final ClearAppService service = new ClearAppService(authInterface, gameInterface, userInterface);
  static final UserService UService = new UserService(userInterface, authInterface);
  static final GameService GService = new GameService(gameInterface);

  @BeforeEach
  void clear() throws DataAccessException {
    service.deleteAllDB();
  }
  @Test
  void listAllGames() throws DataAccessException {
    // add user and token
    var token1 = UService.register(new UserData("Dakota", "1sC00l4", "Iam@hotmail.com"));
    var token2 = UService.register(new UserData("Callie", "H0tStuff1", "TurtleDuck@gmail.com"));
    var token3 = UService.register(new UserData("Anna", "BanANNA77", "IamtheMASTERcommander@gmail.com"));

    // add game
    GService.createGame(token1, new GameData(1000, "Johnny", "Lebron James", "AwesomeGame", new ChessGame()));
    GService.createGame(token2, new GameData(2000, "Steve", "Samuel Jackson", "CoolGame", new ChessGame()));
    GService.createGame(token3, new GameData(3000, "Chad", "Mike Tyson", "BestGame", new ChessGame()));

    assertEquals(3, GService.listGames().size());
  }

  @Test
  void noGamesToList() throws DataAccessException {
    assertEquals(0, GService.listGames().size());
  }
}
