package handler;

import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;

public class JoinGameHandler {
    AuthDAO authDAO;
    GameDAO gameDAO;
    UserDAO userDAO;

    public JoinGameHandler(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
    }
}
