package handler;

import com.google.gson.Gson;
import dataAccess.*;
import request.RegisterRequest;
import response.ResponseClass;
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
        RegisterRequest register_request = new Gson().fromJson(request.body(), RegisterRequest.class);
        String username = register_request.getUsername();
        String password = register_request.getPassword();
        String email = register_request.getEmail();

        // Call service
        RegisterService registerService = new RegisterService(authDAO, gameDAO, userDAO);
        ResponseClass res = null;
        try {
            res = registerService.register(username, password, email);
            response.status(200);  // It worked!!!!
        } catch (AlreadyTakenException e) {
            response.status(403);
            res = new ResponseClass(e.getMessage());
        } catch (Exception e){
            response.status(500);
            res = new ResponseClass(e.getMessage());
        }
        return new Gson().toJson(res);
    }
}
