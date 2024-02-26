package reqAndRes;

public record ClearAppServiceRes(String message) {
  ClearAppServiceRes setMessage(String newMessage) {
    return new ClearAppServiceRes(newMessage);
  }
}
