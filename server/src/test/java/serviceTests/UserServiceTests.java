package serviceTests;

import dataAccess.*;
import model.UserData;
import org.junit.jupiter.api.Test;
import service.ClearAppService;
import service.UserService;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTests {

  static final UserDAOInterface userInterface= UserDAO.getInstance();
  static final AuthDAOInterface authInterface= AuthDAO.getInstance();
  static final UserService UService = new UserService(userInterface, authInterface);

  @Test
  void createUser() throws DataAccessException {
    UService.register(new UserData("Dakota", "1sC00l4", "Iam@hotmail.com"));
    UService.register(new UserData("Callie", "H0tStuff1", "TurtleDuck@gmail.com"));
    UService.register(new UserData("Anna", "BanANNA77", "IamtheMASTERcommander@gmail.com"));

    assertEquals(3, UService.listUsers().size());
  }

  @Test
  void duplicateUser() throws DataAccessException {
    UService.register(new UserData("Dakota", "1sC00l4", "Iam@hotmail.com"));
    UService.register(new UserData("Callie", "H0tStuff1", "TurtleDuck@gmail.com"));
    UService.register(new UserData("Anna", "BanANNA77", "IamtheMASTERcommander@gmail.com"));

    // user with same username attempts to create
    UService.register(new UserData("Dakota", "Fake14", "IamNot@hotmail.com"));

    assertEquals(3, UService.listUsers().size());
  }

  @Test
  void successfulLogin() throws DataAccessException {
    var token = UService.register(new UserData("Dakota", "1sC00l4", "Iam@hotmail.com"));
    var newToken = UService.login(new UserData("Dakota", "1sC00l4", "Iam@hotmail.com"));

    assertNotEquals(token, newToken);
  }

  @Test
  void passwordDoesntMatch() throws DataAccessException {
    var token = UService.register(new UserData("Dakota", "1sC00l4", "Iam@hotmail.com"));
    var newToken = UService.login(new UserData("Dakota", "isCool4", "Iam@hotmail.com"));

    assertNull(newToken); // May need to update test after connecting with server
  }
}
