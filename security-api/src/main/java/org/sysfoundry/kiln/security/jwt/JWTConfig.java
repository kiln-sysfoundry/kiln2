package org.sysfoundry.kiln.security.jwt;

import lombok.Data;

@Data
public class JWTConfig {

    private String publicKey;
    private String prefix;
    private String signatureAlgorithm;

}
