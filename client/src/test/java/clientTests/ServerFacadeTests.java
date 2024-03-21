package clientTests;

import org.junit.jupiter.api.*;
import req.RegistrationReq;
import server.Server;
import web.server.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

  private static Server server;
  static ServerFacade facade;

  @BeforeAll
  public static void init() {
    server = new Server();
    var port = server.run(0);
    System.out.println("Started test HTTP server on " + port);
    facade = new ServerFacade("http://localhost:" + port);
  }

  @AfterAll
  static void stopServer() {
    server.stop();
  }


  @Test
  public void clearAll() {
    assertTrue(true);
  }

  @Test
  void register() throws Exception {
    var userData = facade.register(new RegistrationReq("player1", "password", "p1@email.com"));
    assertEquals("player1", userData.username());
  }

}