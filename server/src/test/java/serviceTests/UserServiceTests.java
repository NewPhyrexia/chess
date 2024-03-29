package serviceTests;

import dataAccess.*;
import dataAccess.DAO.AuthDAO;
import dataAccess.DAO.UserDAO;
import dataAccess.interfaces.AuthDAOInterface;
import dataAccess.interfaces.UserDAOInterface;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import req.ClearAppServiceReq;
import req.LoginReq;
import req.LogoutReq;
import req.RegistrationReq;
import service.ClearAppService;
import service.UserService;

import static org.junit.jupiter.api.Assertions.*;


public class UserServiceTests {

  static final UserDAOInterface userInterface= SqlUserDAO.getInstance();
  static final AuthDAOInterface authInterface= SqlAuthDAO.getInstance();
  static final UserService UService = new UserService();
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
    var token = UService.register(new RegistrationReq("Dakota", "1sC00l4", "Iam@hotmail.com")).authToken();
    var newToken = UService.login(new LoginReq("Dakota", "1sC00l4")).authToken();

    assertNotEquals(token, newToken);
  }

  @Test
  void passwordDoesntMatch() throws DataAccessException {
    UService.register(new RegistrationReq("Dakota", "1sC00l4", "Iam@hotmail.com"));
    var LoginRes = UService.login(new LoginReq("Dakota", "isCool4"));
    var newToken = LoginRes.authToken();
    assertNull(newToken);
  }

  @Test
  void logoutSuccess() throws DataAccessException{
    var user = new UserData("Dakota", "1sC00l4", "Iam@hotmail.com");
    userInterface.addUser(user);
    var authData = authInterface.createAuthToken(user.username());
    authInterface.addAuthData(authData);

    UService.logout(new LogoutReq(authData.authToken()));

    assertNull(authInterface.getAuthToken(authData.authToken()));
  }

  @Test
  void failedLogout() throws DataAccessException {
    AuthData authData = new AuthData("nonExistentUser", "Nobody");
    UService.logout(new LogoutReq(authData.authToken()));

    assertNull(authInterface.getAuthToken(authData.authToken()), "The token should still be null");
  }
}
