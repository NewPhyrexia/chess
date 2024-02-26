package dataAccess;

import model.RegistrationReq;

import java.util.Collection;
import java.util.HashMap;

public class UserDAO implements UserDAOInterface{

  final private HashMap<String, RegistrationReq> allUserData = new HashMap<>();

  private static UserDAO instance;

  public static UserDAO getInstance() {
    if (instance == null){
      instance = new UserDAO();
    }
    return instance;
  }

  public RegistrationReq addUser(RegistrationReq user)  {
    user = new RegistrationReq(user.username(), user.password(), user.email());

    allUserData.put(user.username(),user);
    return user;
  }
  public Collection<RegistrationReq> listUsers() {
    return allUserData.values();
  }
  public RegistrationReq getUser(String username) {
    return allUserData.get(username);
  }

  public String getPassword(String username) {

    var user = allUserData.get(username);
    return user.password();
  }

  public void deleteUser(String username) {
    allUserData.remove(username);
  }

  public void deleteAllUsers() {
    allUserData.clear();
  }
}
