package dataAccess.DAO;

import dataAccess.interfaces.AuthDAOInterface;
import model.AuthData;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class AuthDAO implements AuthDAOInterface {
  final private HashMap<String, AuthData> allAuthTokens = new HashMap<>();

  private static AuthDAO instance;

  public static AuthDAO getInstance() {
    if (instance == null){
      instance = new AuthDAO();
    }
    return instance;
  }

  public AuthData createAuthToken(String username) {
    if(username == null || username.isEmpty()){
      return null;
    }

    String token = UUID.randomUUID().toString();
    return new AuthData(token, username);
  }

  public AuthData addAuthData(AuthData data) {
    data = new AuthData(data.authToken(), data.username());

    allAuthTokens.put(data.authToken(), data);
    return data;
  }
  public AuthData getAuthToken(String token) {return allAuthTokens.get(token);}

  public Collection<AuthData> listAuthTokens() {
    return allAuthTokens.values();
  }

  public void deleteAuthToken(String token) {allAuthTokens.remove(token);}

  public void deleteAllAuthTokens() {
    allAuthTokens.clear();
  }
}
