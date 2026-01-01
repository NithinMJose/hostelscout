package com.hostelscout.hostel.modules.common.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.hostelscout.hostel.modules.common.enums.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JwtService {

    @Value("${app.security.jwt.secret}")
    private String jwtSecret;

    @Value("${app.security.jwt.expiration-minutes}")
    private long jwtExpirationMinutes;

    // Cache algorithm instance for better performance
    private Algorithm cachedAlgorithm;

    /**
     * Get or create the cached algorithm instance
     */
    private Algorithm getAlgorithm() {
        if (cachedAlgorithm == null) {
            cachedAlgorithm = Algorithm.HMAC256(jwtSecret);
        }
        return cachedAlgorithm;
    }

    /**
     * Generate JWT token with username and role
     */
    public String generateToken(String subject, Role role) {
        Instant now = Instant.now();
        return JWT.create()
                .withSubject(subject)
                .withClaim("role", role.name())
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(now.plus(jwtExpirationMinutes, ChronoUnit.MINUTES)))
                .sign(getAlgorithm());
    }

    /**
     * Validate token and extract username (subject)
     * @throws JWTVerificationException if token is invalid or expired
     */
    public String validateAndGetSubject(String token) throws JWTVerificationException {
        return JWT.require(getAlgorithm())
                .build()
                .verify(token)
                .getSubject();
    }

    /**
     * Validate token and extract role claim
     * @throws JWTVerificationException if token is invalid or expired
     */
    public String validateAndGetRole(String token) throws JWTVerificationException {
        return JWT.require(getAlgorithm())
                .build()
                .verify(token)
                .getClaim("role")
                .asString();
    }

}


