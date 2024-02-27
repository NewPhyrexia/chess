package res;

import model.GameData;

import java.util.Collection;

public record ListGamesRes(GameData[] games, String message) {
}
