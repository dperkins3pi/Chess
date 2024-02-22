package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import exceptions.UnauthorizedException;
import model.UserData;
import response.ResponseClass;

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
