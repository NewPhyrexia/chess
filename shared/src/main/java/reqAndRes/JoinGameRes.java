package reqAndRes;

public record JoinGameRes(String message) {

  JoinGameRes setMessage(String newMessage){
    return new JoinGameRes(newMessage);
  }
}
