package web.server;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import exception.ResponseException;
import req.*;
import res.*;

import java.net.*;

public class ServerFacade {
  private final String serverUrl;
  private String authToken;

  public ServerFacade(String url) {serverUrl = url;}

  public String getAuthToken() { return authToken;}

  public ClearAppServiceRes clearApp() throws ResponseException {
    var path = "/db";
    return this.makeRequest("DELETE",path, null, ClearAppServiceRes.class);
  }

  public RegistrationRes register(RegistrationReq request) throws ResponseException {
    var path = "/user";
    var res = this.makeRequest("POST", path, request, RegistrationRes.class);
    authToken = res.authToken();
    return res;
  }

  public LoginRes login(LoginReq request) throws ResponseException {
    var path = "/session";
    var res = this.makeRequest("POST",path, request, LoginRes.class);
    authToken = res.authToken();
    return res;
  }

  public LogoutRes logout() throws ResponseException {
    var path = "/session";
    var res = this.makeRequest("DELETE", path, new LogoutReq(authToken), LogoutRes.class);
    authToken = null;
    return res;
  }

  public CreateGameRes createGame(CreateGameReq req) throws ResponseException {
    var path = "/game";
    return this.makeRequest("POST", path,  new CreateGameReq(authToken, req.gameName()), CreateGameRes.class);
  }

  public ListGamesRes listGames() throws ResponseException {
    var path = "/game";
    return this.makeRequest("GET", path, new ListGamesReq(authToken), ListGamesRes.class);
  }

  public JoinGameRes joinGame(JoinGameReq request) throws ResponseException {
    var path = "/game";
    var res =  this.makeRequest("PUT", path, new JoinGameReq(authToken,request.playerColor(),request.gameID()), JoinGameRes.class);
    return res;
  }


  private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
    try {
      URL url = (new URI(serverUrl + path)).toURL();
      HttpURLConnection http = (HttpURLConnection) url.openConnection();
      http.setRequestMethod(method);

      if (authToken != null) {
        http.addRequestProperty("Authorization", authToken);
      }

      if (!method.equals("GET")) {
        http.setDoOutput(true);
        writeBody(request, http);
      }
      http.connect();
      throwIfNotSuccessful(http);
      return readBody(http, responseClass);
    } catch (Exception ex) {
      throw new ResponseException(500, ex.getMessage());
    }
  }

  private static void writeBody(Object request, HttpURLConnection http) throws IOException {
    if (request != null) {
      http.addRequestProperty("Content-Type", "application/json");
      String reqData = new Gson().toJson(request);
      try (OutputStream reqBody = http.getOutputStream()) {
        reqBody.write(reqData.getBytes());
      }
    }
  }

  private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
    var status = http.getResponseCode();
    if (!isSuccessful(status)) {
      throw new ResponseException(status, "failure: " + status);
    }
  }

  private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
    T response = null;
    if (http.getContentLength() < 0) {
      try (InputStream respBody = http.getInputStream()) {
        InputStreamReader reader = new InputStreamReader(respBody);
        if (responseClass != null) {
          response = new Gson().fromJson(reader, responseClass);
        }
      }
    }
    return response;
  }

  private boolean isSuccessful(int status) {
    return status / 100 == 2;
  }
}
