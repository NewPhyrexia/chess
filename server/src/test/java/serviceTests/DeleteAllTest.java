package serviceTests;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import model.AuthData;
import org.junit.jupiter.api.Test;
import service.AuthService;
import service.GameService;
import service.UserService;

public class DeleteAllTest {

  static final AuthService AService = new AuthService();
  static final UserService UService = new UserService();
  static final GameService GService = new GameService();

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
