package handler;

import com.google.gson.Gson;
import dataAccess.*;
import exceptions.AlreadyTakenException;
import exceptions.BadRequestException;
import exceptions.UnauthorizedException;
import request.LoginRequest;
import response.ResponseClass;
import service.LoginService;
import service.RegisterService;
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
        LoginRequest login_request = new Gson().fromJson(request.body(), LoginRequest.class);
        String username = login_request.getUsername();
        String password = login_request.getPassword();

        // Call service
        LoginService loginService = new LoginService(authDAO, gameDAO, userDAO);

        ResponseClass res = null;
        try {
            res = loginService.login(username, password);
            response.status(200);
        } catch (UnauthorizedException e) {
            res = new ResponseClass(e.getMessage());
            response.status(401);
        } catch (Exception e) {
            res = new ResponseClass(e.getMessage());
            response.status(500);
        }
        return new Gson().toJson(res);
    }
}