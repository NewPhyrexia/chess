package serviceTests;

import dataAccess.*;
import model.AuthData;
import org.junit.jupiter.api.Test;
import service.AuthService;
import service.ClearAppService;
import service.GameService;
import service.UserService;

public class DeleteAllTest {

  static final ClearAppService service = new ClearAppService(new AuthDAO(), new GameDAO(), new UserDAO());

  @Test
  public void deleteAllData() throws DataAccessException {
    // add authTokens
    // add Users
    // add games

    // delete a
    // delete u
    // delete g
    // assert
  }
}
