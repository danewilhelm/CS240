package server;

import handler.ClearHandler;
import handler.GameHandler;
import handler.UserHandler;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // create instances of DAOs
        // 1 2 3

        // create instances of Handlers


        // Register your endpoints and handle exceptions here.
        establishEndpoints();
        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private void establishEndpoints() {
        Spark.post("/user", UserHandler::handleRegister);
        Spark.post("/session", UserHandler::handleLogin);
        Spark.delete("/session", UserHandler::handleLogout);

        Spark.get("/game", GameHandler::handleListGames);
        Spark.post("/game", GameHandler::handleCreateGame);
        Spark.put("/game", GameHandler::handleJoinGame);

        Spark.delete("/db", ClearHandler::handleClear);


    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
