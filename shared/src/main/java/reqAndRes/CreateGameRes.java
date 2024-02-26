package reqAndRes;

public record CreateGameRes(int gameID, String message) {
   CreateGameRes setMessage(String newMessage) {
    return new CreateGameRes(gameID, newMessage);
  }
}
