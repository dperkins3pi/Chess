package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;

public class RegisterService {
    AuthDAO authDAO;
    GameDAO gameDAO;
    UserDAO userDAO;
    public RegisterService(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO){
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
    }
    public void register(String username, String password, String email) throws DataAccessException {
        ;
        // DO STUFF
    }
}
