package org.sysfoundry.kiln.http.subsys;

import io.undertow.io.Receiver;
import io.undertow.server.HttpHandler;
import io.undertow.util.StatusCodes;
import org.sysfoundry.kiln.health.Status;

import static java.nio.charset.StandardCharsets.UTF_8;

class HealthCheckController {


    private Status status;

    HealthCheckController(Status status){
        this.status = status;
    }

    HttpHandler getHealthCheckHandler(){

        Receiver.FullStringCallback fullStringCallback = (exchange, payload)->{

            if(status.get() == Status.State.FAILED){

                exchange.setStatusCode(StatusCodes.INTERNAL_SERVER_ERROR);

            }else if(status.get() == Status.State.WARNING){

                exchange.setStatusCode(StatusCodes.TOO_MANY_REQUESTS);

            }else{

                exchange.setStatusCode(StatusCodes.OK);
            }

            exchange.endExchange();
        };

        return httpServerExchange -> {
          httpServerExchange.getRequestReceiver().receiveFullString(fullStringCallback,UTF_8);
        };
    }
}
