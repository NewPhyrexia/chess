package service;

import dataAccess.AuthDAOInterface;
import model.AuthData;

public class AuthService {
  private AuthDAOInterface authInterface;

  public AuthService(AuthDAOInterface authInterface) {this.authInterface = authInterface;}
}
