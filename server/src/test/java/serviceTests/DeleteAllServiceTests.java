package serviceTests;

import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import org.junit.jupiter.api.Test;
import service.DeleteAllService;

public class DeleteAllServiceTests {

  static final DeleteAllService service = new DeleteAllService();

  @Test
  void deleteAllDB() throws DataAccessException {

    service.deleteAllDB();
  }
}
