package handler;
import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import service.ClearService;
import spark.*;

public class ClearHandler{
    AuthDAO authDAO;
    GameDAO gameDAO;
    UserDAO userDAO;
    public ClearHandler(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO){
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
    }
    public Object handle(Request request, Response response) throws DataAccessException {
        ClearService clearService = new ClearService(authDAO, gameDAO, userDAO);
        clearService.clear();
        return "{}";
    }
}
