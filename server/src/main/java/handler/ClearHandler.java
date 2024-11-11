package handler;

import com.google.gson.Gson;
import result.ClearResult;
import service.ClearService;
import service.UnauthorizedException;
import spark.Request;
import spark.Response;
import dataaccess.*;

public class ClearHandler {
    /*
    Handles 1 endpoint: clear
     */

    private final ClearService clearServiceInstance;

    public ClearHandler (AuthDAO authDAOInstance, GameDAO gameDAOInstance, UserDAO userDAOInstance) {
        clearServiceInstance = new ClearService(authDAOInstance, gameDAOInstance, userDAOInstance);
    }


    public Object handleClear(Request request, Response response) {
        try {
            Gson serializer = new Gson();
            ClearResult resultObj = clearServiceInstance.clear();
            response.status(200);
            return serializer.toJson(resultObj);
        } catch (DataAccessException e) {
            response.status(500);
            return "{ \"message\": \"" + e.getMessage() + "\" }";
        }
    }
}
