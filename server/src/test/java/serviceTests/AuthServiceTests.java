package serviceTests;

import dataAccess.AuthDAO;

public class AuthServiceTests {

  static final AuthService service = new AuthService(new AuthDAO());

}
