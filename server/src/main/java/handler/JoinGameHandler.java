package handler;

import com.google.gson.Gson;
import dataAccess.*;
import exceptions.AlreadyTakenException;
import exceptions.BadRequestException;
import exceptions.UnauthorizedException;
import request.JoinGameRequest;
import response.ResponseClass;
import service.JoinGameService;
import spark.Request;
import spark.Response;

public class JoinGameHandler {
    AuthDAO authDAO;
    GameDAO gameDAO;
    UserDAO userDAO;

    public JoinGameHandler(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
    }

    public Object handle(Request request, Response response) {
        // Get data from request
        String authToken = request.headers("Authorization");
        JoinGameRequest gameRequest = new Gson().fromJson(request.body(), JoinGameRequest.class);
        String playerColor = gameRequest.getPlayerColor();
        Integer gameID = gameRequest.getGameID();

        // Call service
        JoinGameService joinGameService = new JoinGameService(authDAO, gameDAO, userDAO);
        ResponseClass res = null;
        try {  // Catch all errors
            joinGameService.joinGame(authToken, playerColor, gameID);
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
        } catch (AlreadyTakenException e) {
            response.status(403);
            res = new ResponseClass(e.getMessage());
            return new Gson().toJson(res);
        } catch (Exception e) {
            res = new ResponseClass(e.getMessage());
            response.status(500);
            return new Gson().toJson(res);
        }
    }
}
