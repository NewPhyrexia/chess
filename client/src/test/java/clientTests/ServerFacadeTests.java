package clientTests;

import dataAccess.DataAccessException;
import exception.ResponseException;
import org.junit.jupiter.api.*;
import req.CreateGameReq;
import req.JoinGameReq;
import req.LoginReq;
import req.RegistrationReq;
import server.Server;
import web.server.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

  private static Server server;
  static ServerFacade facade;

  @BeforeAll
  public static void init() throws ResponseException {
    server = new Server();
    var port = server.run(0);
    System.out.println("Started test HTTP server on " + port);
    facade = new ServerFacade("http://localhost:" + port);
  }

  @AfterAll
  static void stopServer() {
    server.stop();
  }

  @BeforeEach
  void clear() throws ResponseException {
    facade.clearApp();
  }

  @Test
  public void clearAll() throws ResponseException {
    var userData = facade.register(new RegistrationReq("player1", "password", "p1@email.com"));
    facade.clearApp();
    userData = facade.register(new RegistrationReq("player1", "password", "p1@email.com"));

    assertEquals("player1", userData.username());
  }

  @Test
  void register() throws ResponseException {
    var userData = facade.register(new RegistrationReq("player1", "password", "p1@email.com"));

    assertEquals("player1", userData.username());
  }

  @Test
  void negRegister() throws ResponseException {
    var userData = facade.register(new RegistrationReq("player1", "password", "p1@email.com"));

    assertThrows(ResponseException.class, () -> {
      facade.register(new RegistrationReq("player1", "password", "p1@email.com"));
    });
  }

  @Test
  void login() throws ResponseException {
    var authToken = facade.register(new RegistrationReq("player1", "password", "p1@email.com")).authToken();
    var newAuthToken = facade.login(new LoginReq("player1","password"));

    assertNotEquals(authToken, newAuthToken);
  }

  @Test
  void negLogin() throws ResponseException {
    facade.register(new RegistrationReq("player1", "password", "p1@email.com"));

    assertThrows(ResponseException.class, () -> {
      facade.login(new LoginReq("player2", "password"));
    });
  }

  @Test
  void logout() throws ResponseException {
    facade.register(new RegistrationReq("player1", "password", "p1@email.com")).authToken();
    facade.login(new LoginReq("player1","password"));
    var logoutRes = facade.logout();
    assertNull(logoutRes.message());
  }

  @Test
  void negLogout() throws ResponseException {
    var authToken = facade.register(new RegistrationReq("player1", "password", "p1@email.com")).authToken();
    facade.login(new LoginReq("player1","password"));
    facade.logout();
    assertNotNull(authToken);  // not sure what a good test for this case would be.
  }

  @Test
  void createGame() throws ResponseException {
    facade.register(new RegistrationReq("player1", "password", "p1@email.com"));
    facade.createGame(new CreateGameReq(null, "gameName"));

    assertEquals(1, facade.listGames().games().length);
  }

  @Test
  void negCreateGame() throws ResponseException {
    facade.register(new RegistrationReq("player1", "password", "p1@email.com"));
    facade.createGame(new CreateGameReq(null, "gameName"));

    assertNotEquals(2, facade.listGames().games().length);
  }

  @Test
  void listGames() throws ResponseException {
    facade.register(new RegistrationReq("player1", "password", "p1@email.com"));
    facade.createGame(new CreateGameReq(null, "gameName"));
    facade.createGame(new CreateGameReq(null, "gameName1"));

    assertEquals(2, facade.listGames().games().length);
  }

  @Test
  void negListGames() throws ResponseException {
    facade.register(new RegistrationReq("player1", "password", "p1@email.com"));
    facade.createGame(new CreateGameReq(null, "gameName"));

    assertNotEquals(2, facade.listGames().games().length);
  }

  @Test
  void joinGameAsPlayer() throws ResponseException {
    facade.register(new RegistrationReq("player1", "password", "p1@email.com"));
    var gameID = facade.createGame(new CreateGameReq(null, "gameName")).gameID();
    var res = facade.joinGame(new JoinGameReq(null, "BLACK", gameID));

    assertNull(res.message());
  }

  @Test
  void joinGameAsObserver() throws ResponseException {
    facade.register(new RegistrationReq("player1", "password", "p1@email.com"));
    var gameID = facade.createGame(new CreateGameReq(null, "gameName")).gameID();
    var res = facade.joinGame(new JoinGameReq(null, null, gameID));

    assertNull(res.message());
  }

  @Test
  void negJoinGame() throws ResponseException {
    facade.register(new RegistrationReq("player1", "password", "p1@email.com"));
    var gameID = facade.createGame(new CreateGameReq(null, "gameName")).gameID();

    assertThrows(ResponseException.class, () -> {
      facade.joinGame(new JoinGameReq(null, "cool", gameID));
    });
  }
}