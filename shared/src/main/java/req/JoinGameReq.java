package req;

public record JoinGameReq(String authToken, String playerColor, int gameID) {
}
