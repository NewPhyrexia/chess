package reqAndRes;

public record LoginRes(String authToken, String username, String message) {
  LoginRes setMessage(String newMessage) {
    return new LoginRes(authToken, username, newMessage);
  }
}
