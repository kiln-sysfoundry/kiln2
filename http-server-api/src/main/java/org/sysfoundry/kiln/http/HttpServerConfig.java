package org.sysfoundry.kiln.http;

import lombok.Data;

@Data
public class HttpServerConfig {

    private String bindAddress;
    private int port;
}
