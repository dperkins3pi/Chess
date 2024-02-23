package service;

import dataAccess.*;
import java.util.Collection;
import exceptions.UnauthorizedException;
import model.GameData;

public class ListGamesService {
    AuthDAO authDAO;
    GameDAO gameDAO;
    UserDAO userDAO;

    public ListGamesService(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
    }

    public Collection<GameData> listGames(String authToken) throws DataAccessException, UnauthorizedException {
        if(authDAO.getAuth(authToken) == null){  // If the given authtoken is not in the list
            throw new UnauthorizedException("Error: unauthorized");
        }
        return gameDAO.listGames();
    }
}
