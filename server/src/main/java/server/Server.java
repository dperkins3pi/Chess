package server;

import com.google.gson.Gson;
import model.AuthData;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);
        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
//        Spark.delete("/db", this::clear);    // HOW DO I MAKE IT UTILIZE THE CLEAR HANDLER CLASS??????

        Spark.init();
        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }


    // DELETE STUFF BELOW HERE
//    private Object clear(Request req, Response res) {
//        var auth = new Gson().fromJson(req.body(), AuthData.class);
//        return new Gson().toJson(auth);
//    }
}
