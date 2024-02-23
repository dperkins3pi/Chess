package server;

import com.google.gson.Gson;
import dataAccess.*;
import handler.*;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);
        Spark.staticFiles.location("web");

        // Create DAOs
        AuthDAO authDao = new MemoryAuthDAO();
        GameDAO gameDao = new MemoryGameDAO();
        UserDAO userDao = new MemoryUserDAO();

        // Register your endpoints and handle exceptions here.   //HELP
        Spark.delete("/db", new ClearHandler(authDao, gameDao, userDao)::handle);  //Clear
        Spark.post("/user", new RegisterHandler(authDao, gameDao, userDao)::handle);   //Register
        Spark.post("/session", new LoginHandler(authDao, gameDao, userDao)::handle);  //Login
        Spark.delete("/session", new LogoutHandler(authDao, gameDao, userDao)::handle);  //Logout
        Spark.get("/game", new ListGamesHandler(authDao, gameDao, userDao)::handle);   //

        Spark.init();
        Spark.awaitInitialization();
        return Spark.port();
    }
    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
