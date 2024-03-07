package service;

import dataAccess.*;
import dataAccess.interfaces.AuthDAOInterface;
import dataAccess.interfaces.UserDAOInterface;
import model.AuthData;
import model.UserData;
import req.*;
import res.LoginRes;
import res.LogoutRes;
import res.RegistrationRes;

import java.util.Collection;

public class UserService {

  private final AuthDAOInterface authInterface = SqlAuthDAO.getInstance();
  private final UserDAOInterface userInterface = SqlUserDAO.getInstance();

  /**
   * registers a new user into the db and assigns an authToken
   * @param req
   * @return RegistrationRes
   * @throws DataAccessException
   */
  public RegistrationRes register(RegistrationReq req) throws DataAccessException {
    AuthData authData = null;
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

  /**
   * logs in a valid user
   * @param req
   * @return LoginRes
   * @throws DataAccessException
   */
  public LoginRes login(LoginReq req) throws DataAccessException {
    AuthData authData=null;
    var user = req;
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

  /**
   * removes user associated with the authToken from db
   * @param req
   * @return LogoutRes
   * @throws DataAccessException
   */
  public LogoutRes logout(LogoutReq req) throws DataAccessException{
    var authToken = req;
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
