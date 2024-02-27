package server;

import dataAccess.DataAccessException;
import spark.*;

public class Server {

  public int run(int desiredPort) {
    Spark.port(desiredPort);

    Spark.staticFiles.location("web");

    // Register your endpoints and handle exceptions here.
    Spark.delete("/db", this::ClearApp);
//    Spark.post("/user", this::Register);
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

  }

//  public Object Register(Request req, Response res) throws DataAccessException {
//
//  }
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
