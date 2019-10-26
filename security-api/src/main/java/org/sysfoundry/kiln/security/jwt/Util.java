package org.sysfoundry.kiln.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Objects;

public class Util {

    public static final Logger log = LoggerFactory.getLogger("jws-util");


    public static Jws<Claims> parseAccessToken(String publicKey,String accessToken,String signatureAlgorithm)
            throws IOException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException {
        Objects.nonNull(publicKey);
        Objects.nonNull(accessToken);
        Objects.nonNull(signatureAlgorithm);

        log.debug("-----------JWT----------");
        log.debug("Public Key : {}",publicKey);
        log.debug("Access Key : {}",accessToken);

        PublicKey publicKeyObj = null;
            publicKeyObj = decodePublicKey(pemToDer(publicKey),signatureAlgorithm);

        Jws<Claims> claimsJws = Jwts.parser() //
                .setSigningKey(publicKeyObj) //
                .parseClaimsJws(accessToken);

        return claimsJws;

    }

    /**
     * Decode a PEM string to DER format
     *
     * @param pem
     * @return
     * @throws java.io.IOException
     */
    public static byte[] pemToDer(String pem) throws IOException {
        return Base64.getDecoder().decode(stripBeginEnd(pem));
    }

    public static String stripBeginEnd(String pem) {

        String stripped = pem.replaceAll("-----BEGIN (.*)-----", "");
        stripped = stripped.replaceAll("-----END (.*)----", "");
        stripped = stripped.replaceAll("\r\n", "");
        stripped = stripped.replaceAll("\n", "");

        return stripped.trim();
    }

    public static PublicKey decodePublicKey(byte[] der,String signatureAlgorithm) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {

        X509EncodedKeySpec spec = new X509EncodedKeySpec(der);

        KeyFactory kf = KeyFactory.getInstance(signatureAlgorithm);

        return kf.generatePublic(spec);
    }

}
