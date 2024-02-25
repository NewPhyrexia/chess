package serviceTests;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import model.UserData;
import org.junit.jupiter.api.Test;
import service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserServiceTests {

  static final UserDAO userDAO = UserDAO.getInstance();
  static final AuthDAO authDAO = AuthDAO.getInstance();
  static final UserService UService = new UserService(userDAO, authDAO);

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
    UService.login(new UserData("Dakota", "1sC00l4", "Iam@hotmail.com"));
  }

  @Test
  void passwordDoesntMatch() throws DataAccessException {

  }
}
