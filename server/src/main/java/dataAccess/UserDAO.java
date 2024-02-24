package dataAccess;

import model.UserData;

import java.util.HashMap;

public class UserDAO implements UserDAOInterface{

  final private HashMap<String, UserData> allUserData = new HashMap<>();

  public UserData addUser(UserData user)  {
    user = new UserData(user.username(), user.password(), user.email());

    allUserData.put(user.username(),user);
    return user;
  }

  public UserData getUser(String username) {
    return allUserData.get(username);
  }

  public void deleteUser(String username) {
    allUserData.remove(username);
  }

  public void deleteAllUsers() {
    allUserData.clear();
  }
}
