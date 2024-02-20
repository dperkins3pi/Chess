package service;

import dataAccess.*;
import model.UserData;
import response.RegisterResponse;

public class RegisterService {
    AuthDAO authDAO;
    GameDAO gameDAO;
    UserDAO userDAO;
    public RegisterService(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO){
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
    }
    public RegisterResponse register(String username, String password, String email) throws DataAccessException {
        UserData user = userDAO.getUser(username);
        if(user == null){   //If the user does not already exist
            userDAO.createUser(username, password, email);   // Create the user and store it
            String authToken = authDAO.createAuth(username);
            return new RegisterResponse(username, authToken);    // Return response object
        }
        return null;
    }
}
