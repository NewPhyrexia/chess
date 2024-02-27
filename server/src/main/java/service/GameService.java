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
  int gameID = 0;
  public CreateGameRes createGame(CreateGameReq req) throws DataAccessException {
    try {
        if (!helperService.AuthTokenCheck(req.authToken())) {
          return new CreateGameRes(0,"Error: unauthorized");
        }
        gameID = gameInterface.createGame(req.gameName());
    } catch(Exception e) {
      if (e instanceof DataAccessException){
        return new CreateGameRes( 0,"Error: DataAccessException.");
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

      // meat of function
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
