package web.server;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import dataAccess.DataAccessException;

import java.io.*;
import java.net.*;

public class ServerFacade {
  private final String serverUrl;

  public ServerFacade(String url) {serverUrl = url;}

  public void clearApp() throws DataAccessException {
    var path = "/db";
    this.makeRequest("DELETE",path, null, null);
  }

  public register() throws DataAccessException {
    var path = "/user";
    return this.makeRequest("POST",path, , );
  }

  public login() throws DataAccessException {
    var path = "/session";
    return this.makeRequest("POST",path, , );
  }

  public void logout() throws DataAccessException {
    var path = "/session";
    this.makeRequest("DELETE",path, null, null);
  }

  public GameData[] listGames() throws DataAccessException {
    var path = "/game";
    return this.makeRequest("GET",path, , );
  }

  public createGame() throws DataAccessException {
    var path = "/game";
    return this.makeRequest("POST",path, , );
  }

  public joinGame() throws DataAccessException {
    var path = "/game";
    return this.makeRequest("PUT",path, , );
  }


  private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws DataAccessException {
    try {
      URL url = (new URI(serverUrl + path)).toURL();
      HttpURLConnection http = (HttpURLConnection) url.openConnection();
      http.setRequestMethod(method);
      http.setDoOutput(true);

      writeBody(request, http);
      http.connect();
      throwIfNotSuccessful(http);
      return readBody(http, responseClass);
    } catch (Exception ex) {
      throw new DataAccessException(ex.getMessage());
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

  private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, DataAccessException {
    var status = http.getResponseCode();
    if (!isSuccessful(status)) {
      throw new DataAccessException("failure: " + status);
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
