package service;
import dataAccess.*;

public class ClearService {
    AuthDAO authDAO;
    GameDAO gameDAO;
    UserDAO userDAO;
    public ClearService(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO){
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
    }
    public void clear() throws DataAccessException {
        // Clear tha DAOs
        this.authDAO.clear();
        this.gameDAO.clear();
        this.userDAO.clear();
    }
}
