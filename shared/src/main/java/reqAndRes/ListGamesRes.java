package reqAndRes;

import model.GameData;

public record ListGamesRes(GameData[] listOfGames, String message) {
  ListGamesRes setMessage(String newMessage) {
    return new ListGamesRes(listOfGames, newMessage);
  }
}
