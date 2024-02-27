package service;

import dataAccess.*;
import model.GameData;
import reqAndRes.*;

import java.util.Collection;

public class GameService {

  private final AuthDAOInterface authInterface = AuthDAO.getInstance();
  private GameDAOInterface gameInterface = GameDAO.getInstance();
  private final HelperService helperService = new HelperService(authInterface);


  public GameService() {}
  public CreateGameRes createGame(CreateGameReq req) throws DataAccessException {
    int gameID = 0;

    try {
        if (!helperService.AuthTokenCheck(req.authToken())) {
          return new CreateGameRes(null,"Error: unauthorized");
        }
        gameID = gameInterface.createGame(req.gameName());
    } catch(Exception e) {
      if (e instanceof DataAccessException){
        return new CreateGameRes( null,"Error: DataAccessException.");
      }
    }
    return new CreateGameRes(gameID,null);
  }

  public ListGamesRes listGames(ListGamesReq req) throws DataAccessException {

    try {
      if (helperService.AuthTokenCheck(req.authToken())) {
        return new ListGamesRes(gameInterface.listGames(), null);
      } else
        return new ListGamesRes(null,"Error: unauthorized");

    } catch (Exception e) {
      if (e instanceof DataAccessException) {
        return new ListGamesRes(null,"Error: DataAccessException.");
      }
    }
    return new ListGamesRes(gameInterface.listGames(), null);
  }

  public JoinGameRes joinGame(JoinGameReq req) {
    try {
      // checks
        // check auth
        if (!helperService.AuthTokenCheck(req.authToken())) {
          return new JoinGameRes("Error: unauthorized");
        }
        // check if gameID is not 0
        if (req.gameID() == 0) {
          return new JoinGameRes("Error: bad request");
        }
        if (gameInterface.getGame(req.gameID()) == null) {
          return new JoinGameRes("Error: game does not exist");
        }
        // check for existing users in game
        var username = authInterface.getAuthToken(req.authToken()).username();
        if (req.playerColor().equalsIgnoreCase("black")) {
          var blackUsername=gameInterface.getGame(req.gameID()).blackUsername();
          if (blackUsername != null
                  && !blackUsername.equals(username)) {
            return new JoinGameRes("Error: already taken");
          }
        } else if (req.playerColor().equalsIgnoreCase("white")) {
          var whiteUsername=gameInterface.getGame(req.gameID()).whiteUsername();
          if (whiteUsername != null
                  && !whiteUsername.equals(username)) {
            return new JoinGameRes("Error: already taken");
          }
        }
        gameInterface.updateGame(req.playerColor(), req.gameID(), username);
    } catch (Exception e) {
      if (e instanceof DataAccessException) {
        return new JoinGameRes("Error: DataAccessException.");
      }
    }
    return new JoinGameRes(null);
  }

  public Collection<GameData> listAllGames() throws DataAccessException { // specific method for testing clearApp
    return gameInterface.listGames();
  }



}
