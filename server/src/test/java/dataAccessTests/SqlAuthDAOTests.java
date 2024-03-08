package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.SqlAuthDAO;
import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class SqlAuthDAOTests {

  static private SqlAuthDAO authDAO = new SqlAuthDAO();

  @BeforeEach
  void clear() throws DataAccessException {
    authDAO.deleteAllAuthTokens();
  }
  @Test
  void createAuthToken() throws DataAccessException {
    var token = authDAO.createAuthToken("testName");
    assertEquals("testName", token.username());
  }

  @Test
  void failCreateAuthToken() throws DataAccessException  {
    var token = authDAO.createAuthToken("testName");
    assertNotEquals("NegTestName", token.username());
  }

  @Test
  void addAuthData() {
    assertDoesNotThrow(() -> authDAO.addAuthData(new AuthData("testToken", "testUsername")));
  }

  @Test
  void failAddAuthData() throws DataAccessException {
    authDAO.addAuthData(new AuthData("testToken", "testUsername"));
    assertThrows(DataAccessException.class, () -> {
      authDAO.addAuthData(new AuthData("testToken", "testUsername"));
    });
  }

  @Test
  void getAuthToken() throws DataAccessException {
    authDAO.addAuthData(new AuthData("testToken", "testUsername"));
    var token = authDAO.getAuthToken("testToken");
    assertEquals("testToken", token.authToken());
  }

  @Test
  void failGetAuthToken() throws DataAccessException {
    authDAO.addAuthData(new AuthData("testToken", "testUsername"));
    var token = authDAO.getAuthToken("testToken");
    assertNotEquals("negTestToken", token.authToken());
  }

  @Test
  void listAuthTokens() throws DataAccessException {
    authDAO.addAuthData(new AuthData("testToken", "testUsername"));
    authDAO.addAuthData(new AuthData("testToken1", "testUsername1"));
    var tokens = authDAO.listAuthTokens();

    assertEquals(2, tokens.size());
  }

  @Test
  void failListAuthTokens() throws DataAccessException {
    authDAO.addAuthData(new AuthData("testToken", "testUsername"));
    var tokens = authDAO.listAuthTokens();

    assertNotEquals(2, tokens.size());
  }

  @Test
  void deleteAuthToken() throws DataAccessException {
    authDAO.addAuthData(new AuthData("testToken", "testUsername"));

    authDAO.deleteAuthToken("testToken");
    var tokens = authDAO.listAuthTokens();

    assertEquals(0, tokens.size());
  }

  @Test
  void failDeleteAuthToken() throws DataAccessException {
    authDAO.addAuthData(new AuthData("testToken", "testUsername"));

    authDAO.deleteAuthToken("negTestToken");
    var tokens = authDAO.listAuthTokens();

    assertNotEquals(0, tokens.size());
  }

  @Test
  void deleteAllAuthTokens() throws DataAccessException {
    authDAO.addAuthData(new AuthData("testToken", "testUsername"));
    authDAO.addAuthData(new AuthData("testToken1", "testUsername1"));

    authDAO.deleteAllAuthTokens();
    var tokens = authDAO.listAuthTokens();

    assertEquals(0, tokens.size());
  }
}