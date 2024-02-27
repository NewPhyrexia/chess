package reqAndRes;

import model.GameData;

import java.util.Collection;

public record ListGamesRes(GameData[] games, String message) {
}
