package org.sysfoundry.kiln.http.subsys;

import org.slf4j.Logger;
import org.sysfoundry.kiln.factory.FactoryException;
import org.sysfoundry.kiln.health.Log;
import org.sysfoundry.kiln.http.HttpServer;
import org.sysfoundry.kiln.http.HttpSubsysConfig;
import org.sysfoundry.kiln.sys.BaseSubsys;
import org.sysfoundry.kiln.sys.LifecycleException;

public class HttpSubsys extends BaseSubsys<HttpSubsysConfig> {

    public static final String NAME = "http-subsys";
    private static final Logger log = Log.get(NAME);

    public static final String HEALTH_CHECK_ENDPOINT = "/health/status";

    public HttpSubsys() {
        super(HttpSubsysFactory.class, HttpSubsysConfig.class,NAME);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void start() throws LifecycleException {
        try {
            HttpServer server = lookup(HttpServer.class);
            //log.info("Starting http server {}",server);
            server.start();
        } catch (FactoryException e) {
            throw new LifecycleException(e);
        }
    }

    @Override
    public void stop() throws LifecycleException {
        try {
            HttpServer server = lookup(HttpServer.class);
            log.info("Stopping http server {}",server);
            server.stop();
        } catch (FactoryException e) {
            throw new LifecycleException(e);
        }
    }
}
