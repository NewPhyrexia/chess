package dataAccess;
import model.UserData;
public interface UserDAOInterface {

  UserData addUser(UserData user) throws DataAccessException;

  UserData getUser(String username) throws DataAccessException;

  void deleteUser(String username) throws DataAccessException;

  void deleteAllUsers()  throws DataAccessException;
}
