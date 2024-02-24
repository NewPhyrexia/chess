package serviceTests;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import org.junit.jupiter.api.Test;
import service.AuthService;
import service.ClearAppService;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClearAppServiceTests {
  static final ClearAppService service = new ClearAppService(new AuthDAO(), new GameDAO(), new UserDAO());

  // add services for other 2 services
  @Test
  void deleteAll() throws DataAccessException {
    // add user

    // add token

    // add game

    service.deleteAllDB();
    assertEquals(0, 0); // users
    assertEquals(0, 0); // tokens
    assertEquals(0, 0); // games
  }

}
