package org.sysfoundry.kiln.http.subsys;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.MalformedJwtException;
import io.undertow.security.api.AuthenticationMechanism;
import io.undertow.security.api.SecurityContext;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.*;
import org.slf4j.Logger;
import org.sysfoundry.kiln.health.Log;
import org.sysfoundry.kiln.security.jwt.Util;

import java.util.*;

import static org.sysfoundry.kiln.http.subsys.HttpSubsys.NAME;

public class OpenIDConnectDirectAuthMechanism implements AuthenticationMechanism {

    public static final String INVALID_TOKEN = "INVALID_TOKEN";
    public static final String NO_TOKEN_IN_REQUEST = "NO_TOKEN_IN_REQUEST";
    public static final String EXPIRED_TOKEN = "EXPIRED_TOKEN";
    public static final String MALFORMED_TOKEN = "MALFORMED_TOKEN";
    public static final String BEARER_MISSING = "BEARER_MISSING";

    private String prefix;
    private String publicKey;
    private String signatureAlgorithm;

    private static final Logger log = Log.get(NAME);
    private static final AttachmentKey<String> AUTHENTICATION_RESULT_KEY = AttachmentKey.create(String.class);

    private static final ChallengeResult SENT = new ChallengeResult(true);

    public OpenIDConnectDirectAuthMechanism(String publicKey,
                                            String prefix,
                                            String signatureAlgorithm){
        Objects.nonNull(publicKey);
        Objects.nonNull(prefix);
        Objects.nonNull(signatureAlgorithm);
        this.publicKey = publicKey;
        this.prefix = prefix;
        this.signatureAlgorithm = signatureAlgorithm;
    }


    @Override
    public AuthenticationMechanismOutcome authenticate(HttpServerExchange httpServerExchange, SecurityContext securityContext) {

        HeaderMap requestHeaders = httpServerExchange.getRequestHeaders();

        if(requestHeaders.contains(Headers.AUTHORIZATION)){

            HeaderValues authorizationHeader = requestHeaders.get(Headers.AUTHORIZATION);
            String authorizationHeaderFirst = authorizationHeader.getFirst();

            if(!authorizationHeaderFirst.startsWith(prefix)){
                httpServerExchange.putAttachment(AUTHENTICATION_RESULT_KEY, BEARER_MISSING);
                return AuthenticationMechanismOutcome.NOT_AUTHENTICATED;
            }

            String accessToken = authorizationHeaderFirst.replaceFirst(prefix,"");

            accessToken = accessToken.trim();


            try {

                Jws<Claims> claimsJws = Util.parseAccessToken(publicKey, accessToken, signatureAlgorithm);

                //if we are here means the authentication is successful

                //now lets create the user account along with relevant information

                DefaultUserAccount userAccount = constructUserAccount(claimsJws);

                securityContext.authenticationComplete(userAccount,getClass().getName(),false);

                return AuthenticationMechanismOutcome.AUTHENTICATED;

            }catch(ExpiredJwtException e) {

                httpServerExchange.putAttachment(AUTHENTICATION_RESULT_KEY, EXPIRED_TOKEN);
                return AuthenticationMechanismOutcome.NOT_AUTHENTICATED;

            }catch(MalformedJwtException e){

                httpServerExchange.putAttachment(AUTHENTICATION_RESULT_KEY, MALFORMED_TOKEN);

                return AuthenticationMechanismOutcome.NOT_AUTHENTICATED;

            }catch(Exception e){
                //e.printStackTrace();
                httpServerExchange.putAttachment(AUTHENTICATION_RESULT_KEY, INVALID_TOKEN);

                return AuthenticationMechanismOutcome.NOT_AUTHENTICATED;
            }
        }else{
            httpServerExchange.putAttachment(AUTHENTICATION_RESULT_KEY, NO_TOKEN_IN_REQUEST);
            //so authorization header available so nothing can be done
            return AuthenticationMechanismOutcome.NOT_ATTEMPTED;
        }

    }


