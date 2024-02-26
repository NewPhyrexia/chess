package reqAndRes;

public record JoinGameReq(String authToken, String teamColor, int gameID) {
}
