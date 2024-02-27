package reqAndRes;

import model.GameData;

import java.util.Collection;

public record ListGamesRes(Collection<GameData> listOfGames, String message) {
}
