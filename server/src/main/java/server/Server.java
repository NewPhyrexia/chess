package server;

import com.google.gson.Gson;
import dataAccess.*;
import reqAndRes.ClearAppServiceReq;
import reqAndRes.RegistrationReq;
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
    Spark.delete("/db", this::ClearApp);
    Spark.post("/user", this::Register);
//    Spark.post("/session", this::Login);
//    Spark.delete("/session", this::Logout);
//    Spark.get("/game", this::ListGames);
//    Spark.post("/game", this::CreateGame);
//    Spark.put("/game", this::JoinGame);
    Spark.exception(DataAccessException.class, this::exceptionHandler);

    Spark.awaitInitialization();
    return Spark.port();
  }

  public int port() {
    return Spark.port();
  }

  public void stop() {
    Spark.stop();
    Spark.awaitStop();
  }

  private void exceptionHandler(DataAccessException ex, Request req, Response res) {
    res.status(ex.StatusCode());
  }

  public Object ClearApp(Request req, Response res) throws DataAccessException {
    String reqBody = req.body();
    var clearAppReq = new Gson().fromJson(reqBody, ClearAppServiceReq.class);
    var result = CAService.deleteAllDB(clearAppReq);
    if (result.message() == null){
      res.status(200);
    }
    else {
      res.status(500  );
    }
    res.type("application/json");
    return new Gson().toJson(result);
  }

  public Object Register(Request req, Response res) throws DataAccessException {
    var reqBody = req.body();
    var registrationReq = new Gson().fromJson(reqBody, RegistrationReq.class);
    var result = UService.register(registrationReq);


    return new Gson().toJson(result);
  }
//
//  public Object Login(Request req, Response res) throws DataAccessException {
//
//  }
//
//  public Object Logout(Request req, Response res) throws DataAccessException {
//
//  }
//
//  public Object ListGames(Request req, Response res) throws DataAccessException {
//
//  }
//
//  public Object CreateGame(Request req, Response res) throws DataAccessException {
//
//  }
//
//  public Object JoinGame(Request req, Response res) throws DataAccessException {
//
//  }
}
