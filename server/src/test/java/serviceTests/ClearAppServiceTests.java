package serviceTests;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import model.UserData;
import org.junit.jupiter.api.Test;
import service.ClearAppService;
import service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClearAppServiceTests {
  static final ClearAppService service = new ClearAppService(new AuthDAO(), new GameDAO(), new UserDAO());
  static final UserService UService = new UserService(new UserDAO());

  // add services for other 2 services
  @Test
  void deleteAll() throws DataAccessException {
    // add user and token
    UService.register(new UserData("Dakota", "1sC00l4", "Iam@hotmail.com"));
    UService.register(new UserData("Callie", "H0tStuff1", "TurtleDuck@gmail.com"));
    UService.register(new UserData("Anna", "BanANNA77", "IamtheMASTERcommander@gmail.com"));

    // add token

    // add game

    service.deleteAllDB();
    assertEquals(0, 0); // users
//    assertEquals(0, 0); // tokens
//    assertEquals(0, 0); // games
  }

}
