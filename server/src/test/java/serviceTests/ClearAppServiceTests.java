package serviceTests;

import chess.ChessGame;
import dataAccess.*;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Test;
import service.ClearAppService;
import service.GameService;
import service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClearAppServiceTests {

  static final UserDAO userDAO = new UserDAO();
  static final AuthDAO authDAO = new AuthDAO();
  static final GameDAO gameDAO = new GameDAO();
  static final ClearAppService service = new ClearAppService(authDAO, gameDAO, userDAO);
  static final UserService UService = new UserService(userDAO, authDAO);

  static final GameService GService = new GameService(gameDAO);

  // add services for other 2 services
  @Test
  void deleteAll() throws DataAccessException {
    // add user and token
    UService.register(new UserData("Dakota", "1sC00l4", "Iam@hotmail.com"));
    UService.register(new UserData("Callie", "H0tStuff1", "TurtleDuck@gmail.com"));
    UService.register(new UserData("Anna", "BanANNA77", "IamtheMASTERcommander@gmail.com"));

    // add game
    GService.createGame("Token1", new GameData(1000, "Johnny", "Lebron James", "AwesomeGame", new ChessGame()));
    GService.createGame("Token1", new GameData(2000, "Steve", "Samuel Jackson", "CoolGame", new ChessGame()));
    GService.createGame("Token1", new GameData(3000, "Chad", "Mike Tyson", "BestGame", new ChessGame()));

    service.deleteAllDB();
    assertEquals(0, UService.listUsers().size()); // users
    assertEquals(0, UService.listAuthTokens().size()); // tokens
    assertEquals(0, GService.listGames().size()); // games
  }

}