    private DefaultUserAccount constructUserAccount(Jws<Claims> claimsJws) {

        Claims claims = claimsJws.getBody();

        String subject = claims.getSubject();
        String name = claims.get("name",String.class);
        String preferredName = claims.get("preferred_username",String.class);
        String givenName = claims.get("given_name",String.class);
        boolean emailVerified = claims.get("email_verified",Boolean.class);
        String scopeStr = claims.get("scope",String.class);

        String[] scopesArray = scopeStr.split(" ");

        Set<String> scopes = new HashSet<>(Arrays.asList(scopesArray));

        Map<String,Object> attributes = getAttributes(claims.get("user_attributes",Map.class));

        Set<String> roles = getRoles(claims.get("resource_access",Map.class));
        DefaultUserAccount account = new DefaultUserAccount(name,preferredName,givenName,emailVerified,scopes,roles,attributes);

        return account;
    }

    private Set<String> getRoles(Map resourceAccessMap) {
        Set<String> roleSet = new LinkedHashSet<>();
        if(resourceAccessMap != null){
            resourceAccessMap.forEach((k,v)->{
                String resourceName = k.toString();
                Map resourceAccessMapNested = (Map)v;
                if(resourceAccessMapNested.containsKey("roles")){
                    List roleObjects = (List)resourceAccessMapNested.get("roles");
                    roleObjects.forEach(obj->{
                        roleSet.add(String.format("%s/%s",resourceName,obj.toString()));
                    });
                }
            });
        }
        return roleSet;
    }

    private Map<String, Object> getAttributes(Map userAttributes) {
        Map<String,Object> retVal = new HashMap<>();
        if(userAttributes != null){
            userAttributes.forEach((k,v)->{
                retVal.put(k.toString(),v);
            });
        }
        return retVal;
    }

    @Override
    public ChallengeResult sendChallenge(HttpServerExchange httpServerExchange, SecurityContext securityContext) {

        String authenticationResult = httpServerExchange.getAttachment(AUTHENTICATION_RESULT_KEY);

        String realmString = String.format("realm=\"%s\"",httpServerExchange.getHostAndPort());

        log.debug("Authentication Result {}",authenticationResult);
        if(authenticationResult != null){
            if(authenticationResult.equalsIgnoreCase(NO_TOKEN_IN_REQUEST)){
                httpServerExchange.getResponseHeaders().put(Headers.WWW_AUTHENTICATE,String.format("Bearer %s",realmString));
                return new ChallengeResult(true,StatusCodes.UNAUTHORIZED);
            }else if(authenticationResult.equalsIgnoreCase(INVALID_TOKEN)){
                String errorString = "error=\"invalid_token\"";
                String errorDescString = "error_description=\"unable to parse the access token\"";
                httpServerExchange.getResponseHeaders().put(Headers.WWW_AUTHENTICATE,String.format("Bearer %s,%s,%s",
                        realmString,errorString,errorDescString));

                return new ChallengeResult(true, StatusCodes.UNAUTHORIZED);
            }else if(authenticationResult.equalsIgnoreCase(EXPIRED_TOKEN)){
                String errorString = "error=\"invalid_token\"";
                String errorDescString = "error_description=\"The access token expired\"";
                httpServerExchange.getResponseHeaders().put(Headers.WWW_AUTHENTICATE,String.format("Bearer %s,%s,%s",
                        realmString,errorString,errorDescString));

                return new ChallengeResult(true, StatusCodes.UNAUTHORIZED);
            }else if(authenticationResult.equalsIgnoreCase(MALFORMED_TOKEN)){
                String errorString = "error=\"invalid_token\"";
                String errorDescString = "error_description=\"The access token is malformed\"";
                httpServerExchange.getResponseHeaders().put(Headers.WWW_AUTHENTICATE,String.format("Bearer %s,%s,%s",
                        realmString,errorString,errorDescString));

                return new ChallengeResult(true, StatusCodes.UNAUTHORIZED);
            }else if(authenticationResult.equalsIgnoreCase(BEARER_MISSING)){
                String errorString = "error=\"invalid_token\"";
                String errorDescString = String.format("error_description=\"%s is missing in the Authorization header\"",prefix);
                httpServerExchange.getResponseHeaders().put(Headers.WWW_AUTHENTICATE,String.format("Bearer %s,%s,%s",
                        realmString,errorString,errorDescString));

                return new ChallengeResult(true, StatusCodes.UNAUTHORIZED);
            }
        }
        return ChallengeResult.NOT_SENT;
    }
}
