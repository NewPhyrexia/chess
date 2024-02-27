package server;

import dataAccess.DataAccessException;
import spark.*;

public class Server {

  public int run(int desiredPort) {
    Spark.port(desiredPort);

    Spark.staticFiles.location("web");

    // Register your endpoints and handle exceptions here.
//    Spark.delete("/db", );
//    Spark.post("/user", );
//    Spark.post("/session", );
//    Spark.delete("/session", );
//    Spark.get("/game", );
//    Spark.post("/game", );
//    Spark.put("/game", );
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


}
