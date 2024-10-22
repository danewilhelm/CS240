package handler;

import com.google.gson.Gson;
import result.ClearResult;
import service.ClearService;
import spark.Request;
import spark.Response;

public class ClearHandler {
    /*
    Handles 1 endpoint: clear
     */

    public static Object handleClear(Request request, Response response) {
        Gson serializer = new Gson();
//        var gsonClearRequest = serializer.fromJson(request, Clear)
        ClearResult resultObj = ClearService.clear();
        response.status(200);
        return serializer.toJson(resultObj);
    }
}
