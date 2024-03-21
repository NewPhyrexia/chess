package clientTests;

import dataAccess.DataAccessException;
import exception.ResponseException;
import org.junit.jupiter.api.*;
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
    facade.clearApp();
  }

  @AfterAll
  static void stopServer() {
    server.stop();
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
}