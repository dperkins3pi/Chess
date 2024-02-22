package service;

import dataAccess.*;
import handler.AlreadyTakenException;
import model.UserData;
import response.ResponseClass;

public class RegisterService {
    AuthDAO authDAO;
    GameDAO gameDAO;
    UserDAO userDAO;
    public RegisterService(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO){
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
    }
    public ResponseClass register(String username, String password, String email) throws DataAccessException, AlreadyTakenException {
        UserData user = userDAO.getUser(username);
        if(user == null){   //If the user does not already exist
            userDAO.createUser(username, password, email);   // Create the user and store it
            String authToken = authDAO.createAuth(username);
            return new ResponseClass(username, authToken);    // Return response object
        }
        else{
            throw new AlreadyTakenException("Error: already taken");
        }
    }
}
