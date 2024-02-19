package server;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.MemoryAuthDAO;
import dataAccess.GameDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.UserDAO;
import dataAccess.MemoryUserDAO;
import handler.ClearHandler;
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
        Spark.delete("/db", new ClearHandler(authDao, gameDao, userDao)::handleRequest);  //Clear

        Spark.init();
        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
