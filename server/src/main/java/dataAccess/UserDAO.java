package dataAccess;

import model.UserData;

import java.util.HashMap;

public class UserDAO {

  final private HashMap<String, UserData> allUserData = new HashMap<>();

  public UserData addUser(UserData user)  {
    user = new UserData(user.username(), user.password(), user.email());

    allUserData.put(user.username(),user);
    return user;
  }

  public void deleteAllUsers() throws DataAccessException {
    allUserData.clear();
  }
}
