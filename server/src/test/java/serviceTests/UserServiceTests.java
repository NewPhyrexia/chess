package serviceTests;

import dataAccess.UserDAO;
import service.UserService;

public class UserServiceTests {
  static final UserService service = new UserService(new UserDAO());

}
