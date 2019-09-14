package org.sysfoundry.kiln.http;

import io.undertow.predicate.Predicate;
import io.undertow.server.HttpHandler;
import lombok.Value;

@Value
public class HttpHandlerRoute {

    private String method;
    private String pathTemplate;
    private Predicate predicate;
    private HttpHandler handler;

}
