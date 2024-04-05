package service;

import dataAccess.*;
import dataAccess.interfaces.AuthDAOInterface;
import dataAccess.interfaces.GameDAOInterface;
import req.*;
import res.CreateGameRes;
import res.JoinGameRes;
import res.ListGamesRes;


public class GameService {

  private final AuthDAOInterface authInterface = SqlAuthDAO.getInstance();
  private GameDAOInterface gameInterface = SqlGameDAO.getInstance();
  private final HelperService helperService = new HelperService(authInterface);


  public GameService() {}

  /**
   * Creates a gameID
   * @param req
   * @return CreateGamesRes
   * @throws DataAccessException
   */
  public CreateGameRes createGame(CreateGameReq req) throws DataAccessException {
    int gameID = 0;

    try {
        if (!helperService.authTokenCheck(req.authToken())) {
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

  /**
   * Creates an array of current games
   * @param req
   * @return ListGamesRes
   * @throws DataAccessException
   */
  public ListGamesRes listGames(ListGamesReq req) throws DataAccessException {

    try {
      if (helperService.authTokenCheck(req.authToken())) {
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

  /**
   * Allows user to join a game as the white player, black player, or an observer
   * @param req
   * @return JoinGameRes
   * @throws DataAccessException
   */
  public JoinGameRes joinGame(JoinGameReq req) throws DataAccessException {
    try {
      // checks
        // check auth
        if (!helperService.authTokenCheck(req.authToken())) {
          return new JoinGameRes("Error: unauthorized");
        }
        // check if gameID is 0
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
          if (blackUsername != null) {
            return new JoinGameRes("Error: already taken");
          }
        } else if (req.playerColor().equalsIgnoreCase("white")) {
          var whiteUsername=gameInterface.getGame(req.gameID()).whiteUsername();
          if (whiteUsername != null) {
            return new JoinGameRes("Error: already taken");
          }
        }
        var game = gameInterface.getGame(req.gameID()).game();
        gameInterface.updateGame(req.playerColor(), req.gameID(), username, game);
    } catch (Exception e) {
      if (e instanceof DataAccessException) {
        return new JoinGameRes("Error: DataAccessException.");
      }
    }
    return new JoinGameRes(null);
  }
}
