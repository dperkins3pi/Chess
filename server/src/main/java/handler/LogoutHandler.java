package handler;

import com.google.gson.Gson;
import dataAccess.*;
import exceptions.UnauthorizedException;
import response.ResponseClass;
import service.LogoutService;
import spark.Request;
import spark.Response;

public class LogoutHandler {
    AuthDAO authDAO;
    GameDAO gameDAO;
    UserDAO userDAO;

    public LogoutHandler(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
    }

    public Object handle(Request request, Response response) throws DataAccessException {
        // Get data from request
        String authToken = request.headers("Authorization");

        // Call service
        LogoutService logoutService = new LogoutService(authDAO, gameDAO, userDAO);
        ResponseClass res = null;
        try {   // Catch all errors
            logoutService.logout(authToken);
            response.status(200);
            return "{}";
        } catch (UnauthorizedException e) {
            res = new ResponseClass(e.getMessage());
            response.status(401);
            return new Gson().toJson(res);
        } catch (Exception e) {
            res = new ResponseClass(e.getMessage());
            response.status(500);
            return new Gson().toJson(res);
        }
    }
}
