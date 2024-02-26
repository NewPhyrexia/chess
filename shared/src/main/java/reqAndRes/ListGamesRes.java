package reqAndRes;

import model.GameData;

public record ListGamesRes(GameData[] listOfGames, String message) {
}
