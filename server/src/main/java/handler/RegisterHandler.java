package handler;

import com.google.gson.Gson;
import dataAccess.*;
import service.RegisterService;
import spark.Request;
import spark.Response;

public class RegisterHandler {
    AuthDAO authDAO;
    GameDAO gameDAO;
    UserDAO userDAO;
    public RegisterHandler(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO){
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
    }
    public Object handle(Request request, Response response) throws DataAccessException {
        // Get data from request
        var register_request = new Gson().fromJson(request.body(), RegisterRequest.class);
        String username = register_request.getUsername();
        String password = register_request.getPassword();
        String email = register_request.getEmail();

        // Call service
        RegisterService registerService = new RegisterService(authDAO, gameDAO, userDAO);
        registerService.register(username, password, email);
        return "{}";
    }
}
