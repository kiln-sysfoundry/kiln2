package org.sysfoundry.kiln.http.subsys;

import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderValues;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;
import io.undertow.util.StatusCodes;

import static org.sysfoundry.kiln.http.HttpHeaders.X_STATUS_REASON;

public class ResponseUtil {


    public static void sendCreated(HttpServerExchange exchange,String location){
        exchange.setStatusCode(StatusCodes.CREATED);
        exchange.getResponseHeaders().put(Headers.LOCATION,location);
        exchange.endExchange();
    }

    public static void sendOKJSON(HttpServerExchange exchange,String responseMessage){
        exchange.setStatusCode(StatusCodes.OK);
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE,"application/json");
        exchange.getResponseSender().send(responseMessage);
        exchange.endExchange();
    }

    public static void sendOK(HttpServerExchange exchange,String contentType,String responseMessage){
        exchange.setStatusCode(StatusCodes.OK);
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, contentType);
        exchange.getResponseSender().send(responseMessage);
        exchange.endExchange();
    }


    public static void sendBadRequest(HttpServerExchange exchange,Exception cause){
        exchange.setStatusCode(StatusCodes.CREATED);
        exchange.getResponseHeaders().put(new HttpString(X_STATUS_REASON),cause.getMessage());
        exchange.endExchange();
    }

    public static void sendServerError(HttpServerExchange exchange,Exception cause){
        exchange.setStatusCode(StatusCodes.INTERNAL_SERVER_ERROR);
        exchange.endExchange();
    }

    public static void sendConflictError(HttpServerExchange exchange,Exception cause){
        exchange.setStatusCode(StatusCodes.CONFLICT);
        exchange.getResponseHeaders().put(new HttpString(X_STATUS_REASON),cause.getMessage());
        exchange.endExchange();
    }


}
