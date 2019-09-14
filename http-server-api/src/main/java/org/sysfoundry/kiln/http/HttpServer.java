package org.sysfoundry.kiln.http;

import org.sysfoundry.kiln.sys.LifecycleException;

public interface HttpServer {

    public void start() throws LifecycleException;

    public void stop() throws LifecycleException;
}
