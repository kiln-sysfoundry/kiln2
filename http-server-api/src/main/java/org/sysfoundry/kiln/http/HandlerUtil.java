package org.sysfoundry.kiln.http;

import io.undertow.io.Receiver;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.BlockingHandler;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HandlerUtil {

    public static HttpHandler blocking(Receiver.FullStringCallback callback){
        return new BlockingHandler(httpServerExchange -> {
            httpServerExchange.getRequestReceiver().receiveFullString(callback,UTF_8);
        });
    }
}
