package handler;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
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
    public Object handleRequest(Request request, Response response) {
        // HELP!!!!!!!!!
        return null;
    }
}
