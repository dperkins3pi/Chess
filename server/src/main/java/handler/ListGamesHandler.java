package handler;

import com.google.gson.Gson;
import java.util.Collection;
import dataAccess.*;
import exceptions.UnauthorizedException;
import model.GameData;
import response.GameResponseClass;
import service.ListGamesService;
import spark.Request;
import spark.Response;

public class ListGamesHandler {
    AuthDAO authDAO;
    GameDAO gameDAO;
    UserDAO userDAO;

    public ListGamesHandler(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
    }

    public Object handle(Request request, Response response) throws DataAccessException {
        // Get data from request
        String authToken = request.headers("Authorization");

        // Call service
        ListGamesService listGamesService = new ListGamesService(authDAO, gameDAO, userDAO);
        GameResponseClass res = null;
        try {
            Collection<GameData> games = listGamesService.listGames(authToken);
            res = new GameResponseClass(games);
            response.status(200);
            return new Gson().toJson(res);
        } catch (UnauthorizedException e) {
            res = new GameResponseClass(e.getMessage());
            response.status(401);
            return new Gson().toJson(res);
        } catch (Exception e) {
            res = new GameResponseClass(e.getMessage());
            response.status(500);
            return new Gson().toJson(res);
        }
    }
}
