package service;

import dataAccess.*;
import model.AuthData;
import model.UserData;
import reqAndRes.*;

import java.util.Collection;

public class UserService {

  private final AuthDAOInterface authInterface = AuthDAO.getInstance();
  private final UserDAOInterface userInterface = UserDAO.getInstance();


  public UserService() {
  }

  public RegistrationRes register(RegistrationReq req) throws DataAccessException {
    AuthData authData = null;
    // check if user exists
    var user = new UserData(req.username(),req.password(),req.email());
    try {
        if (user.username() == null || user.password() == null || user.email() == null) {
          return new RegistrationRes(null,null,"Error: bad request");
        }
        if (userInterface.getUser(user.username()) != null) {
          return new RegistrationRes(null, null, "Error: already taken");
        }

        userInterface.addUser(user);
        authData=authInterface.createAuthToken(user.username());
        authInterface.addAuthData(authData);
    } catch(Exception e) {
      if (e instanceof DataAccessException){
        return new RegistrationRes(null, null,"Error: DataAccessException.");
      }
    }
    return new RegistrationRes(authData.authToken(), user.username(), null);
  }

  public LoginRes login(LoginReq user) throws DataAccessException {
    AuthData authData=null;

    try {
      // user not in system
      if (userInterface.getUser(user.username()) == null) {
        return new LoginRes(null,null,"Error: unauthorized");
      }
      // provided password does not match saved password
      if (!user.password().equals(userInterface.getUser(user.username()).password())) {
        return new LoginRes(null,null,"Error: unauthorized");
      }
      // create new authToken
      authData=authInterface.createAuthToken(user.username());
      authInterface.addAuthData(authData);
    } catch (Exception e) {
      if (e instanceof DataAccessException) {
        return new LoginRes(null, null, "Error: DataAccessException.");
      }
    }
    return new LoginRes(authData.authToken(), user.username(), null);
  }

  public LogoutRes logout(LogoutReq authToken) throws DataAccessException{
    try {
      if (authToken == null || authInterface.getAuthToken(authToken.authToken()) == null){
        return new LogoutRes("Error: unauthorized");
      }
      authInterface.deleteAuthToken(authToken.authToken());
    } catch (Exception e) {
      if (e instanceof DataAccessException) {
        return new LogoutRes("Error: DataAccessException.");
      }
    }
    return new LogoutRes(null);
  }

  public Collection<UserData> listUsers() throws DataAccessException {
    return userInterface.listUsers();
  }

  public Collection<AuthData> listAuthTokens() throws DataAccessException {
    return authInterface.listAuthTokens();
  }
}
