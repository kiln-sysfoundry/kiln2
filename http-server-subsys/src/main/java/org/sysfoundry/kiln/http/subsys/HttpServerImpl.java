package org.sysfoundry.kiln.http.subsys;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.RoutingHandler;
import lombok.NonNull;
import org.slf4j.Logger;
import org.sysfoundry.kiln.health.Log;
import org.sysfoundry.kiln.http.HttpHandlerRoute;
import org.sysfoundry.kiln.http.HttpServer;
import org.sysfoundry.kiln.http.HttpServerConfig;
import org.sysfoundry.kiln.sys.LifecycleException;

import java.util.Set;

import static org.sysfoundry.kiln.http.subsys.HttpSubsys.NAME;

class HttpServerImpl implements HttpServer {

    private static final Logger log = Log.get(NAME);

    private HttpServerConfig config;
    private Set<HttpHandlerRoute> allRoutes;
    private Undertow httpServer;

    public HttpServerImpl(@NonNull HttpServerConfig httpServerConfig,
                          @NonNull Set<HttpHandlerRoute> allRoutes) {
        this.config = httpServerConfig;
        this.allRoutes = allRoutes;
    }

    @Override
    public void start() throws LifecycleException {
        log.info("Starting http server @ {}:{}",config.getBindAddress(),config.getPort());
        httpServer = Undertow.builder()
                .addHttpListener(config.getPort(),config.getBindAddress())
                .setHandler(getRoutingHandler())
                .build();

        httpServer.start();
        log.info("http server started @{}:{}",config.getBindAddress(),config.getPort());
    }

    private HttpHandler getRoutingHandler() {
        RoutingHandler routingHandler = Handlers.routing();
        for (HttpHandlerRoute route : allRoutes) {
            if(route.getPredicate() == null) {
                routingHandler.add(route.getMethod(), route.getPathTemplate(), route.getHandler());
            }else{
                routingHandler.add(route.getMethod(),route.getPathTemplate(),route.getPredicate(),route.getHandler());
            }
        }

        return routingHandler;
    }

    @Override
    public void stop() throws LifecycleException {
        if(httpServer != null){
            httpServer.stop();
        }
    }
}
