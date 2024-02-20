package service;

import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;

public class ListGamesService {
    AuthDAO authDAO;
    GameDAO gameDAO;
    UserDAO userDAO;

    public ListGamesService(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
    }
}
