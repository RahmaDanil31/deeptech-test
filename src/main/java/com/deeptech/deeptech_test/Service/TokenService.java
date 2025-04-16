package com.deeptech.deeptech_test.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class TokenService {

    private final Set<String> blacklistedTokens = new HashSet<>();

    Algorithm algorithm = Algorithm.HMAC256("secret");
    private static final long expirationTimeMillis = 3600000;

    public String createToken(String issuer) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expirationTimeMillis);

        try {
            return JWT.create()
                    .withIssuer(issuer)
                    .withExpiresAt(expirationDate)
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            return exception.getMessage();
        }

    }

    public Boolean verifyToken(String token, String issuer) {

        if (blacklistedTokens.contains(token)) {
            return false;
        }

        try {
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException exception) {
            return false;
        }
    }

    public String getIssuer(String token) {

        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getIssuer();
        } catch (JWTDecodeException exception) {
            return null;
        }
    }

    public String refreshToken(String token) {
        String issuer = getIssuer(token);
        return createToken(issuer);
    }

    public void logout(String token) {
        String issuer = getIssuer(token);
        if(issuer!=null)
            blacklistedTokens.add(token);
    }
}

