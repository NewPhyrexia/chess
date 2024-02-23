package dataAccess;

import model.AuthData;

import java.util.HashMap;

public class AuthDAO {
  final private HashMap<String, AuthData> allAuthTokens = new HashMap<>();

  public AuthData addAuthToken(AuthData token) {
    token = new AuthData(token.authToken(), token.username());

    allAuthTokens.put(token.authToken(), token);
    return token;
  }

  public void deleteAllAuthTokens() throws DataAccessException {
    allAuthTokens.clear();
  }
}
