package serviceTests;

import dataAccess.AuthDAO;
import service.AuthService;

public class AuthServiceTests {

  static final AuthService service = new AuthService(new AuthDAO());

}
