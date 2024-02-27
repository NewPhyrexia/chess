package serviceTests;

import chess.ChessGame;
import dataAccess.*;
import model.GameData;
import model.RegistrationReq;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reqAndRes.ClearAppServiceReq;
import reqAndRes.ListGamesReq;
import service.ClearAppService;
import service.GameService;
import service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class GameServiceTests {
  static final UserDAOInterface userInterface= UserDAO.getInstance();
  static final AuthDAOInterface authInterface= AuthDAO.getInstance();
  static final GameDAOInterface gameInterface= GameDAO.getInstance();
  static final ClearAppService service = new ClearAppService();
  static final UserService UService = new UserService(userInterface, authInterface);
  static final GameService GService = new GameService(gameInterface);

  @BeforeEach
  void clear() throws DataAccessException {
    service.deleteAllDB(new ClearAppServiceReq());
  }

  @Test
  void successfulGameCreated() throws  DataAccessException {
    var token1 = UService.register(new RegistrationReq("Dakota", "1sC00l4", "Iam@hotmail.com"));

    GService.createGame(token1.authToken(), new GameData(1000, "Johnny", "Lebron James", "AwesomeGame", new ChessGame()));
    assertEquals(1, GService.listGames(new ListGamesReq(token1.authToken())).listOfGames().size());
  }

  @Test
  void nonUserCantCreateGame() throws DataAccessException {
    String token = "token";
    GService.createGame(token, new GameData(1000, "Johnny", "Lebron James", "AwesomeGame", new ChessGame()));

    var res = GService.listGames(new ListGamesReq(token));
    assertNull(res.listOfGames());
  }

  @Test
  void listAllGames() throws DataAccessException {
    // add user and token
    var token1 = UService.register(new RegistrationReq("Dakota", "1sC00l4", "Iam@hotmail.com"));
    var token2 = UService.register(new RegistrationReq("Callie", "H0tStuff1", "TurtleDuck@gmail.com"));
    var token3 = UService.register(new RegistrationReq("Anna", "BanANNA77", "IamtheMASTERcommander@gmail.com"));

    // add game
    GService.createGame(token1.authToken(), new GameData(1000, "Johnny", "Lebron James", "AwesomeGame", new ChessGame()));
    GService.createGame(token2.authToken(), new GameData(2000, "Steve", "Samuel Jackson", "CoolGame", new ChessGame()));
    GService.createGame(token3.authToken(), new GameData(3000, "Chad", "Mike Tyson", "BestGame", new ChessGame()));

    assertEquals(3, GService.listGames(new ListGamesReq(token1.authToken())).listOfGames().size());
  }

  @Test
  void noGamesToList() throws DataAccessException {
    var token1 = UService.register(new RegistrationReq("Dakota", "1sC00l4", "Iam@hotmail.com"));
    assertEquals(0, GService.listGames(new ListGamesReq(token1.authToken())).listOfGames().size());
  }
}
