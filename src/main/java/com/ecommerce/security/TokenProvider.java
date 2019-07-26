package com.ecommerce.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ecommerce.model.persistence.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

@Component
public class TokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

    @Value("${JWTSecret}")
    private String jwtSecret;

    @Value("${JWTExpiration}")
    private int jwtExpiration;

    public String generateToken(User user) {
        String jwt = JWT.create()
                .withSubject(user.getUsername())
                .withIssuedAt(Date.from(Instant.now()))
                .withExpiresAt(Date.from(Instant.now().plusSeconds(jwtExpiration)))
                .sign(HMAC512(jwtSecret.getBytes()));
        return String.format("Bearer %s", jwt);
    }

    public String getUsernameFromJWT(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            JWTVerifier jwtVerifier = JWT.require(HMAC512(jwtSecret)).build();
            jwtVerifier.verify(authToken);
            return true;
        } catch (JWTVerificationException exception) {
            logger.error("Invalid JWT signature or claims");
        }
        return false;
    }
}
