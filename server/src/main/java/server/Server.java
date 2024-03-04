package server;

import dataAccess.*;
import handler.*;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);
        Spark.staticFiles.location("web");

        // Create DAOs
        AuthDAO authDao;
        GameDAO gameDao;
        UserDAO userDao;
        try{
            authDao = new DatabaseAuthDAO();
            userDao = new DatabaseUserDAO();
            gameDao = new DatabaseGameDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        // Register your endpoints and handle exceptions here.   //HELP
        Spark.delete("/db", new ClearHandler(authDao, gameDao, userDao)::handle);  //Clear
        Spark.post("/user", new RegisterHandler(authDao, gameDao, userDao)::handle);   //Register
        Spark.post("/session", new LoginHandler(authDao, gameDao, userDao)::handle);  //Login
        Spark.delete("/session", new LogoutHandler(authDao, gameDao, userDao)::handle);  //Logout
        Spark.get("/game", new ListGamesHandler(authDao, gameDao, userDao)::handle);   //ListGame
        Spark.post("/game", new CreateGameHandler(authDao, gameDao, userDao)::handle);  //CreateGame
        Spark.put("/game", new JoinGameHandler(authDao, gameDao, userDao)::handle);  //JoinGame

        Spark.init();
        Spark.awaitInitialization();
        return Spark.port();
    }
    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
