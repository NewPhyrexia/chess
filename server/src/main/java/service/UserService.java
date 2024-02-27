package service;

import dataAccess.AuthDAOInterface;
import dataAccess.DataAccessException;
import dataAccess.UserDAOInterface;
import model.AuthData;
import model.RegistrationReq;
import reqAndRes.ClearAppServiceRes;
import reqAndRes.LoginRes;
import reqAndRes.RegistrationRes;

import java.util.Collection;

public class UserService {

  private final AuthDAOInterface authInterface;
  private final UserDAOInterface userInterface;

  public UserService(UserDAOInterface userInterface, AuthDAOInterface authInterface) {
    this.userInterface = userInterface;
    this.authInterface = authInterface;
  }

  public RegistrationRes register(RegistrationReq user) throws DataAccessException {
    AuthData authData = null;
    // check if user exists
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

  public LoginRes login(RegistrationReq user) throws DataAccessException {
    AuthData authData=null;

    try {
      // user not in system
      if (userInterface.getUser(user.username()) == null) {
        return null;
      }
      // provided password does not match saved password
      if (!user.password().equals(userInterface.getUser(user.username()).password())) {
        return null;
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

  public void logout(AuthData authToken) throws DataAccessException{
    authInterface.deleteAuthToken(authToken.authToken());
  }

  public Collection<RegistrationReq> listUsers() throws DataAccessException {
    return userInterface.listUsers();
  }

  public Collection<AuthData> listAuthTokens() throws DataAccessException {
    return authInterface.listAuthTokens();
  }
}
