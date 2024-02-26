package reqAndRes;

public record LogoutRes(String message) {
  LogoutRes setMessage(String newMessage) {return new LogoutRes(newMessage);}
}
