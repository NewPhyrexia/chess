package service;

import dataAccess.GameDAOInterface;
import model.AuthData;
import model.GameData;
import model.UserData;

public class GameService {

  private GameDAOInterface gameInterface;

  public GameService(GameDAOInterface gameInterface) {this.gameInterface = gameInterface;}
//  public AuthData createGame(GameData game) {}
//  public AuthData joinGame(GameData game) {}
//  public AuthData listGames(GameData game) {}
}
