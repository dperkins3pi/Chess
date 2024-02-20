package service;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import spark.*;
import com.google.gson.Gson;

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
        this.authDAO.clear();
        this.gameDAO.clear();
        this.userDAO.clear();
    }
}
