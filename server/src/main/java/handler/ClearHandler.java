package handler;
import dataAccess.*;
import response.ResponseClass;
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
        // Call clear service
        Object res = "{}";
        try {
            ClearService clearService = new ClearService(authDAO, gameDAO, userDAO);
            clearService.clear();
            response.status(200);
            return res;  // Return empty JSON
        } catch (Exception e){
            res = new ResponseClass(e.getMessage());
            response.status(500);
            return res;
        }
    }
}
