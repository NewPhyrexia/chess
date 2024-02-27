package dataAccess;

import model.UserData;

import java.util.Collection;
import java.util.HashMap;

public class UserDAO implements UserDAOInterface{

  final private HashMap<String, UserData> allUserData = new HashMap<>();

  private static UserDAO instance;

  public static UserDAO getInstance() {
    if (instance == null){
      instance = new UserDAO();
    }
    return instance;
  }

  public UserData addUser(UserData user)  {
    user = new UserData(user.username(), user.password(), user.email());

    allUserData.put(user.username(),user);
    return user;
  }

  public Collection<UserData> listUsers() {
    return allUserData.values();
  }

  public UserData getUser(String username) {
    return allUserData.get(username);
  }

  public void deleteAllUsers() {
    allUserData.clear();
  }
}
