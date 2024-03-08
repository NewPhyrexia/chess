package dataAccessTests;

import dataAccess.DAO.GameDAO;
import dataAccess.DataAccessException;
import dataAccess.SqlUserDAO;
import model.UserData;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import req.ClearAppServiceReq;

public class SqlUserDAOTests {

  private final SqlUserDAO userDAO = SqlUserDAO.getInstance();

  @BeforeEach
  void clear() throws DataAccessException {
    userDAO.deleteAllUsers();
  }

  @Test
  void addUser() {

    assertDoesNotThrow(() -> userDAO.addUser(new UserData("testName", "testPassword", "testEmail")));
  }

  @Test
  void failAddUser() throws DataAccessException  {
    userDAO.addUser(new UserData("testName", "testPassword", "testEmail"));
    assertThrows(DataAccessException.class, () -> {
      userDAO.addUser(new UserData("testName", "testPassword", "testEmail"));
    });
  }

  @Test
  void listUsers() throws DataAccessException {
    userDAO.addUser(new UserData("testName", "testPassword", "testEmail"));
    userDAO.addUser(new UserData("testName1", "testPassword1", "testEmail1"));
    var users = userDAO.listUsers();

    assertEquals(2, users.size());
  }

  @Test
  void failListUsers() throws DataAccessException {
    userDAO.addUser(new UserData("testName", "testPassword", "testEmail"));
    userDAO.addUser(new UserData("testName1", "testPassword1", "testEmail1"));
    var users = userDAO.listUsers();

    assertNotEquals(3, users.size());
  }

  @Test
  void getUser() throws DataAccessException {
    userDAO.addUser(new UserData("testName", "testPassword", "testEmail"));
    var user = userDAO.getUser("testName");
    assertEquals("testName", user.username());
  }

  @Test
  void failGetUser() throws DataAccessException {
    userDAO.addUser(new UserData("testName", "testPassword", "testEmail"));
    var user = userDAO.getUser("testName");
    assertNotEquals("negTestName", user.username());
  }

  @Test
  void deleteAllUsers() throws DataAccessException {
    userDAO.addUser(new UserData("testName", "testPassword", "testEmail"));
    userDAO.addUser(new UserData("testName1", "testPassword1", "testEmail1"));

    userDAO.deleteAllUsers();
    var users = userDAO.listUsers();

    assertEquals(0, users.size());
  }
}
