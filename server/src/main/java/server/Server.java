package server;

import dataaccess.*;
import handler.ClearHandler;
import handler.GameHandler;
import handler.UserHandler;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // create instances of DAOs
        AuthDAO authDAOInstance = new AuthMemoryDAO();
        GameDAO gameDAOInstance = new GameMemoryDAO();
        UserDAO userDAOInstance = new UserDatabaseDAO();

        // create instances of Handlers
        ClearHandler clearHandlerInstance = new ClearHandler(authDAOInstance, gameDAOInstance, userDAOInstance);
        GameHandler gameHandlerInstance = new GameHandler(authDAOInstance, gameDAOInstance, userDAOInstance);
        UserHandler userHandlerInstance = new UserHandler(authDAOInstance, gameDAOInstance, userDAOInstance);

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", userHandlerInstance::handleRegister);
        Spark.post("/session", userHandlerInstance::handleLogin);
        Spark.delete("/session", userHandlerInstance::handleLogout);

        Spark.get("/game", gameHandlerInstance::handleListGames);
        Spark.post("/game", gameHandlerInstance::handleCreateGame);
        Spark.put("/game", gameHandlerInstance::handleJoinGame);

        Spark.delete("/db", clearHandlerInstance::handleClear);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
