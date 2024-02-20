package handler;

import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;

public class ListGamesHandler {
    AuthDAO authDAO;
    GameDAO gameDAO;
    UserDAO userDAO;

    public ListGamesHandler(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
    }
}
