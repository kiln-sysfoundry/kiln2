package org.sysfoundry.kiln.http.subsys;

import io.undertow.security.api.AuthenticationMechanism;
import io.undertow.security.api.AuthenticationMode;
import io.undertow.security.handlers.AuthenticationCallHandler;
import io.undertow.security.handlers.AuthenticationConstraintHandler;
import io.undertow.security.handlers.AuthenticationMechanismsHandler;
import io.undertow.security.handlers.SecurityInitialHandler;
import io.undertow.security.idm.Account;
import io.undertow.security.idm.Credential;
import io.undertow.security.idm.IdentityManager;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.BlockingHandler;
import org.sysfoundry.kiln.security.jwt.JWTConfig;

import java.util.ArrayList;
import java.util.List;

public class SecurityUtil {

    private static IdentityManager oidcIdManager = new OidcIdentityManager();

    public static HttpHandler securedByOidcToken(HttpHandler handlerToBeSecured, JWTConfig jwtConfig){
        HttpHandler handler = handlerToBeSecured;
        handler = new AuthenticationCallHandler(handler);
        handler = new AuthenticationConstraintHandler(handler);
        handler = new AuthenticationMechanismsHandler(handler,getOidcAuthenticationMechanisms(jwtConfig.getPublicKey()
                ,jwtConfig.getPrefix(),jwtConfig.getSignatureAlgorithm()));
        handler = new SecurityInitialHandler(AuthenticationMode.PRO_ACTIVE,getOidcIdentityManager(),handler);
        handler = new BlockingHandler(handler);
        return handler;
    }

    private static IdentityManager getOidcIdentityManager() {
        return oidcIdManager;
    }

    private static List<AuthenticationMechanism> getOidcAuthenticationMechanisms(String publicKey,String prefix,String signatureAlgorithm) {
        List<AuthenticationMechanism> mechanisms = new ArrayList<>();

        mechanisms.add(new OpenIDConnectDirectAuthMechanism(publicKey,prefix,signatureAlgorithm));
        return mechanisms;
    }

    static class OidcIdentityManager implements IdentityManager{

        @Override
        public Account verify(Account account) {
            return account;
        }

        @Override
        public Account verify(String s, Credential credential) {
            return null;
        }

        @Override
        public Account verify(Credential credential) {
            return null;
        }
    }



}
