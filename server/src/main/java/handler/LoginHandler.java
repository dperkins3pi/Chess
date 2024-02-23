package handler;

import com.google.gson.Gson;
import dataAccess.*;
import exceptions.UnauthorizedException;
import request.LoginRequest;
import response.ResponseClass;
import service.LoginService;
import spark.Request;
import spark.Response;

public class LoginHandler {
    AuthDAO authDAO;
    GameDAO gameDAO;
    UserDAO userDAO;

    public LoginHandler(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
    }

    public Object handle(Request request, Response response) throws DataAccessException {
        // Get data from request
        LoginRequest loginRequest = new Gson().fromJson(request.body(), LoginRequest.class);
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        // Call service
        LoginService loginService = new LoginService(authDAO, gameDAO, userDAO);

        ResponseClass res = null;
        try {   // Catch all errors
            res = loginService.login(username, password);
            response.status(200);
            return new Gson().toJson(res);
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