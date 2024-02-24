package service;

import dataAccess.*;
import exceptions.BadRequestException;
import exceptions.UnauthorizedException;
import model.AuthData;
import model.GameData;

import java.util.Collection;
import java.util.Map;

public class CreateGameService {
    AuthDAO authDAO;
    GameDAO gameDAO;
    UserDAO userDAO;

    public CreateGameService(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
    }

    public Integer createGame(String authToken, String gameName) throws DataAccessException, UnauthorizedException, BadRequestException {
        Map<String, AuthData> authTokens = authDAO.getAuthTokens();
        AuthData auth = authTokens.get(authToken);

        if(auth == null){  // If the given authtoken is not in the list
            throw new UnauthorizedException("Error: unauthorized");
        }
        if(gameName == null){  // If invalid input is given
            throw new BadRequestException("Error: bad request");
        }
        return gameDAO.createGame(gameName);   // Return the id of the game created
    }
}
