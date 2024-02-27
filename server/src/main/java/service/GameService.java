package service;

import dataAccess.*;
import model.GameData;
import reqAndRes.*;

import java.util.Collection;

public class GameService {

  private final AuthDAOInterface authInterface = AuthDAO.getInstance();
  private GameDAOInterface gameInterface = GameDAO.getInstance();
  private final HelperService helperService = new HelperService(authInterface);


  public GameService(GameDAOInterface gameInterface) {this.gameInterface = gameInterface;}
  public CreateGameRes createGame(CreateGameReq req) throws DataAccessException {
    if (helperService.AuthTokenCheck(req.authToken())) {
      var gameID = gameInterface.createGame(req.gameName());
      return gameID;
    } else return new CreateGameRes();
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

  public record joinGame(String playerColor, int gameID) {

  }

  public Collection<GameData> listAllGames() throws DataAccessException { // specific method for testing clearApp
    return gameInterface.listGames();
  }



}
