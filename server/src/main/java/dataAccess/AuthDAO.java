package dataAccess;

import model.AuthData;

import java.util.Collection;
import java.util.HashMap;

public class AuthDAO implements AuthDAOInterface{
  final private HashMap<String, AuthData> allAuthTokens = new HashMap<>();

  public AuthData addAuthToken(AuthData token) {
    token = new AuthData(token.authToken(), token.username());

    allAuthTokens.put(token.authToken(), token);
    return token;
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
