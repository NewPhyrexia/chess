package serviceTests;

import dataAccess.GameDAO;
import service.GameService;

public class GameServiceTests {

  static final GameService service = new GameService(new GameDAO());

}
