package handler;

import com.google.gson.Gson;
import dataAccess.*;
import exceptions.BadRequestException;
import exceptions.UnauthorizedException;
import request.CreateGameRequest;
import response.ResponseClass;
import service.CreateGameService;
import spark.Request;
import spark.Response;

public class CreateGameHandler {
    AuthDAO authDAO;
    GameDAO gameDAO;
    UserDAO userDAO;

    public CreateGameHandler(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
    }
    public Object handle(Request request, Response response) throws DataAccessException {
        // Get data from request
        String authToken = request.headers("Authorization");
        CreateGameRequest gameRequest = new Gson().fromJson(request.body(), CreateGameRequest.class);
        String gameName = gameRequest.getGameName();

        // Call service
        CreateGameService createGameService = new CreateGameService(authDAO, gameDAO, userDAO);
        ResponseClass res = null;
        try {  // Catch all errors
            int id = createGameService.createGame(authToken, gameName);
            res = new ResponseClass(id);
            response.status(200);
            return new Gson().toJson(res);
        } catch (UnauthorizedException e) {
            res = new ResponseClass(e.getMessage());
            response.status(401);
            return new Gson().toJson(res);
        } catch (BadRequestException e) {
            response.status(400);
            res = new ResponseClass(e.getMessage());
            return new Gson().toJson(res);
        } catch (Exception e) {
            res = new ResponseClass(e.getMessage());
            response.status(500);
            return new Gson().toJson(res);
        }
    }
}
