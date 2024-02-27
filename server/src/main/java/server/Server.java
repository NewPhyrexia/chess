package server;

import com.google.gson.Gson;
import dataAccess.*;
import reqAndRes.*;
import service.ClearAppService;
import service.GameService;
import service.UserService;
import spark.*;

public class Server {

  private final ClearAppService CAService = new ClearAppService();
  private final UserService UService = new UserService();
  private final GameService GService = new GameService();


  public int run(int desiredPort) {
    Spark.port(desiredPort);

    Spark.staticFiles.location("web");

    // Register your endpoints and handle exceptions here.
    Spark.delete("/db", this::clearApp);
    Spark.post("/user", this::register);
    Spark.post("/session", this::login);
    Spark.delete("/session", this::logout);
    Spark.get("/game", this::listGames);
    Spark.post("/game", this::createGame);
    Spark.put("/game", this::joinGame);
    Spark.exception(DataAccessException.class, this::exceptionHandler);

    Spark.awaitInitialization();
    return Spark.port();
  }

  public void stop() {
    Spark.stop();
    Spark.awaitStop();
  }

  private void exceptionHandler(DataAccessException ex, Request req, Response res) {
    res.status(ex.StatusCode());
  }

  public Object clearApp(Request req, Response res) throws DataAccessException {
    String reqBody = req.body();
    var clearAppReq = new Gson().fromJson(reqBody, ClearAppServiceReq.class);
    var result = CAService.deleteAllDB(clearAppReq);
    if (result.message() == null){
      res.status(200);
    }
    else {
      res.status(500);
    }
    res.type("application/json");
    return new Gson().toJson(result);
  }

  public Object register(Request req, Response res) throws DataAccessException {
    var reqBody = req.body();
    var registrationReq = new Gson().fromJson(reqBody, RegistrationReq.class);
    var result = UService.register(registrationReq);
    if (result.message() == null){
      res.status(200);
    }
    else if (result.message().equals("Error: bad request")) {
      res.status(400);
    }
    else if (result.message().equals("Error: already taken")) {
      res.status(403);
    }
    else {
      res.status(500);
    }
    res.type("application/json");
    return new Gson().toJson(result);
  }

  public Object login(Request req, Response res) throws DataAccessException {
    var reqBody = req.body();
    var loginReq = new Gson().fromJson(reqBody, LoginReq.class);
    var result = UService.login(loginReq);
    if (result.message() == null){
      res.status(200);
    }
    else if (result.message().equals("Error: unauthorized")) {
      res.status(401);
    }
    else {
      res.status(500);
    }
    res.type("application/json");
    return new Gson().toJson(result);
  }

  public Object logout(Request req, Response res) throws DataAccessException {
    var reqHeaders = req.headers("authorization");
    var logoutReq = new LogoutReq(reqHeaders);
    var result = UService.logout(logoutReq);
    if (result.message() == null){
      res.status(200);
    }
    else if (result.message().equals("Error: unauthorized")) {
      res.status(401);
    }
    else {
      res.status(500);
    }
    res.type("application/json");
    return new Gson().toJson(result);
  }

  public Object listGames(Request req, Response res) throws DataAccessException {
    var reqHeaders = req.headers("authorization");
    var listGamesReq = new ListGamesReq(reqHeaders);
    var result = GService.listGames(listGamesReq);
    if (result.message() == null){
      res.status(200);
    }
    else if (result.message().equals("Error: unauthorized")) {
      res.status(401);
    }
    else {
      res.status(500);
    }
    res.type("application/json");
    return new Gson().toJson(result);
  }

  public Object createGame(Request req, Response res) throws DataAccessException {
    var reqHeaders = req.headers("authorization");
    var reqBody = req.body();
    var createGameReq = new CreateGameReq(reqHeaders, new Gson().fromJson(reqBody, CreateGameReq.class).gameName());
    var result = GService.createGame(createGameReq);
    if (result.message() == null){
      res.status(200);
    }
    else if (result.message().equals("Error: bad request")) {
      res.status(400);
    }
    else if (result.message().equals("Error: unauthorized")) {
      res.status(401);
    }
    else {
      res.status(500);
    }
    res.type("application/json");
    return new Gson().toJson(result);
  }

  public Object joinGame(Request req, Response res) throws DataAccessException{
    var reqHeaders = req.headers("authorization");
    var reqBody = req.body();
    var gson = new Gson().fromJson(reqBody, JoinGameReq.class);
    var joinGameReq = new JoinGameReq(reqHeaders, gson.playerColor(), gson.gameID());
    var result = GService.joinGame(joinGameReq);
    if (result.message() == null){
      res.status(200);
    }
    else if (result.message().equals("Error: bad request")) {
      res.status(400);
    }
    else if (result.message().equals("Error: unauthorized")) {
      res.status(401);
    }
    else if (result.message().equals("Error: already taken")) {
      res.status(403);
    }
    else {
      res.status(500);
    }
    res.type("application/json");
    return new Gson().toJson(result);
  }
}
