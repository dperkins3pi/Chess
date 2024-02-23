package service;

import dataAccess.*;
import exceptions.UnauthorizedException;
import model.UserData;

public class LogoutService {
    AuthDAO authDAO;
    GameDAO gameDAO;
    UserDAO userDAO;

    public LogoutService(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
    }

    public void logout(String authToken) throws DataAccessException, UnauthorizedException {
        if(authDAO.getAuth(authToken) == null){  // If the given authtoken is not in the list
            throw new UnauthorizedException("Error: unauthorized");
        }
        authDAO.deleteAuth(authToken);
    }
}
