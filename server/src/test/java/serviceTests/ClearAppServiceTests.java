package serviceTests;

import dataAccess.*;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reqAndRes.ClearAppServiceReq;
import reqAndRes.CreateGameReq;
import reqAndRes.RegistrationReq;
import service.ClearAppService;
import service.GameService;
import service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClearAppServiceTests {

  static final UserDAOInterface userInterface= UserDAO.getInstance();
  static final AuthDAOInterface authInterface= AuthDAO.getInstance();
  static final GameDAOInterface gameInterface= GameDAO.getInstance();
  static final ClearAppService service = new ClearAppService();
  static final UserService UService = new UserService();
  static final GameService GService = new GameService();

  @BeforeEach
  void clear() throws DataAccessException {
    service.deleteAllDB(new ClearAppServiceReq());
  }
  @Test
  void deleteAll() throws DataAccessException {
    // add user and token
    var token1 = UService.register(new RegistrationReq("Dakota", "1sC00l4", "Iam@hotmail.com"));
    var token2 = UService.register(new RegistrationReq("Callie", "H0tStuff1", "TurtleDuck@gmail.com"));
    var token3 = UService.register(new RegistrationReq("Anna", "BanANNA77", "IamtheMASTERcommander@gmail.com"));

    // add game
    GService.createGame(new CreateGameReq(token1.authToken(), "game1"));
    GService.createGame(new CreateGameReq(token2.authToken(), "game2"));
    GService.createGame(new CreateGameReq(token3.authToken(), "game3"));

    service.deleteAllDB(new ClearAppServiceReq());
    assertEquals(0, UService.listUsers().size());
    assertEquals(0, UService.listAuthTokens().size());
    assertEquals(0, GService.listAllGames().size());
  }

}
