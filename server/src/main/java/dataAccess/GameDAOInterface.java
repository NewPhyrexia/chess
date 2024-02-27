package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;

public interface GameDAOInterface {

  int createGame(String gameName) throws DataAccessException;

  ChessGame getGame(int gameID) throws DataAccessException;

  Collection<ChessGame>  listGames() throws DataAccessException;
  void deleteGame(int gameID) throws DataAccessException;

  void deleteAllGames()  throws DataAccessException;
}
