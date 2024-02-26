package reqAndRes;

public record RegistrationRes(String authToken, String username, String message) {
  RegistrationRes setMessage(String newMessage) {return new RegistrationRes(authToken, username, newMessage);}
}
