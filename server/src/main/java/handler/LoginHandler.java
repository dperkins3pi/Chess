package handler;

import com.google.gson.Gson;
import dataAccess.*;
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
        LoginRequest login_request = new Gson().fromJson(request.body(), LoginRequest.class);
        String username = login_request.getUsername();
        String password = login_request.getPassword();

        // Call service
        LoginService loginService = new LoginService(authDAO, gameDAO, userDAO);
        ResponseClass user = loginService.login(username, password);
        return new Gson().toJson(user);
    }
}
