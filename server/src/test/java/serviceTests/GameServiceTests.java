package serviceTests;

import dataAccess.*;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reqAndRes.*;
import service.ClearAppService;
import service.GameService;
import service.UserService;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTests {
  static final ClearAppService service = new ClearAppService();
  static final UserService UService = new UserService();
  static final GameService GService = new GameService();

  @BeforeEach
  void clear() throws DataAccessException {
    service.deleteAllDB(new ClearAppServiceReq());
    GameDAO.tempGameID = 1; // for pre server testing only
  }

  @Test
  void successfulGameCreated() throws  DataAccessException {
    var token1 = UService.register(new RegistrationReq("Dakota", "1sC00l4", "Iam@hotmail.com"));

    GService.createGame(new CreateGameReq(token1.authToken(), "game1"));
//    assertEquals(1, GService.listGames(new ListGamesReq(token1.authToken())).listOfGames().size());
  }

  @Test
  void nonUserCantCreateGame() throws DataAccessException {
    String token = "token";
    GService.createGame(new CreateGameReq(token, "game1"));

    var res = GService.listGames(new ListGamesReq(token));
//    assertNull(res.listOfGames());
  }

  @Test
  void listAllGames() throws DataAccessException {
    // add user and token
    var token1 = UService.register(new RegistrationReq("Dakota", "1sC00l4", "Iam@hotmail.com"));
    var token2 = UService.register(new RegistrationReq("Callie", "H0tStuff1", "TurtleDuck@gmail.com"));
    var token3 = UService.register(new RegistrationReq("Anna", "BanANNA77", "IamtheMASTERcommander@gmail.com"));

    // add game
    GService.createGame(new CreateGameReq(token1.authToken(), "game1"));
    GService.createGame(new CreateGameReq(token2.authToken(), "game2"));
    GService.createGame(new CreateGameReq(token3.authToken(), "game3"));

//    assertEquals(3, GService.listGames(new ListGamesReq(token1.authToken())).listOfGames().size());
  }

  @Test
  void noGamesToList() throws DataAccessException {
    var token1 = UService.register(new RegistrationReq("Dakota", "1sC00l4", "Iam@hotmail.com"));

//    assertEquals(0, GService.listGames(new ListGamesReq(token1.authToken())).listOfGames().size());
  }

  @Test
  void joinGameSuccess() throws DataAccessException{
    var token1 = UService.register(new RegistrationReq("Dakota", "1sC00l4", "Iam@hotmail.com"));
    var createGameRes = GService.createGame(new CreateGameReq(token1.authToken(), "game1"));
    GService.joinGame(new JoinGameReq(token1.authToken(),"WHITE", createGameRes.gameID()));

    assertEquals(token1.username(), GameDAO.getInstance().getGame(createGameRes.gameID()).whiteUsername());
  }

  @Test
  void joinGameFailure() throws DataAccessException {
    var token1 = UService.register(new RegistrationReq("Dakota", "1sC00l4", "Iam@hotmail.com"));
    var createGameRes = GService.createGame(new CreateGameReq(token1.authToken(), "game1"));
    GService.joinGame(new JoinGameReq("invalid","WHITE", createGameRes.gameID()));

    assertNotEquals(token1.username(), GameDAO.getInstance().getGame(createGameRes.gameID()).whiteUsername());
  }
}
