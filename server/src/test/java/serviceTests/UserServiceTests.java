package serviceTests;

import dataAccess.*;
import model.AuthData;
import model.RegistrationReq;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reqAndRes.ClearAppServiceReq;
import service.ClearAppService;
import service.UserService;

import static org.junit.jupiter.api.Assertions.*;


public class UserServiceTests {

  static final UserDAOInterface userInterface= UserDAO.getInstance();
  static final AuthDAOInterface authInterface= AuthDAO.getInstance();
  static final UserService UService = new UserService(userInterface, authInterface);
  static final ClearAppService service = new ClearAppService();

  @BeforeEach
  void clear() throws DataAccessException {
    service.deleteAllDB(new ClearAppServiceReq());
  }
  @Test
  void createUser() throws DataAccessException {
    UService.register(new RegistrationReq("Dakota", "1sC00l4", "Iam@hotmail.com"));
    UService.register(new RegistrationReq("Callie", "H0tStuff1", "TurtleDuck@gmail.com"));
    UService.register(new RegistrationReq("Anna", "BanANNA77", "IamtheMASTERcommander@gmail.com"));

    assertEquals(3, UService.listUsers().size());
  }

  @Test
  void duplicateUser() throws DataAccessException {
    UService.register(new RegistrationReq("Dakota", "1sC00l4", "Iam@hotmail.com"));
    UService.register(new RegistrationReq("Callie", "H0tStuff1", "TurtleDuck@gmail.com"));
    UService.register(new RegistrationReq("Anna", "BanANNA77", "IamtheMASTERcommander@gmail.com"));

    // user with same username attempts to create
    UService.register(new RegistrationReq("Dakota", "Fake14", "IamNot@hotmail.com"));

    assertEquals(3, UService.listUsers().size());
  }

  @Test
  void successfulLogin() throws DataAccessException {
    var token = UService.register(new RegistrationReq("Dakota", "1sC00l4", "Iam@hotmail.com"));
    var newToken = UService.login(new RegistrationReq("Dakota", "1sC00l4", "Iam@hotmail.com"));

    assertNotEquals(token, newToken);
  }

  @Test
  void passwordDoesntMatch() throws DataAccessException {
    UService.register(new RegistrationReq("Dakota", "1sC00l4", "Iam@hotmail.com"));
    var newToken = UService.login(new RegistrationReq("Dakota", "isCool4", "Iam@hotmail.com"));

    assertNull(newToken); // May need to update test after connecting with server as this may return an error
  }

  @Test
  void logoutSuccess() throws DataAccessException{
    var user = new RegistrationReq("Dakota", "1sC00l4", "Iam@hotmail.com");
    userInterface.addUser(user);
    var authData = authInterface.createAuthToken(user.username());
    authInterface.addAuthData(authData);

    UService.logout(authData);

    assertNull(authInterface.getAuthToken(authData.authToken()));
  }

  @Test
  void failedLogout() throws DataAccessException {
    AuthData authData = new AuthData("nonExistentUser", "Nobody");
    UService.logout(authData);

    assertNull(authInterface.getAuthToken(authData.authToken()), "The token should still be null");
  }
}
